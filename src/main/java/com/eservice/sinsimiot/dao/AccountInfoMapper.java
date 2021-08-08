package com.eservice.sinsimiot.dao;


import com.eservice.sinsimiot.common.Mapper;
import com.eservice.sinsimiot.model.account.AccountDTO;
import com.eservice.sinsimiot.model.account.AccountInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @description :
* @author : silent
* @date: 2020/3/19 18:33
*/
public interface AccountInfoMapper extends Mapper<AccountInfo> {

    /**
     * 登录
     * @param account
     * @param password
     * @return
     */
    AccountInfo requestLogin(@Param("account") String account, @Param("password") String password);

    /**
     * 条件模糊搜索
     * 增加按照角色查询
     * @param accountDTO
     * @return
     */
    List<AccountInfo> findByCondition(AccountDTO accountDTO);

    List<AccountInfo> checkRoleId(@Param("roleId") String roleId);
}
