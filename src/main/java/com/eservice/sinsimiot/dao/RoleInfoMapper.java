package com.eservice.sinsimiot.dao;

import com.eservice.sinsimiot.common.Mapper;
import com.eservice.sinsimiot.model.role.RoleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @description : 角色管理
* @author : silent
* @date: 2020/3/23 14:22
*/
public interface RoleInfoMapper extends Mapper<RoleInfo> {
    /**
     * 指定条件查询
     * @param condition
     * @return
     */
    List<RoleInfo> findByCondition(@Param("condition") String condition );
}
