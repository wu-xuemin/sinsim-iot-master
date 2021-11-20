 package com.eservice.sinsimiot.service.impl;

import com.eservice.sinsimiot.dao.IotMachineMapper;
import com.eservice.sinsimiot.model.iot_machine.IotMachine;
import com.eservice.sinsimiot.service.IotMachineService;
import com.eservice.sinsimiot.common.AbstractServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Class Description: xxx
 * @author Wilson Hu
 * @date 2021/10/11.
 */
@Service
@Transactional
public class IotMachineServiceImpl extends AbstractServiceImpl<IotMachine> implements IotMachineService {
    @Resource
    private IotMachineMapper iotMachineMapper;

    public List<IotMachine> selectIotMachine(String account, String nameplate, String machineModelInfo) {
        return iotMachineMapper.selectIotMachine(account, nameplate, machineModelInfo);
    }

    @Override
    public boolean save(IotMachine model) {
        //原先没有做，没有保存成功
        iotMachineMapper.insert(model);
        return true;
    }

    @Override
    public boolean update(IotMachine model) {
        iotMachineMapper.updateByPrimaryKey(model);
        return false;
    }
}