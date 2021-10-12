package com.eservice.sinsimiot.web;

import com.eservice.sinsimiot.core.MessageConstant;
import com.eservice.sinsimiot.core.OperationConstant;
import com.eservice.sinsimiot.core.ResponseCode;
import com.eservice.sinsimiot.model.staff.StaffInfo;
import com.eservice.sinsimiot.service.LogRecordService;
import com.eservice.sinsimiot.util.EncryptUtil;
import com.eservice.sinsimiot.util.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
import com.eservice.sinsimiot.model.staff.StaffSearchDTO;
import com.eservice.sinsimiot.service.StaffInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.ArrayList;
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
@RequestMapping("/staff/info")
public class StaffInfoController {

    private final String MODULE = "员工管理";

    @Resource
    private StaffInfoService staffInfoService;
    @Resource
    private LogRecordService logRecordService;
    @Value("${aes.key}")
    private String AES_KEY;
    @Value("${aes.iv}")
    private String AES_IV;

    private Logger logger = Logger.getLogger(StaffInfo.class);
    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 员工管理
     * @title 新增
     * @description 新增员工
     * @method POST
     * @url IP/api/staff/info/add
     * @header token 必选 string token
     * @json_param {"name":"姓名","staffNumber":"工号","cardNumber":"卡号","faceIds":"上传照片后返回的id","contact":"联系方式","entryTime":"入职时间","birthday":"生日","remark":"备注","tagIds":"标签id","parkId":"园区id"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestHeader("token") String token, @RequestBody @NotNull StaffInfo staffInfo) {
        String account = JwtUtil.getAccount(token);
        //人员信息添加到数据库
        Result result = staffInfoService.saveStaff(staffInfo);
        if (result.getCode() == ResponseCode.OK) {
            //日志记录
            logRecordService.save(account, MODULE, OperationConstant.ADD_CN, String.format("姓名：%s，工号：%s，新增成功", staffInfo.getName(), staffInfo.getStaffNumber()));
            return result;
        } else {
            logRecordService.save(account, MODULE, OperationConstant.ADD_CN, String.format("姓名：%s，工号：%s，新增失败", staffInfo.getName(), staffInfo.getStaffNumber()));
            return result;
        }
    }

