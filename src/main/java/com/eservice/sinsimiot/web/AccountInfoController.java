package com.eservice.sinsimiot.web;

import com.eservice.sinsimiot.core.MessageConstant;
import com.eservice.sinsimiot.core.OperationConstant;
import com.eservice.sinsimiot.model.account.AccountInfo;
import com.eservice.sinsimiot.service.LogRecordService;
import com.eservice.sinsimiot.util.EncryptUtil;
import com.eservice.sinsimiot.util.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
import com.eservice.sinsimiot.model.account.AccountDTO;
import com.eservice.sinsimiot.service.AccountInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Description: xxx
 *
 * @author Code Generator
 * @date 2020/03/06.
 */
@RestController
@RequestMapping("/account/info")
public class AccountInfoController {

    private final String MODULE = "账号管理";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private AccountInfoService accountInfoService;

    @Resource
    private LogRecordService logRecordService;

    @Value("${aes.key}")
    private String AES_KEY;
    @Value("${aes.iv}")
    private String AES_IV;

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 账号管理
     * @title 新增
     * @description 账号新增
     * @method POST
     * @url IP/api/account/info/add
     * @header token 必选 string token
     * @json_param {"userName":"str","account":"str","password":"str","roleId":"str","parkId":"str"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestHeader("token") String token, @RequestBody @NotNull AccountInfo accountInfo) {
        String account = JwtUtil.getAccount(token);
        String password = EncryptUtil.decrypt(accountInfo.getPassword(), AES_KEY, AES_IV);
        String checkResult = accountInfoService.checkPassword(password);
        if (checkResult != null) {
            return ResultGenerator.genPassWordFailResult(checkResult);
        }
        if (accountInfoService.save(accountInfo)) {
            logRecordService.save(account, MODULE, OperationConstant.ADD_CN, String.format("用户名：%s，账号：%s，新增成功", accountInfo.getUserName(), accountInfo.getAccount()));
            return ResultGenerator.genSuccessResult(account);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.ADD_CN, String.format("用户名：%s，账号：%s，新增失败", accountInfo.getUserName(), accountInfo.getAccount()));
            return ResultGenerator.genFailResult(MessageConstant.ADD_FAILED_CN);
        }
    }

    /**
     * showdoc
     *
     * @param accountId 必选 string 账号ID
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 账号管理
     * @title 删除
     * @description 删除账号
     * @method DELETE
     * @url IP/api/account/info/delete/{accountId}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/delete/{accountId}", method = RequestMethod.DELETE)
    public Result delete(@RequestHeader("token") String token, @PathVariable @NotNull String accountId) {
        String account = JwtUtil.getAccount(token);
        AccountInfo accountInfo = accountInfoService.findById(accountId);
        if (accountInfo == null) {
            return ResultGenerator.genSuccessResult(accountId, MessageConstant.DOES_NOT_EXIST_CN);
        }
        if (accountInfoService.deleteById(accountId)) {
            logRecordService.save(account, MODULE, OperationConstant.DELETE_CN, String.format("用户名：%s，账号：%s，删除成功", accountInfo.getUserName(), accountInfo.getAccount()));
            return ResultGenerator.genSuccessResult();
        } else {
            logRecordService.save(account, MODULE, OperationConstant.DELETE_CN, String.format("用户名：%s，账号：%s，删除失败", accountInfo.getUserName(), accountInfo.getAccount()));
            return ResultGenerator.genFailResult(MessageConstant.DELETE_FAILED_CN);
        }
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 账号管理
     * @title 修改
     * @description 修改账号
     * @method PUT
     * @url IP/api/account/info/update
     * @header token 必选 string token
     * @json_param {"accountId":"str","userName":"str","account":"str","password":"str","roleId":"str","parkId":"str"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result update(@RequestHeader("token") String token, @RequestBody @NotNull AccountInfo accountInfo) {
        String account = JwtUtil.getAccount(token);
        AccountInfo oldAccountInfo = accountInfoService.findById(accountInfo.getAccountId());
        System.out.println(accountInfo.getPassword());
        String password = EncryptUtil.decrypt(accountInfo.getPassword(), AES_KEY, AES_IV);
        String checkResult = accountInfoService.checkPassword(password);
        if (checkResult != null) {
            return ResultGenerator.genPassWordFailResult(checkResult);
        }
        if (oldAccountInfo == null) {
            return ResultGenerator.genFailResult(MessageConstant.DOES_NOT_EXIST_CN);
        } else {
            if (!oldAccountInfo.getPassword().equals(accountInfo.getPassword())) {
                accountInfo.setPassword(accountInfo.getPassword());
            }
        }
        if (accountInfoService.update(accountInfo)) {
            logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("用户名：%s，账号：%s，修改成功", accountInfo.getUserName(), accountInfo.getAccount()));
            return ResultGenerator.genSuccessResult(account);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("用户名：%s，账号：%s，修改失败", oldAccountInfo.getUserName(), oldAccountInfo.getAccount()));
            return ResultGenerator.genFailResult(MessageConstant.DELETE_FAILED_CN);
        }
    }

    /**
     * showdoc
     *
     * @param accountId 必选 string 账号ID
     * @return {"code":200,"message":"SUCCESS","data":{"accountId":"str","userName":"str","account":"str","password":"str","parkId":"str","roleId":"str","createTime":1585205164906,"updateTime":1585205164906}}
     * @catalog 账号管理
     * @title 详情
     * @description 账号详情
     * @method GET
     * @url IP/api/account/info/detail/{accountId}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/detail/{accountId}", method = RequestMethod.GET)
    public Result detail(@PathVariable @NotNull String accountId) {
        AccountInfo accountInfo = accountInfoService.findById(accountId);
        if (accountInfo == null) {
            return ResultGenerator.genFailResult(MessageConstant.DOES_NOT_EXIST_CN);
        } else {
            return ResultGenerator.genSuccessResult(accountInfo);
        }
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":{"serialVersionUID":0,"pageNum":0,"pageSize":0,"size":0,"orderBy":"str","startRow":0,"endRow":0,"total":0,"pages":0,"list":[{"accountId":"str","userName":"str","account":"str","password":"str","parkId":"str","roleId":"str","createTime":1585213076235,"updateTime":1585213076235}],"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":true,"hasNextPage":true,"navigatePages":0,"navigatepageNums":"int[]","navigateFirstPage":0,"navigateLastPage":0}}
     * @catalog 账号管理
     * @title 搜索
     * @description 账号搜索
     * @method POST
     * @url IP/api/account/info/search
     * @header token 必选 string token
     * @json_param {"condition":"str","roleId":"str","page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result search(@RequestBody AccountDTO accountDTO) {
        PageHelper.startPage(accountDTO.getPage(), accountDTO.getLimit());
        List<AccountInfo> list = accountInfoService.findByCondition(accountDTO);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":{"userName":"str","account":"str","roleId":"str","token":1585205546459}}
     * @catalog 账号管理
     * @title 登录
     * @description 通过账号密码登录平台，并进入管理平台
     * @method POST
     * @url IP/api/account/info/login
     * @header token 必选 string token
     * @json_param {"account":"str","password":"str"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody AccountInfo accountInfo) {
        String account = accountInfo.getAccount();
        String password = accountInfo.getPassword();
        logger.info("========login=======");
        logger.warn("try login account: {}, pswd is {}", accountInfo.getAccount(), accountInfo.getPassword());
        if (account == null || "".equals(account)) {
            return ResultGenerator.genFailResult(" 账号不能为空 ");
        } else if (password == null || "".equals(password)) {
            return ResultGenerator.genFailResult(" 密码不能为空");
        } else {
            accountInfo = accountInfoService.requestLogin(account, password);
            if (accountInfo != null) {
                Map<String, String> user = new HashMap<>();
                user.put("token", JwtUtil.getToken(account, accountInfo.getRoleId()));
                user.put("account_id", accountInfo.getAccountId());
                user.put("account", accountInfo.getAccount());
                user.put("parkId", accountInfo.getParkId());
                user.put("role", accountInfo.getRoleId());
                user.put("userName", accountInfo.getUserName());
                logRecordService.save(account, MODULE, "登录", String.format("账号：%s，已进入管理平台", accountInfo.getAccount()));
                return ResultGenerator.genSuccessResult(user);
            } else {
                return ResultGenerator.genFailResult(" 账号或密码错误");
            }
        }
    }

    /**
     * showdoc
     *
     * @param account 必选 string 账号
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 账号管理
     * @title 注销
     * @description 注销当前登录的账号，并退出管理平台
     * @method GET
     * @url IP/api/account/info/logout/{account}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/logout/{account}", method = RequestMethod.GET)
    public Result logout(@PathVariable String account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCondition(account);
        List<AccountInfo> list = accountInfoService.findByCondition(accountDTO);
        if (list != null && list.size() > 0) {
            logRecordService.save(account, MODULE, "注销", String.format("账号：%s，已退出管理平台", account));
            return ResultGenerator.genSuccessResult(account);
        } else {
            return ResultGenerator.genFailResult(MessageConstant.DOES_NOT_EXIST_CN);
        }

    }

    /**
     * showdoc
     *
     * @param account 必选 string 账号
     * @return {"code":200,"message":"SUCCESS","data":false}
     * @catalog 账号管理
     * @title 检测账号
     * @description 检测账号是否存在
     * @method GET
     * @url IP/api/account/info/check/{account}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark data true 表述重复，false 表示不重复
     * @number 1.0
     */
    @RequestMapping(value = "/check/{account}", method = RequestMethod.GET)
    public Result check(@PathVariable String account) {
        Condition condition = new Condition(AccountInfo.class);
        condition.createCriteria().andEqualTo("account", account);
        List<AccountInfo> accountInfoList = accountInfoService.findByCondition(condition);
        if (accountInfoList != null && accountInfoList.size() > 0) {
            return ResultGenerator.genSuccessResult(true);
        }
        return ResultGenerator.genSuccessResult(false);
    }
}
