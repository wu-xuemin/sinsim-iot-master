package com.eservice.sinsimiot.service.impl;

import com.eservice.sinsimiot.model.staff.StaffInfo;
import com.eservice.sinsimiot.service.DepartingStaffService;
import com.eservice.sinsimiot.common.AbstractServiceImpl;
import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.dao.DepartingStaffMapper;
import com.eservice.sinsimiot.model.departing_staff.DepartingStaff;
import com.eservice.sinsimiot.model.staff.StaffSearchDTO;
import com.eservice.sinsimiot.service.StaffInfoService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Class Description: xxx
 *
 * @author Mr.xie
 * @date 2021/06/07.
 */
@Service
@Transactional
@Slf4j
public class DepartingStaffServiceImpl extends AbstractServiceImpl<DepartingStaff> implements DepartingStaffService {
    @Resource
    private DepartingStaffMapper departingStaffMapper;
    @Resource
    private StaffInfoService staffInfoService;

    private Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public boolean save(DepartingStaff model) {
        model.setCreateTime(System.currentTimeMillis());
        if (departingStaffMapper.insertSelective(model) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(DepartingStaff model) {
        model.setUpdateTime(System.currentTimeMillis());
        if (departingStaffMapper.updateByPrimaryKeySelective(model) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Result settingStaff(DepartingStaff departingStaff) {
        if (deleteById(departingStaff.getStaffId())) {
            log.info("departingStaff : {} is delete success", departingStaff.getStaffId());
        }
        StaffInfo staffInfo = new StaffInfo();
        BeanUtils.copyProperties(departingStaff, staffInfo);
        Result result = staffInfoService.saveStaff(staffInfo);
        return result;
    }

    @Override
    public List<DepartingStaff> findByKey(StaffSearchDTO staffSearchDTO) {
        return departingStaffMapper.findByKey(staffSearchDTO);
    }
}
