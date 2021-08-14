package com.eservice.sinsimiot.web;

import com.alibaba.fastjson.JSON;
import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
import com.eservice.sinsimiot.model.iot_machine.IotMachine;
//import com.eservice.sinsimiot.model.user.UserDetail;
//import com.eservice.sinsimiot.service.common.Constant;
import com.eservice.sinsimiot.model.iot_machine.IotMachineSearchDTO;
import com.eservice.sinsimiot.service.impl.IotMachineServiceImpl;
//import com.eservice.sinsimiot.service.impl.UserServiceImpl;
//import com.eservice.sinsimiot.service.mqtt.MqttMessageHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
//import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
* Class Description: xxx
* @author Wilson Hu
* @date 2021/04/25.
*/
@RestController
@RequestMapping("/iot/machine")
public class IotMachineController {
    @Resource
    private IotMachineServiceImpl iotMachineService;

//    @Resource
//    private MqttMessageHelper mqttMessageHelper;

//    @Resource
//    private UserServiceImpl userService;

    @PostMapping("/add")
    public Result add(IotMachine iotMachine) {
        iotMachineService.save(iotMachine);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 接收绣花机传来的信息
     * 如果该绣花机已经存在，则更新该机器信息
     * 如果该绣花机不存在，则新增该绣花机
     *
     * account/password
     */
    @PostMapping("/updateInfo")
//    public Result updateInfo(@RequestParam  String iotMachine) {
        public Result updateInfo(@RequestBody String iotMachine,
                                 @RequestParam String account,
                                 @RequestParam String password) {

//        User user = userService.selectByAccount(account);
//        if(user == null){
//            return ResultGenerator.genFailResult(account + " 不存在该账号！");
//        } else if(user.getValid() == Constant.INVALID){
//            return ResultGenerator.genFailResult("禁止登录，" + account + " 已设为离职");
//        }
//        UserDetail userDetail = userService.requestLogin(account, password);
//        if(userDetail == null) {
//            logger.info(account + "login 账号或密码不正确");
//            return ResultGenerator.genFailResult("账号或密码不正确！");
//        }else {
//            logger.info(account + "login success");
//        }

        IotMachine iotMachine1 = JSON.parseObject(iotMachine, IotMachine.class);
        String msg = null;
        if (iotMachine1 == null) {
            msg = "iotMachine1对象JSON解析失败！";
//            logger.warn(msg);
            return ResultGenerator.genFailResult(msg);
        }

        // 暂定 机器铭牌号是唯一的，后续可以考虑 同个品牌下的铭牌号是唯一
        Condition tempCondition = new Condition(IotMachine.class);
        tempCondition.createCriteria().andCondition("nameplate = ", iotMachine1.getNameplate());
//        List<IotMachine> existIotMachines = iotMachineService.findByCondition(tempCondition);

        /**
         * 改为要记录历史记录之后，要记录创建时间，更新时间就没用了
          */
        iotMachine1.setCreateTime(new Date());
        iotMachine1.setUser(account);
        iotMachineService.save(iotMachine1);
        msg = iotMachine1.getNameplate() + iotMachine1.getCreateTime() + ",已记录该机器的信息";

//        log.info(msg);
        return ResultGenerator.genSuccessResult(msg);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        iotMachineService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(IotMachine iotMachine) {
        iotMachineService.update(iotMachine);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        IotMachine iotMachine = iotMachineService.findById(id);
        return ResultGenerator.genSuccessResult(iotMachine);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<IotMachine> list = iotMachineService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 发送MQTT消息，
     * （给树莓派，树莓派收到该MQTT消息后返回最新的机器信息给服务器）
     */
//    @PostMapping("/refreshMachineInfo")
//    public Result refreshMachineInfo(@RequestParam(defaultValue = "allMachines")  String iotMachineNameplate) {
//
//        mqttMessageHelper.sendToClient(Constant.S2C_REFRESH_MACHINE_INFO, iotMachineNameplate );
//        return ResultGenerator.genSuccessResult();
//    }

    /**
     * 根据账户、铭牌号 获取该账户名下对应的机器IOT信息
     * 比如用户可以以此查看自己的机器
     * account:  为空时则不限账户
     * nameplate：为空时则不限铭牌
     * machineModelInfo：为空时不限制机器型号
     */
//    @PostMapping("/selectIotMachine")
//    public Result selectIotMachine(@RequestParam(defaultValue = "0") Integer page,
//                                   @RequestParam(defaultValue = "0") Integer size,
//                                   @RequestParam(defaultValue = "")String account,
//                                   @RequestParam(defaultValue = "")String nameplate,
//                                   @RequestParam(defaultValue = "")String machineModelInfo) {
//        PageHelper.startPage(page, size);
//        List<IotMachine> list = iotMachineService.selectIotMachine(account, nameplate, machineModelInfo);
//        PageInfo pageInfo = new PageInfo(list);
//        return ResultGenerator.genSuccessResult(pageInfo);
//    }
    /**
     * 统一按原工程的DTO方式查询.
     * (其实之前也是DTO了，返回的整个对象全部信息，而不是一个一个地查询和获取)
     * @param iotMachineSearchDTO
     * @return
     */
    @RequestMapping(value = "/selectIotMachine", method = RequestMethod.POST)
    public Result search(@RequestBody @NotNull IotMachineSearchDTO iotMachineSearchDTO) {
        PageHelper.startPage(iotMachineSearchDTO.getPage(), iotMachineSearchDTO.getLimit());
        List<IotMachine> list = iotMachineService.selectIotMachine(
                iotMachineSearchDTO.getUser(),
                iotMachineSearchDTO.getNameplate(),
                iotMachineSearchDTO.getMachineModelInfo());
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    /**
     * 让服务器发送MQTT消息
     * 可用于：通过发送MQTT消息，通知订阅了某topic的树莓派执行某个动作，比如把数据发给服务器
     * 这样就不需要服务器通过HTTP访问树莓派了。
     */
//    @PostMapping("/sendMqtt")
//    public void sendMqtt(String topic) {
//        logger.info("sendMqttToIotMachine ：发MQTT （给树莓派）: " + topic);
//        mqttMessageHelper.sendToClient(topic, "by sendMqtt" );
//    }
}
