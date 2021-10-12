package com.eservice.sinsimiot.web;

import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
import com.eservice.sinsimiot.core.MessageConstant;
import com.eservice.sinsimiot.core.OperationConstant;
import com.eservice.sinsimiot.model.account.AccountDTO;
import com.eservice.sinsimiot.model.role.RoleInfo;
import com.eservice.sinsimiot.service.AccountInfoService;
import com.eservice.sinsimiot.service.LogRecordService;
import com.eservice.sinsimiot.service.RoleInfoService;
import com.eservice.sinsimiot.util.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Code Generator
 * @date 2020/03/06.
 */
@RestController
@RequestMapping("/role/info")
public class RoleInfController {

    private final String MODULE = "角色管理";

    @Resource
    private RoleInfoService roleInfoService;
    @Resource
    private AccountInfoService accountInfoService;
    @Resource
    private LogRecordService logRecordService;

    private Logger logger = Logger.getLogger(RoleInfController.class);

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 角色管理
     * @title 新增
     * @description 新增角色
     * @method POST
     * @url IP/api/role/info/add
     * @header token 必选 string token
     * @json_param {"roleName":"角色名称","remark":"权限描述","scope":"权限"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestHeader("token") String token, @RequestBody @NotNull RoleInfo roleInfo) {
        String account = JwtUtil.getAccount(token);
        if (roleInfoService.save(roleInfo)) {
            logRecordService.save(account, MODULE, OperationConstant.ADD_CN, String.format("角色：%s，新增成功", roleInfo.getRoleName()));
            return ResultGenerator.genSuccessResult(roleInfo);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.ADD_CN, String.format("角色：%s，新增失败", roleInfo.getRoleName()));
            return ResultGenerator.genFailResult(MessageConstant.ADD_FAILED_CN);
        }
    }

    /**
     * showdoc
     *
     * @param roleId 必选 string 角色ID
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 角色管理
     * @title 删除
     * @description 删除角色
     * @method DELETE
     * @url IP/api/role/info/delete/{roleId}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/delete/{roleId}", method = RequestMethod.DELETE)
    public Result delete(@RequestHeader("token") String token, @PathVariable @NotNull String roleId) {
        String account = JwtUtil.getAccount(token);
        RoleInfo roleInfo = roleInfoService.findById(roleId);
        if (roleInfo == null) {
            return ResultGenerator.genSuccessResult(roleId, MessageConstant.DOES_NOT_EXIST_CN);
        }
        if (roleInfoService.deleteById(roleId)) {
            logRecordService.save(account, MODULE, OperationConstant.DELETE_CN, String.format("角色：%s，删除成功", roleInfo.getRoleName()));
            accountInfoService.updateAccount(roleId);
            return ResultGenerator.genSuccessResult(roleId);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.DELETE_CN, String.format("角色：%s，删除失败", roleInfo.getRoleName()));
            return ResultGenerator.genFailResult(MessageConstant.DELETE_FAILED_CN);
        }
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 角色管理
     * @title 修改
     * @description 修改角色
     * @method PUT
     * @url IP/api/role/info/update
     * @header token 必选 string token
     * @json_param {"roleId":"str","roleName":"str","remark":"str","scope":"str"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result update(@RequestHeader("token") String token, @RequestBody @NotNull RoleInfo roleInfo) {
        String account = JwtUtil.getAccount(token);
        RoleInfo oldRoleInfo = roleInfoService.findById(roleInfo.getRoleId());
        if (roleInfo == null) {
            return ResultGenerator.genFailResult(MessageConstant.DOES_NOT_EXIST_CN);
        }
        if (roleInfoService.update(roleInfo)) {
            logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("角色：%s，修改成功", roleInfo.getRoleName()));
            //只为数据回显正常
            roleInfo = roleInfoService.findById(roleInfo.getRoleId());
            return ResultGenerator.genSuccessResult(roleInfo);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("角色：%s，修改失败", oldRoleInfo.getRoleName()));
            return ResultGenerator.genFailResult(MessageConstant.UPDATE_FAILED_CN);
        }
    }

    /**
     * showdoc
     *
     * @param roleId 必选 string 角色ID
     * @return {"code":200,"message":"SUCCESS","data":{"roleId":"str","roleName":"str","remark":"str","createTime":1585206399731,"updateTime":1585206399731,"scope":"str"}}
     * @catalog 角色管理
     * @title 详情
     * @description 角色详情
     * @method GET
     * @url IP/api/role/info/detail/{roleId}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/detail/{roleId}", method = RequestMethod.GET)
    public Result detail(@PathVariable @NotNull String roleId) {
        RoleInfo roleInfo = roleInfoService.findById(roleId);
        return ResultGenerator.genSuccessResult(roleInfo);
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":{"serialVersionUID":0,"pageNum":0,"pageSize":0,"size":0,"orderBy":"str","startRow":0,"endRow":0,"total":0,"pages":0,"list":[{"roleId":"str","roleName":"str","remark":"str","createTime":1585206399731,"updateTime":1585206399731,"scope":"str"}],"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":true,"hasNextPage":true,"navigatePages":0,"navigatepageNums":"int[]","navigateFirstPage":0,"navigateLastPage":0}}
     * @catalog 角色管理
     * @title 搜索
     * @description 角色搜索
     * @method POST
     * @url IP/api/role/info/search
     * @header token 必选 string token
     * @json_param {"condition":"str","page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result search(@RequestBody @NotNull AccountDTO accountDTO) {
        PageHelper.startPage(accountDTO.getPage(), accountDTO.getLimit());
        List<RoleInfo> list = roleInfoService.findByCondition(accountDTO.getCondition());
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":[{"roleId":"str","roleName":"str","remark":"str","createTime":1585206399731,"updateTime":1585206399731,"scope":"str"}]}
     * @catalog 角色管理
     * @title 全量角色列表
     * @description 角色列表
     * @method GET
     * @url IP/api/role/info/list
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list() {
        logger.info("selectIotMachine");
        List<RoleInfo> list = roleInfoService.findAll();
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * showdoc
     *
     * @param name 必选 string 账号名称
     * @return {"code":200,"message":"SUCCESS","data":false}
     * @catalog 角色管理
     * @title 检测角色名称
     * @description 检测角色名称是否重复
     * @method GET
     * @url IP/api/role/info/check
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark data true 表述重复，false 表示不重复
     * @number 1.0
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public Result check(@RequestParam String name) {
        Condition condition = new Condition(RoleInfo.class);
        condition.createCriteria().andEqualTo("roleName", name);
        List<RoleInfo> roleInfoList = roleInfoService.findByCondition(condition);
        if (roleInfoList != null && roleInfoList.size() > 0) {
            return ResultGenerator.genSuccessResult(true);
        }
        return ResultGenerator.genSuccessResult(false);
    }
}
