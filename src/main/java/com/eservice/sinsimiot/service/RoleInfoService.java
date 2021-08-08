package com.eservice.sinsimiot.service;



import com.eservice.sinsimiot.common.Service;
import com.eservice.sinsimiot.model.role.RoleInfo;

import java.util.List;

/**
* Class Description: xxx
* @author Code Generator
* @date 2020/03/06.
*/
public interface RoleInfoService extends Service<RoleInfo> {

    /**
     * 指定条件查询
     * @param condition
     * @return
     */
    List<RoleInfo> findByCondition(String condition );
}