    /**
     * showdoc
     *
     * @param staffId 必选 string 员工id（staffId）
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 员工管理
     * @title 删除
     * @description 删除指定员工
     * @method DELETE
     * @url IP/api/staff/info/delete/{staffId}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @RequestMapping(value = "/delete/{staffId}", method = RequestMethod.DELETE)
    public Result delete(@RequestHeader("token") String token, @PathVariable String staffId) {
        String account = JwtUtil.getAccount(token);
        StaffInfo staffInfo = staffInfoService.findById(staffId);
        if (staffInfo == null) {
            return ResultGenerator.genSuccessResult(staffId, MessageConstant.DOES_NOT_EXIST_CN);
        }
        if (staffInfoService.deleteStaff(staffId)) {
            logRecordService.save(account, MODULE, OperationConstant.DELETE_CN, String.format("姓名：%s，工号：%s，删除成功", staffInfo.getName(), staffInfo.getStaffNumber()));
            return ResultGenerator.genSuccessResult(staffId);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.DELETE_CN, String.format("姓名：%s，工号：%s，删除失败", staffInfo.getName(), staffInfo.getStaffNumber()));
            return ResultGenerator.genFailResult(MessageConstant.DELETE_FAILED_CN);
        }
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 员工管理
     * @title 修改
     * @description 修改员工信息
     * @method PUT
     * @url IP/api/staff/info/update
     * @header token 必选 string token
     * @json_param {"name":"姓名","staffNumber":"工号","cardNumber":"卡号","faceIds":"上传照片后返回的id","contact":"联系方式","entryTime":"入职时间","birthday":"生日","remark":"备注","tagIds":"标签id","parkId":"园区id"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result update(@RequestHeader("token") String token, @RequestBody @NotNull StaffInfo staffInfo) {
        String account = JwtUtil.getAccount(token);
        StaffInfo oldStaffInfo = staffInfoService.findById(staffInfo.getStaffId());
        if (oldStaffInfo == null) {
            return ResultGenerator.genFailResult(MessageConstant.DOES_NOT_EXIST_CN);
        }
        Result result = staffInfoService.updateStaff(staffInfo, oldStaffInfo);
        if (result.getCode() == ResponseCode.OK) {
            logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("姓名：%s，工号：%s，修改成功", staffInfo.getName(), staffInfo.getStaffNumber()));
            return result;
        } else {
            logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("姓名：%s，工号：%s，修改失败", oldStaffInfo.getName(), oldStaffInfo.getStaffNumber()));
            return result;
        }
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Result modify(@RequestHeader("token") String token, @RequestBody @NotNull StaffInfo staffInfo) {
        String account = JwtUtil.getAccount(token);
        if (staffInfoService.update(staffInfo)) {
            logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("姓名：%s，手机号：%s，修改成功", staffInfo.getName(), staffInfo.getContact()));
            return ResultGenerator.genSuccessResult(staffInfo);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("姓名：%s，手机号：%s，修改失败", staffInfo.getName(), staffInfo.getContact()));
            return ResultGenerator.genFailResult(MessageConstant.UPDATE_FAILED_CN);
        }
    }

    /**
     * showdoc
     *
     * @param staffId 必选 string 员工id（staffId）
     * @return {"code":200,"message":"SUCCESS","data":{"staffId":"str","name":"str","email":"str","staffNumber":"str","parkId":"str","cardNumber":"str","faceIds":"str","contact":"str","entryTime":"str","birthday":"str","remark":"str","createTime":1585731811456,"updateTime":1585731811456,"tagIds":"str","meta":"str"}}
     * @catalog 员工管理
     * @title 详情
     * @description 员工详细信息
     * @method GET
     * @url IP/api/staff/info/detail/{id}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/detail/{staffId}", method = RequestMethod.GET)
    public Result detail(@PathVariable @NotNull String staffId) {
        StaffInfo staffInfo = staffInfoService.findById(staffId);
        if (staffInfo == null) {
            return ResultGenerator.genFailResult(MessageConstant.DOES_NOT_EXIST_CN);
        } else {
            return ResultGenerator.genSuccessResult(staffInfo);
        }
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":{"serialVersionUID":0,"pageNum":0,"pageSize":0,"size":0,"orderBy":"str","startRow":0,"endRow":0,"total":0,"pages":0,"list":[{"staffId":"str","name":"str","email":"str","parkId":"str","staffNumber":"str","cardNumber":"str","faceIds":"str","contact":"str","entryTime":"str","birthday":"str","remark":"str","createTime":1585731811456,"updateTime":1585731811456,"tagIds":"str","meta":"str"}],"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":true,"hasNextPage":true,"navigatePages":0,"navigatepageNums":"int[]","navigateFirstPage":0,"navigateLastPage":0}}
     * @catalog 员工管理
     * @title 搜索
     * @description 条件搜索
     * @method POST
     * @url IP/api/staff/info/search
     * @header token 必选 string token
     * @json_param {"condition":"str","tagIds":["str"],"areaIds":["str"],"type":"str","parkId":"str","page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result search(@RequestBody @NotNull StaffSearchDTO staffSearchDTO) {
        PageHelper.startPage(staffSearchDTO.getPage(), staffSearchDTO.getLimit());
        String account = staffSearchDTO.getAccount();
        logger.info("staff search get account:" + account);
        List<StaffInfo> list = staffInfoService.findByCondition(staffSearchDTO);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":[{"staffId":"str","name":"str","email":"str","parkId":"str","staffNumber":"str","cardNumber":"str","faceIds":"str","contact":"str","entryTime":"str","birthday":"str","remark":"str","createTime":1585731811456,"updateTime":1585731811456,"tagIds":"str","meta":"str"}]}
     * @catalog 员工管理
     * @title 全量员工
     * @description 全量员工
     * @method GET
     * @url IP/api/staff/info/list
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list() {
        List<StaffInfo> staffInfos = staffInfoService.findAll();
        return ResultGenerator.genSuccessResult(staffInfos);
    }


    /**
     * showdoc
     *
     * @param multipartFile 必选 MultipartFile 上传控件的name字段对应的名称
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 员工管理
     * @title 导入
     * @description 导入员工信息
     * @method DELETE
     * @url IP/api/staff/info/import
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功， 602:Excel文件获取失败【前端可以弹出"message"字段】,603:部分人导入失败【触发下载返回的文件路径】,
     * @number 1.0
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public Result importRecord(@RequestParam MultipartFile multipartFile) {
        File file = staffInfoService.unZipFile(multipartFile, null);
        if (file == null) {
            return ResultGenerator.genExcelReadFailResult("Excel文件获取失败");
        } else {
            String fileName = staffInfoService.readStaffData(file);
            if (fileName == null) {
                return ResultGenerator.genSuccessResult();
            }
            return ResultGenerator.genExcelImportFailResult(fileName);
        }
    }

    /**
     * showdoc
     *
     * @param areaId 必选 string 区域id
     * @return {"code":200,"message":"SUCCESS","data":0}
     * @catalog 员工管理
     * @title 统计区域人数
     * @description 根据区域id统计，能通行该区域的总人数
     * @method GET
     * @url IP/api/staff/info/area/{areaId}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/area/{areaId}", method = RequestMethod.GET)
    public Result countByArea(@PathVariable String areaId) {
        StaffSearchDTO staffSearchDTO = new StaffSearchDTO();
        if (areaId != null && !areaId.isEmpty()) {
            ArrayList<String> areaIds = new ArrayList<>();
            areaIds.add(areaId);
            staffSearchDTO.setAreaIds(areaIds);
        }
        List<StaffInfo> list = staffInfoService.findByCondition(staffSearchDTO);
        return ResultGenerator.genSuccessResult(list == null ? 0 : list.size());
    }

    /**
     * showdoc
     *
     * @param tagId 必选 string 区域id
     * @return {"code":200,"message":"SUCCESS","data":0}
     * @catalog 员工管理
     * @title 统计标签人数
     * @description 根据标签Id，统计拥有该标签的总人数
     * @method GET
     * @url IP/api/staff/info/tag/{tagId}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/tag/{tagId}", method = RequestMethod.GET)
    public Result countByTag(@PathVariable String tagId) {
        StaffSearchDTO staffSearchDTO = new StaffSearchDTO();
        if (tagId != null && !tagId.isEmpty()) {
            ArrayList<String> tagIds = new ArrayList<>();
            tagIds.add(tagId);
            staffSearchDTO.setTagIds(tagIds);
        }
        // 修改为sql统计
        Integer count = staffInfoService.staffInfoByTagIdCount(staffSearchDTO);
        return ResultGenerator.genSuccessResult(count);
    }

    /**
     * showdoc
     *
     * @param number 必选 String 员工卡号
     * @return {"code":200,"message":"SUCCESS","data":false}
     * @catalog 员工管理
     * @title 检查卡号
     * @description 判断员工卡号是否重复
     * @method GET
     * @url IP/api/staff/info/check/card/{number}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark data true 表述重复，false 表示不重复
     * @number 1.0
     */
    @RequestMapping(value = "/check/card/{number}", method = RequestMethod.GET)
    public Result checkCardNumber(@PathVariable String number) {
        return ResultGenerator.genSuccessResult(staffInfoService.checkCardNumber(number));
    }

