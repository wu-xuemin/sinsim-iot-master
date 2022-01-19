package com.eservice.sinsimiot.service.impl;


import com.eservice.sinsimiot.common.AbstractServiceImpl;
import com.eservice.sinsimiot.core.ValidateConstant;
import com.eservice.sinsimiot.dao.AccountInfoMapper;
import com.eservice.sinsimiot.model.account.AccountInfo;
import com.eservice.sinsimiot.model.account.AccountDTO;
import com.eservice.sinsimiot.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Slf4j
public class AccountInfoServiceImpl extends AbstractServiceImpl<AccountInfo> implements AccountInfoService {

    @Resource
    private AccountInfoMapper accountInfoMapper;

    private Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public AccountInfo requestLogin(String account, String password) {
        return accountInfoMapper.requestLogin(account, password);
    }

    /**
     * 根据参数模糊搜索
     *
     * @param AccountDTO 参数
     * @return
     */
    @Override
    public List<AccountInfo> findByCondition(AccountDTO AccountDTO) {
        return accountInfoMapper.findByCondition(AccountDTO);
    }

    @Override
    public Boolean checkRoleId(String roleId) {
        List<AccountInfo> accountInfos = accountInfoMapper.checkRoleId(roleId);
        if (accountInfos != null || accountInfos.size() > 0) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void updateAccount(String roleId) {
        List<AccountInfo> accountInfos = accountInfoMapper.checkRoleId(roleId);
        if (accountInfos != null || accountInfos.size() > 0) {
            for (AccountInfo accountInfo : accountInfos) {
                log.info("old AccountInfo : {}", accountInfo);
                accountInfo.setRoleId("");
                accountInfo.setUpdateTime(System.currentTimeMillis());
                update(accountInfo);
                log.info("new AccountInfo : {} ", accountInfo);
            }
        }
    }

    @Override
    public String checkPassword(String password) {
        if (password == null || password.length() < 6) {
            return "password length is greater than 6";
        } else if (!password.matches(ValidateConstant.REG_NUMBER)) {
            return "password must contain numbers";
        } else if (!password.matches(ValidateConstant.REG_LOWERCASE)) {
            return "Password must contain lowercase letters";
        } else if (!password.matches(ValidateConstant.REG_UPPERCASE)) {
            return "Password must contain uppercase letters";
        } else if (!password.matches(ValidateConstant.REG_SYMBOL)) {
            return "Password must contain special characters";
        }
        return null;
    }

    @Override
    public boolean save(AccountInfo model) {
        model.setAccountId(UUID.randomUUID().toString());
        model.setCreateTime(System.currentTimeMillis());
        model.setUpdateTime(System.currentTimeMillis());
        if (accountInfoMapper.insertSelective(model) > 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean update(AccountInfo model) {
        model.setUpdateTime(System.currentTimeMillis());
        if (accountInfoMapper.updateByPrimaryKeySelective(model) > 0) {
            return true;
        } else {
            return false;
        }

    }
}
