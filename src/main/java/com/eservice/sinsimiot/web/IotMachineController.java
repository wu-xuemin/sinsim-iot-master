package com.eservice.sinsimiot.web;

import com.alibaba.fastjson.JSON;
import com.eservice.sinsimiot.common.*;
import com.eservice.sinsimiot.model.iot_machine.*;
//import com.eservice.sinsimiot.model.user.UserDetail;
//import com.eservice.sinsimiot.service.common.Constant;
//import com.eservice.sinsimiot.service.impl.IotMachineServiceImpl;
//import com.eservice.sinsimiot.service.impl.UserServiceImpl;
//import com.eservice.sinsimiot.service.mqtt.MqttMessageHelper;
import com.eservice.sinsimiot.service.IotMachineRepository;
import com.eservice.sinsimiot.service.PatternService;
import com.eservice.sinsimiot.service.impl.IotMachineServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
//import org.apache.log4j.Logger;
//import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
//import com.eservice.sinsimiot.model.pattern.Pattern;

/**
* Class Description: xxx
* @author Wilson Hu
* @date 2021/04/25.
*/
@RestController
@RequestMapping("/iot/machine")
public class IotMachineController {

    /** 设置集合名称 */
    private static final String COLLECTION_NAME = "iot_machine";
    private static final String COLLECTION_NAME_SMALL = "iot_machine_small";

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private IotMachineServiceImpl iotMachineService;

    @Resource
    private AccessAftersaleService accessAftersaleService;

    @Resource
    private PythonService pythonService;

    @Value("${spring.data.pythonAccessMongo}")
    private String pythonAccessMongo;

    @Resource
    private AccessSinsimProcessService accessSinsimProcessService;

    @Resource
    private PatternService patternService;
    /*
     * 从售后数据库访问，获取机型信息等
     * (机器的来源)
     */
    @Autowired
    @Qualifier("DataSourceAftersaleDbTemplate")
    private JdbcTemplate dataSourceAftersaleDbTemplate;

    @Autowired
    public IotMachineRepository iotMachineRepository;

//    private Logger logger = Logger.getLogger(IotMachineController.class);
    private Logger logger = LoggerFactory.getLogger(getClass());
//2021-09-07目前卡在：按时间查询没搞通、查询效率也未知。

