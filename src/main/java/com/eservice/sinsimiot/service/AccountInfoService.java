package com.eservice.sinsimiot.service;


import com.eservice.sinsimiot.common.Service;
import com.eservice.sinsimiot.model.account.AccountInfo;
import com.eservice.sinsimiot.model.account.AccountDTO;

import java.util.List;

/**
* Class Description: xxx
* @author Code Generator
* @date 2020/03/06.
*/
public interface AccountInfoService extends Service<AccountInfo> {
    /**
     * 登录
     * @param account 账号
     * @param password 密码
     * @return
     */
    AccountInfo requestLogin(String account, String password);

    /**
     * 根据参数模糊搜索
     * @param accountDTO 参数
     * @return
     */
    List<AccountInfo> findByCondition(AccountDTO accountDTO);

    Boolean checkRoleId(String roleId);

    void updateAccount(String roleId);

    String checkPassword(String password);
}
