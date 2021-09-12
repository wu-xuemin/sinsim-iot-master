package com.eservice.sinsimiot.web;

import com.alibaba.fastjson.JSON;
import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
import com.eservice.sinsimiot.model.iot_machine.IotMachine;
//import com.eservice.sinsimiot.model.user.UserDetail;
//import com.eservice.sinsimiot.service.common.Constant;
import com.eservice.sinsimiot.model.iot_machine.IotMachineSearchDTO;
//import com.eservice.sinsimiot.service.impl.IotMachineServiceImpl;
//import com.eservice.sinsimiot.service.impl.UserServiceImpl;
//import com.eservice.sinsimiot.service.mqtt.MqttMessageHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.regex.Pattern;

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

    private Logger logger = Logger.getLogger(IotMachineController.class);
//2021-09-07目前卡在：按时间查询没搞通、查询效率也未知。

    /**
     *  为什么 调用接口却没反应？ 是因为数据太太，上一次的查询一直没结束吗？
     *  --估计是这样，改成小数据量的话，
     *
     *
     * @return
     */
    @RequestMapping(value = "/selectIotMachine", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Object selectIotMachine( @RequestBody @NotNull IotMachineSearchDTO iotMachineSearchDTO ){
//    public Object selectIotMachine( @RequestParam String nameplate,
//                                    @RequestParam String machineModelInfo,
//                                    @RequestParam String account,
//                                    @RequestParam String queryStartTime,
//                                    @RequestParam String queryEndTime){
        logger.info("selectIotMachine");
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
       System.out.println("查询开始，15分钟没反应，照理查询时间很快啊。。。没加时间也卡，，/**/");
        query.limit(9);
//        List<IotMachine> list = mongoTemplate.find(query, IotMachine.class, COLLECTION_NAME_SMALL);
        List<IotMachine> list = mongoTemplate.find(query, IotMachine.class, COLLECTION_NAME );

        System.out.println("查询完成, 查到了个数：" + list.size());
        for(int i=0; i<list.size(); i++){
            Date date = new Date(Long.parseLong(Integer.parseInt(list.get(i).getId().toString().substring(0, 8), 16) + "000"));
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

        }
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


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
        logger.info("selectIotMachine");
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


        logger.info(msg);
        return ResultGenerator.genSuccessResult(msg);
    }
}