    /**
     *  为什么 调用接口却没反应？ 是因为数据太太，上一次的查询一直没结束吗？
     *  --估计是这样，改成小数据量的话，
     *  从monogodb查询数据。
     *  为了提高查询速度，只支持结尾模糊的查询 --貌似NG， 那先做完全匹配--也NG
	 
     *  因为通过JAVA mongoTemplate 查询，6千万时间耗时约35秒，索引貌似没起效。
     *  所以改为JAVA调用 python, python访问mongodb。
     */
    @RequestMapping(value = "/selectIotMachine", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Object selectIotMachine( @RequestBody @NotNull IotMachineSearchDTO iotMachineSearchDTO ){
        logger.info("selectIotMachine");
        String nameplate = iotMachineSearchDTO.getNameplate();
//        List<AftersaleMachine> list = accessAftersaleService.accessAs();
        String result = pythonService.callPythonScript(pythonAccessMongo, nameplate);

//        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(result);
    }
    
    @RequestMapping(value = "/selectIotMachineByMongoTemplate", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Object selectIotMachineByMongoTemplate( @RequestBody @NotNull IotMachineSearchDTO iotMachineSearchDTO ){
//    public Object selectIotMachine( @RequestParam String nameplate,
//                                    @RequestParam String machineModelInfo,
//                                    @RequestParam String account,
//                                    @RequestParam String queryStartTime,
//                                    @RequestParam String queryEndTime){
        logger.info("selectIotMachineByMongoTemplate");
        String account = iotMachineSearchDTO.getUser();
        String nameplate = iotMachineSearchDTO.getNameplate();
        String machineModelInfo = iotMachineSearchDTO.getMachineModelInfo(); ///为啥这里会执行很长时间。。。好像是因为debug断点
        Long st = iotMachineSearchDTO.getQueryStartTime();
        Long ed = iotMachineSearchDTO.getQueryEndTime();
        Date queryStartTime = null;
        Date queryEndTime = null;
        if(st != null && ed != null) {
            queryStartTime = longToDate(st);
            queryEndTime = longToDate(iotMachineSearchDTO.getQueryEndTime());
        }
//        Date dateStart =   iotMachineSearchDTO.getQueryStartTime() ;

////完全匹配
//        Pattern pattern = Pattern.compile("^张$", Pattern.CASE_INSENSITIVE);
////右匹配
//        Pattern pattern = Pattern.compile("^.*张$", Pattern.CASE_INSENSITIVE);
////左匹配
//        Pattern pattern = Pattern.compile("^张.*$", Pattern.CASE_INSENSITIVE);
//模糊匹配
//        Pattern pattern = Pattern.compile("^.*nameplate.*$", Pattern.CASE_INSENSITIVE);
        Query query = new Query();
        if( account!=null && !account.isEmpty()) {
            Pattern pattern = Pattern.compile("^.*" + account + ".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(
                    new Criteria().and("user").regex(pattern)
            );
        }

        if( nameplate!=null && !nameplate.isEmpty()) {
            Pattern pattern = Pattern.compile("^.*" + nameplate + ".*$", Pattern.CASE_INSENSITIVE);
                query.addCriteria(
                        new Criteria().and("nameplate").regex(pattern)
                );
        }
        if( machineModelInfo!=null && !machineModelInfo.isEmpty()) {
            Pattern pattern = Pattern.compile("^.*" + machineModelInfo + ".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(
                    new Criteria().and("machineModelInfo").regex(pattern)
            );
        }
//        query.addCriteria( new Criteria().and("machineModelInfo").regex(pattern));
//        query.addCriteria(Criteria.where("time").lte(dateStart));
//        query.addCriteria(where("objectId").gte(new ObjectId(LocalDateTimeUtil.LocalDateTimeToUdate(order.getCreateDate()))).lte(new ObjectId(LocalDateTimeUtil.LocalDateTimeToUdate(order.getCreateEndDate()))));
//       if(iotMachineSearchDTO.getQueryStartTime()!=null && iotMachineSearchDTO.getQueryEndTime()!= null) {
//           String start = Long.toHexString(iotMachineSearchDTO.getQueryStartTime()) + "0000000000000000";
//           String end = Long.toHexString(iotMachineSearchDTO.getQueryEndTime()) + "0000000000000000";
//
//           query.addCriteria(Criteria.where("_id").gt(new ObjectId(start)).lt(new
//                   ObjectId(end)));
//       }

        if(queryStartTime!=null && queryEndTime!= null) {

            //iotMachineSearchDTO.getQueryStartTime()值类似为 1631006497 是unix时间戳


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            System.out.println("starting...开始时间：" + simpleDateFormat.format(queryStartTime));
            System.out.println(" ...结束时间：" + simpleDateFormat.format(queryEndTime));
            Criteria c = Criteria.where("createdTime").lt( simpleDateFormat.format(queryEndTime))
                    .gte( (simpleDateFormat.format(queryStartTime)));
            query.addCriteria(c);

//            Date date1 = new Date(date);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date1);
//            calendar.add(Calendar.MONTH, 1);
//            Query query = new Query();
//            query.addCriteria(Criteria.where("inspectStaffid").is(id)
//                    .andOperator(
//                            Criteria.where("inspectDate").lt(dateToISODate(calendar.getTime())),
//                            Criteria.where("inspectDate").gte(dateToISODate(date1))));
        }
        logger.info("mongo查询开始..."+ query.toString());
//        query.limit(9);
//        List<IotMachine> list = mongoTemplate.find(query, IotMachine.class, COLLECTION_NAME_SMALL);
        //2021-1011 主页面查询，显示的机器列表，机器铭牌不重复--难做到，所以改为把IOT机器的基本信息保存到mysql中，不包含历史数据，所以数据量小
        List<IotMachineMongo> list = mongoTemplate.find(query, IotMachineMongo.class, COLLECTION_NAME );

//        List<IotMachineMongo> list = mongoTemplate.find(Query.query(TextCriteria.forDefaultLanguage().matching("要搜索的内容")), IotMachineMongo.class);
        //findDistinct 返回的只有单一字段，比如这里只返回了去重后的nameplate字段列表。
//        List<IotMachine> list = mongoTemplate.findDistinct(query,"nameplate",COLLECTION_NAME, IotMachine.class);


        logger.info("mongo 查询完成, 查到了个数：" + list.size() );
        for(int i=0; i<list.size(); i++){
            Date date = new Date(Long.parseLong(Integer.parseInt(list.get(i).getId().toString().substring(0, 8), 16) + "000"));
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

        }
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 2021-1011 主页面查询，显示的机器列表，机器铭牌不重复--难做到，所以改为把IOT机器的基本信息保存到mysql中，不包含历史数据，所以数据量小
     * 查询mysql中的IOT基本信息
     */
    @PostMapping("/selectIotMachineBaseInfo")
    public Result selectIotMachineBaseInfo(@RequestParam(defaultValue = "0") Integer page,
                                           @RequestParam(defaultValue = "11") Integer size,
                                           @RequestParam(defaultValue = "")String account,
                                           @RequestParam(defaultValue = "")String nameplate,
                                           @RequestParam(defaultValue = "") String machineModelInfo) {
        PageHelper.startPage(page, size);
        List<IotMachine> list = iotMachineService.selectIotMachine(account, nameplate, machineModelInfo);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    //根据 ObjectId查询
//    import org.bson.types.ObjectId;
//
//
//    public abstract class MangoBaseDaoImpl<T> implements MangoBaseDao<T> {
//        protected abstract Class<T> getEntityClass();
//        @Autowired
//        protected MongoTemplate mongoTemplate;
//        @Override
//        public long deleteByDate(long startTimeStamp,long endTimeStamp){
//            String start = Long.toHexString(startTimeStamp)+"0000000000000000";
//            String end = Long.toHexString(endTimeStamp)+"0000000000000000";
//            Query query = new Query();
//            query.addCriteria(Criteria.where("_id").gt(new ObjectId(start)).lt(new
//                    ObjectId(end)));
//            return mongoTemplate.remove(query,getEntityClass()).getDeletedCount();
//        }


    public static Date strToDateLong(String strDate) {
        Date strtodate = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            strtodate = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strtodate;
    }


    public static Date dateToISODate(String dateStr){
        //T代表后面跟着时间，Z代表UTC统一时间
        Date date = formatD(dateStr);
        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
        String isoDate = format.format(date);
        try {
            return format.parse(isoDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static Date formatD(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
        Date ret = null ;
        try {
            ret = simpleDateFormat.parse(dateStr) ;
        } catch (ParseException e) {
            //
        }
        return ret;
    }


    public static Date longToDate(long lo){
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  (date);
    }
    //一亿级别数据查询测试
    @RequestMapping(value = "/selectHugeSizeDocument", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Object selectHugeSizeDocument( @RequestBody @NotNull IotMachineSearchDTO iotMachineSearchDTO ){
        logger.info("selectHugeSizeDocument");
        String account = iotMachineSearchDTO.getUser();
        String nameplate = iotMachineSearchDTO.getNameplate();
        String machineModelInfo = iotMachineSearchDTO.getMachineModelInfo();

////完全匹配
//        Pattern pattern = Pattern.compile("^张$", Pattern.CASE_INSENSITIVE);
////右匹配
//        Pattern pattern = Pattern.compile("^.*张$", Pattern.CASE_INSENSITIVE);
////左匹配
//        Pattern pattern = Pattern.compile("^张.*$", Pattern.CASE_INSENSITIVE);
//模糊匹配
//        Pattern pattern = Pattern.compile("^.*nameplate.*$", Pattern.CASE_INSENSITIVE);
        Query query = new Query();
        if( account!=null && !account.isEmpty()) {
            Pattern pattern = Pattern.compile("^.*" + account + ".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(
                    new Criteria().and("nameplate").regex(pattern)
            );
        }

        if( nameplate!=null && !nameplate.isEmpty()) {
            Pattern pattern = Pattern.compile("^.*" + nameplate + ".*$", Pattern.CASE_INSENSITIVE);
                query.addCriteria(
                        new Criteria().and("nameplate").regex(pattern)
                );
        }
        if( machineModelInfo!=null && !machineModelInfo.isEmpty()) {
            Pattern pattern = Pattern.compile("^.*" + machineModelInfo + ".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(
                    new Criteria().and("machineModelInfo").regex(pattern)
            );
        }
        List<IotMachine> list = mongoTemplate.find(query, IotMachine.class, COLLECTION_NAME);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/updateInfo")
//    public Result updateInfo(@RequestParam  String iotMachine) {
    public Result updateInfo(@RequestBody String iotMachine,
                             @RequestParam String account) {


        logger.info("updateInfo:" + iotMachine + ", account:" + account);

        IotMachine iotMachine1 = JSON.parseObject(iotMachine, IotMachine.class);
        String msg = null;
        if (iotMachine1 == null) {
            msg = "iotMachine1对象JSON解析失败！";
//            logger.warn(msg);
            return ResultGenerator.genFailResult(msg);
        }
        iotMachine1.setUser(account);
        mongoTemplate.save( iotMachine1, COLLECTION_NAME);

        /** 2021-1011
         * 用mongoTemplate.findDistinct去重,只返回单一字段。不支持多字段返回
         * 用mongoTemplate.aggregate去重，排序是查询效率会变的非常低。暂时不明确方法，待续
         * 所以，同时在mysql里也存储基本信息（不包含历史信息），目前用于WEB/APP主页查询
         */
        List<IotMachine> iotMachineExistList = iotMachineService.selectIotMachine(account,
                iotMachine1.getNameplate(),iotMachine1.getMachineModelInfo());
        if(iotMachineExistList != null& iotMachineExistList.size()!= 0){
            logger.info(iotMachine1.getNameplate() + "，该机器基本信息 已经存在，不需要记录到mysql");
        } else {
            logger.info(iotMachine1.getNameplate() + ",需记录该机器的基本信息到mysql");
            iotMachineService.save(iotMachine1);
        }

        logger.info(msg);
        return ResultGenerator.genSuccessResult(msg);
    }

    /**
     * 从售后部 获取机器机型信息
     */
    @PostMapping("/getMachineModelInfoFromAftersale")
    public Result getMachineModelInfoFromAftersale(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            @RequestParam(defaultValue = "")String nameplate) {
        PageHelper.startPage(page, size);
  
//        String query = "select * from machine m  where m.nameplate = 'nameplateXX'".replace("nameplateXX",nameplate);
//        List<AftersaleMachine> list = dataSourceAftersaleDbTemplate.query(query, new BeanPropertyRowMapper(AftersaleMachine.class));
//        PageInfo pageInfo = new PageInfo(list);

        //两种方式访问都OK,获取售后数据正常。
        List<AftersaleMachine> list = accessAftersaleService.accessAs();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 从生产部 获取机器类型
     */
    @PostMapping("/getMachineTypeListFromSinsimProcess")
    public Result getMachineTypeListFromSinsimProcess(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            String nameplate) {
        PageHelper.startPage(page, size);

        List<SinsimProcessMachineType> list = accessSinsimProcessService.getMachineType();
        PageInfo pageInfo = new PageInfo(list);

        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据铭牌号，获取某机器的花样
     * （都是sinsim的机器，铭牌号应该不重复）
     */
    @PostMapping("/getMachinePattern")
    public Result getMachinePattern(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            String nameplate) {
        PageHelper.startPage(page, size);

        //先获取机器
        IotMachine iotMachine = null;
        List<IotMachine> iotMachineList = iotMachineService.selectIotMachine("", nameplate, "");
       //Pattern名称冲突了 所以写完整
        List<com.eservice.sinsimiot.model.pattern.Pattern> patternList = new ArrayList<>();
        if(iotMachineList !=null && iotMachineList.size() !=0 ){
            iotMachine = iotMachineList.get(0); //（都是sinsim的机器，铭牌号应该不重复, 获取唯一机器）

            String strPattern = iotMachine.getPattern();
            if(strPattern != null) {
                String[] arrayList = null;
                arrayList = strPattern.split(",");
                if (arrayList != null && arrayList.length != 0) {
                    for (int i = 0; i < arrayList.length; i++) {
                        com.eservice.sinsimiot.model.pattern.Pattern pattern = patternService.findById(Integer.valueOf(arrayList[i]));
                        patternList.add(pattern);
                    }
                }
            }

        }
        PageInfo pageInfo = new PageInfo(patternList);

        return ResultGenerator.genSuccessResult(pageInfo);
    }

    // MongoRepository 方式查询
//    @Test
    @PostMapping("/findIotMachineList")
    public Result findIotMachineList(  @RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "0") Integer size,
                               String nameplate) {
        PageHelper.startPage(page, size);
        IotMachine iotMachine = new IotMachine();
        iotMachine.setNameplate(nameplate);
        Example<IotMachine> iotMachineExample = Example.of(iotMachine);
        List<IotMachine> iotMachineList = iotMachineRepository.findAll(iotMachineExample);
        PageInfo pageInfo = new PageInfo(iotMachineList);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
