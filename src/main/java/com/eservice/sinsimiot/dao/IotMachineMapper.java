package com.eservice.sinsimiot.dao;

import com.eservice.sinsimiot.common.Mapper;
import com.eservice.sinsimiot.model.iot_machine.IotMachine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IotMachineMapper extends Mapper<IotMachine> {

    List<IotMachine> selectIotMachine(@Param("account")String  account,
                                      @Param("nameplate")String  nameplate,
                                      @Param("machineModelInfo")String machineModelInfo);
}