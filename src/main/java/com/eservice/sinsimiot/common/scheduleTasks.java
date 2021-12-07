package com.eservice.sinsimiot.common;

import com.eservice.sinsimiot.core.Constant;
import com.eservice.sinsimiot.model.iot_machine.IotMachine;
import com.eservice.sinsimiot.service.impl.IotMachineServiceImpl;
import org.apache.log4j.Logger;
import org.python.antlr.ast.Str;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * @author eservice
 */
@Service
public class scheduleTasks {

    @Resource
    private IotMachineServiceImpl iotMachineService;

    private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 每次启动都会执行一次
     * 每 scheduleHours 执行一次
     * 更新机器的 真实状态，是在last_status基础上更新是否离线
     * 判断方法：updated_time距离当前时间超过了定时更新的时间间隔，则判断为离线。
     *
     * fixedRate 值的单位为毫秒
     */
//    @Scheduled(fixedRate = 1000 * 60 * 60 * Constant.scheduleHours )
    @Scheduled(fixedRate = 1000 * 60 * 1 * Constant.scheduleHours /2) //分钟
    public void scheduleHoursUpdateIotMachineStatus() {
        String msg = "";
        List<IotMachine> iotMachineExistList = iotMachineService.selectIotMachine(
                null,
                null,
                null);
        if(iotMachineExistList == null){
            logger.info("scheduleHoursUpdateIotMachineStatus: iotMachineExistList 为空");
            return;
        }
        //系统时间返回当前值
        java.util.Date nowDate = new Date(System.currentTimeMillis());
        long nowDateLong = nowDate.getTime();

        long machineUpdateTimeLong = 0;
        long differenceMinutes = 0;

        for(int i=0; i<iotMachineExistList.size(); i++){

            machineUpdateTimeLong =   iotMachineExistList.get(i).getUpdatedTime().getTime();
            differenceMinutes = (nowDateLong - machineUpdateTimeLong) / (1000 * 60);
            //logger.info("差了分钟数：" + differenceMinutes);
            if(differenceMinutes/60 > Constant.scheduleHours){
                if( ! iotMachineExistList.get(i).getMachineStatus().equals(Constant.MACHINE_STATUS_OFFLINE)) {
                    iotMachineExistList.get(i).setMachineStatus(Constant.MACHINE_STATUS_OFFLINE);
                    iotMachineService.update(iotMachineExistList.get(i));
                    logger.info( "该机器超时未上报，判断为离线，且更新为离线 " + iotMachineExistList.get(i).getNameplate());
                }
            }
        }
    }
}