    /**
     * showdoc
     *
     * @param number 必选 String 员工工号
     * @return {"code":200,"message":"SUCCESS","data":false}
     * @catalog 员工管理
     * @title 检查工号
     * @description 判断员工工号是否重复
     * @method GET
     * @url IP/api/staff/info/check/staff/{number}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark data true 表述重复，false 表示不重复
     * @number 1.0
     */
    @RequestMapping(value = "/check/staff/{number}", method = RequestMethod.GET)
    public Result checkStaffNumber(@PathVariable String number) {
        return ResultGenerator.genSuccessResult(staffInfoService.checkStaffNumber(number));
    }


    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 员工管理
     * @title 导出
     * @description 员工信息导出
     * @method POST
     * @url IP/api/staff/info/export
     * @header token 必选 string token
     * @json_param {"condition":"str","tagIds":["str"],"areaIds":["str"],"parkId":"str","type":"str","page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public Result export(@RequestHeader("token") String token, @RequestBody @NotNull StaffSearchDTO staffSearchDTO) {
        String account = JwtUtil.getAccount(token);
        String name = staffInfoService.exportRecord(staffSearchDTO);
        if (name != null) {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, String.format("员工信息导出成功"));
            return ResultGenerator.genSuccessResult(name);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, String.format("员工信息导出失败"));
            return ResultGenerator.genFailResult(MessageConstant.EXPORT_FAILED_CN);
        }
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 员工管理
     * @title 导出
     * @description 员工信息导出
     * @method POST
     * @url IP/api/staff/info/export/zip
     * @header token 必选 string token
     * @json_param {"condition":"str","tagIds":["str"],"areaIds":["str"],"type":"str","parkId":"str","page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @RequestMapping(value = "/export/zip", method = RequestMethod.POST)
    public Result exportZip(@RequestHeader("token") String token, @RequestBody @NotNull StaffSearchDTO staffSearchDTO) {
        String account = JwtUtil.getAccount(token);
        String name = staffInfoService.exportRecordZip(staffSearchDTO);
        if (name != null) {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, String.format("员工信息导出成功"));
            return ResultGenerator.genSuccessResult(name);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, String.format("员工信息导出失败"));
            return ResultGenerator.genFailResult(MessageConstant.EXPORT_FAILED_CN);
        }
    }


    /**
     * showdoc
     *
     * @param staffId 必选 string 员工id（staffId）
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 员工管理
     * @title 设置员工离职
     * @description 设置员工离职
     * @method POST
     * @url IP/api/staff/info/setting/{staffId}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @RequestMapping(value = "/setting/{staffId}", method = RequestMethod.POST)
    public Result setting(@RequestHeader String token, @PathVariable String staffId) {
        String account = JwtUtil.getAccount(token);
        StaffInfo staffInfo = staffInfoService.findById(staffId);
        if (staffInfo != null) {
            Boolean result = staffInfoService.settingStaff(staffInfo);
            if (result) {
                logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("姓名：%s，工号：%s 设置离职成功", staffInfo.getName(), staffInfo.getStaffNumber()));
                return ResultGenerator.genSuccessResult(result);
            } else {
                logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("姓名：%s，工号：%s，设置离职失败", staffInfo.getName(), staffInfo.getStaffNumber()));
            }
        } else {
            logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("姓名：%s，工号：%s，不存在", staffInfo.getName(), staffInfo.getStaffNumber()));
            return ResultGenerator.genFailResult(MessageConstant.DOES_NOT_EXIST_CN);
        }
        return ResultGenerator.genFailResult(MessageConstant.TRY_AGAIN_LATER_CN);
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody StaffSearchDTO staffSearchDTO) {
        if (!StringUtils.isEmpty(staffSearchDTO.getAccount()) && !StringUtils.isEmpty(staffSearchDTO.getPassword())) {
            String account = staffSearchDTO.getAccount();
            String password = EncryptUtil.decrypt(staffSearchDTO.getPassword(), AES_KEY, AES_IV);
            if (account == null || "".equals(account)) {
                return ResultGenerator.genFailResult(" 账号不能为空 ");
            } else if (password == null || "".equals(password)) {
                return ResultGenerator.genFailResult(" 密码不能为空");
            } else {
                StaffInfo staffInfo = staffInfoService.login(account, password);
                if (staffInfo != null) {
                    if (staffInfo != null) {
                        Map<String, Object> user = new HashMap<>();
                        user.put("token", JwtUtil.getToken(account, staffInfo.getStaffId()));
                        user.put("user", staffInfo);
                        user.put("parkId", staffInfo.getParkId());
                        user.put("staffNum", staffInfo.getStaffNumber());
                        return ResultGenerator.genSuccessResult(user);
                    } else {
                        return ResultGenerator.genFailResult(" 账号或密码错误");
                    }
                }
            }
            return ResultGenerator.genFailResult("登录失败");
        } else {
            return ResultGenerator.genFailResult("参数不能为空");
        }

    }


}
