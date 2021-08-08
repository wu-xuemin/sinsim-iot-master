package com.eservice.sinsimiot.web;

import com.eservice.sinsimiot.core.MessageConstant;
import com.eservice.sinsimiot.core.OperationConstant;
import com.eservice.sinsimiot.core.ResponseCode;
import com.eservice.sinsimiot.model.departing_staff.DepartingStaff;
import com.eservice.sinsimiot.model.staff.StaffSearchDTO;
import com.eservice.sinsimiot.service.DepartingStaffService;
import com.eservice.sinsimiot.service.LogRecordService;
import com.eservice.sinsimiot.util.JwtUtil;
import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Mr.xie
 * @date 2021/06/07.
 */
@RestController
@RequestMapping("/departing/staff")
public class DepartingStaffController {

    private final String MODULE = "离职员工管理";

    @Resource
    private DepartingStaffService departingStaffService;
    @Resource
    private LogRecordService logRecordService;

    @PostMapping("/add")
    public Result add(@RequestBody @NotNull DepartingStaff departingStaff) {
        departingStaffService.save(departingStaff);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * showdoc
     *
     * @param staffId 必选 string 员工id（staffId）
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 离职员工管理
     * @title 删除
     * @description 删除指定员工
     * @method DELETE
     * @url IP/api/departing/staff/delete/{staffId}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @DeleteMapping("/delete/{staffId}")
    public Result delete(@RequestHeader String token, @PathVariable String staffId) {
        String account = JwtUtil.getAccount(token);
        DepartingStaff departingStaff = departingStaffService.findById(staffId);
        if (departingStaff != null) {
            if (departingStaffService.deleteById(staffId)) {
                logRecordService.save(account, MODULE, OperationConstant.DELETE_CN, String.format("姓名：%s，工号：%s，删除成功", departingStaff.getName(), departingStaff.getStaffNumber()));
                return ResultGenerator.genSuccessResult(staffId);
            } else {
                logRecordService.save(account, MODULE, OperationConstant.DELETE_CN, String.format("姓名：%s，工号：%s，删除失败", departingStaff.getName(), departingStaff.getStaffNumber()));
                return ResultGenerator.genFailResult(MessageConstant.DELETE_FAILED_CN);
            }
        } else {
            logRecordService.save(account, MODULE, OperationConstant.DELETE_CN, String.format("姓名：%s，工号：%s，不存在或已删除", departingStaff.getName(), departingStaff.getStaffNumber()));
            return ResultGenerator.genSuccessResult(MessageConstant.DOES_NOT_EXIST_CN);
        }

    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 离职员工管理
     * @title 修改
     * @description 修改员工信息
     * @method PUT
     * @url IP/api/departing/staff/update
     * @header token 必选 string token
     * @json_param {"name":"姓名","staffNumber":"工号","cardNumber":"卡号","faceIds":"上传照片后返回的id","contact":"联系方式","entryTime":"入职时间","birthday":"生日","remark":"备注","tagIds":"标签id","parkId":"园区id"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @PutMapping("/update")
    public Result update(@RequestHeader String token, @RequestBody @NotNull DepartingStaff departingStaff) {
        String account = JwtUtil.getAccount(token);
        if (departingStaffService.update(departingStaff)) {
            logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("姓名：%s，工号：%s，修改成功", departingStaff.getName(), departingStaff.getStaffNumber()));
            return ResultGenerator.genSuccessResult(departingStaff);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("姓名：%s，工号：%s，修改失败", departingStaff.getName(), departingStaff.getStaffNumber()));
            return ResultGenerator.genFailResult(MessageConstant.UPDATE_FAILED_CN);
        }
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam @NotNull Integer id) {
        DepartingStaff departingStaff = departingStaffService.findById(id);
        return ResultGenerator.genSuccessResult(departingStaff);
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":{"serialVersionUID":0,"pageNum":0,"pageSize":0,"size":0,"orderBy":"str","startRow":0,"endRow":0,"total":0,"pages":0,"list":[{"staffId":"str","name":"str","email":"str","parkId":"str","staffNumber":"str","cardNumber":"str","faceIds":"str","contact":"str","entryTime":"str","birthday":"str","remark":"str","createTime":1585731811456,"updateTime":1585731811456,"tagIds":"str","meta":"str"}],"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":true,"hasNextPage":true,"navigatePages":0,"navigatepageNums":"int[]","navigateFirstPage":0,"navigateLastPage":0}}
     * @catalog 离职员工管理
     * @title 搜索
     * @description 条件搜索
     * @method POST
     * @url IP/api/departing/staff/list
     * @header token 必选 string token
     * @json_param {"condition":"str","tagIds":["str"],"areaIds":["str"],"type":"str","parkId":"str","page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @PostMapping("/list")
    public Result list(@RequestBody StaffSearchDTO staffSearchDTO) {
        PageHelper.startPage(staffSearchDTO.getPage(), staffSearchDTO.getLimit());
        List<DepartingStaff> list = departingStaffService.findByKey(staffSearchDTO);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


    /**
     * showdoc
     *
     * @param staffId 必选 string 员工id（staffId）
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 离职员工管理
     * @title 设置员工在职
     * @description 设置员工在职
     * @method POST
     * @url IP/api/departing/staff/setting/{staffId}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @PostMapping("/setting/{staffId}")
    public Result setting(@RequestHeader String token, @PathVariable String staffId) {
        String account = JwtUtil.getAccount(token);
        DepartingStaff departingStaff = departingStaffService.findById(staffId);
        if (departingStaff != null) {
            Result result = departingStaffService.settingStaff(departingStaff);
            if (result.getCode() == ResponseCode.OK) {
                logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("姓名：%s，工号：%s，修改为’在职‘成功", departingStaff.getName(), departingStaff.getStaffNumber()));
                return result;
            } else {
                logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("姓名：%s，工号：%s，修改为’在职‘失败", departingStaff.getName(), departingStaff.getStaffNumber()));
                return result;
            }
        } else {
            return ResultGenerator.genFailResult(MessageConstant.DOES_NOT_EXIST_CN);
        }
    }
}
