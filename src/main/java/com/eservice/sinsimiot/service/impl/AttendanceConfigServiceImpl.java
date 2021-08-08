package com.eservice.sinsimiot.service.impl;

import com.eservice.sinsimiot.common.AbstractServiceImpl;
import com.eservice.sinsimiot.dao.AttendanceConfigMapper;
import com.eservice.sinsimiot.service.AttendanceConfigService;
import com.eservice.sinsimiot.model.attendance_config.AttendanceConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;


/**
 * Class Description: xxx
 *
 * @author Mr.xie
 * @date 2021/05/12.
 */
@Service
@Transactional
public class AttendanceConfigServiceImpl extends AbstractServiceImpl<AttendanceConfig> implements AttendanceConfigService {
    @Resource
    private AttendanceConfigMapper attendanceConfigMapper;

    @Override
    public boolean save(AttendanceConfig model) {
        model.setId(UUID.randomUUID().toString());
        model.setCreateTime(System.currentTimeMillis() / 1000L);
        if (attendanceConfigMapper.insertSelective(model) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(AttendanceConfig model) {
        model.setUpdateTime(System.currentTimeMillis() / 1000L);
        if (attendanceConfigMapper.updateByPrimaryKeySelective(model) > 0) {
            return true;
        }
        return false;
    }
}
