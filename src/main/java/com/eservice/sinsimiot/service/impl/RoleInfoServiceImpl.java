package com.eservice.sinsimiot.service.impl;



import com.eservice.sinsimiot.common.AbstractServiceImpl;
import com.eservice.sinsimiot.dao.RoleInfoMapper;
import com.eservice.sinsimiot.model.role.RoleInfo;
import com.eservice.sinsimiot.service.RoleInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;


/**
 * Class Description: xxx
 *
 * @author Code Generator
 * @date 2020/03/06.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleInfoServiceImpl extends AbstractServiceImpl<RoleInfo> implements RoleInfoService {
    @Resource
    private RoleInfoMapper roleInfoMapper;

    /**
     * 指定条件查询
     *
     * @param condition
     * @return
     */
    @Override
    public List<RoleInfo> findByCondition(String condition) {
        return roleInfoMapper.findByCondition(condition);
    }

    @Override
    public boolean save(RoleInfo model) {
        model.setRoleId(UUID.randomUUID().toString());
        model.setCreateTime(System.currentTimeMillis());
        model.setUpdateTime(System.currentTimeMillis());
        if (roleInfoMapper.insertSelective(model) > 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean update(RoleInfo model) {
        model.setUpdateTime(System.currentTimeMillis());
        if (roleInfoMapper.updateByPrimaryKeySelective(model) > 0) {
            return true;
        } else {
            return false;
        }

    }
}
