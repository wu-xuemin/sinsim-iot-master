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
//import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/selectIotMachines", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Object selectIotMachines(@RequestBody @NotNull IotMachineSearchDTO iotMachineSearchDTO){

//        IotMachine iotMachine = new IotMachine();
//        iotMachine.setLineBrokenAverageTime("tt");
//        iotMachine.setLineBrokenNumber("33");
//        iotMachine.setMachineModelInfo("modaaa");
//        iotMachine.setNameplate("nnnn");
//        iotMachine.setNeedleTotalNumber("66");
//        iotMachine.setNonworkingTime("22");
//        iotMachine.setPowerOnTimes("31");
//        iotMachine.setUptime("311");
//        iotMachine.setUser("sss");
//        iotMachine.setWorkingTime("11");
//        iotMachine.setProductTotalNumber("677");
////        iotMachine.setId();
//        mongoTemplate.save( iotMachine, COLLECTION_NAME);
        List<IotMachine> list = mongoTemplate.findAll(IotMachine.class, COLLECTION_NAME);
        PageHelper.startPage(iotMachineSearchDTO.getPage(), iotMachineSearchDTO.getLimit());
//        return list;
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
