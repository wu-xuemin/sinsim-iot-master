package com.eservice.sinsimiot.web;

import com.eservice.sinsimiot.common.SearchDTO;
import com.eservice.sinsimiot.service.AttendanceConfigService;
import com.eservice.sinsimiot.util.JwtUtil;
import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
import com.eservice.sinsimiot.core.MessageConstant;
import com.eservice.sinsimiot.core.OperationConstant;
import com.eservice.sinsimiot.model.attendance_config.AttendanceConfig;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.eservice.sinsimiot.service.LogRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Mr.xie
 * @date 2021/05/12.
 */
@RestController
@RequestMapping("/attendance/config")
public class AttendanceConfigController {

    private final String MODULE = "考勤配置";

    @Resource
    private AttendanceConfigService attendanceConfigService;
    @Resource
    private LogRecordService logRecordService;

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 考勤时间配置
     * @title 新增
     * @description 新增考勤时间段配置
     * @method POST
     * @url IP/api/attendance/config/add
     * @header token 必选 string token
     * @json_param {"name":"str","startTime":"str","endTime":"str"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @PostMapping("/add")
    public Result add(@RequestHeader String token, @RequestBody @NotNull AttendanceConfig attendanceConfig) {
        String account = JwtUtil.getAccount(token);
        if (attendanceConfigService.save(attendanceConfig)) {
            logRecordService.save(account, MODULE, OperationConstant.ADD_CN, String.format("规则：%s，开始时间：%s，结束时间：%s,新增成功", attendanceConfig.getName(), attendanceConfig.getStartTime(), attendanceConfig.getEndTime()));
            return ResultGenerator.genSuccessResult(attendanceConfig);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.ADD_CN, String.format("规则：%s，开始时间：%s，结束时间：%s,新增失败", attendanceConfig.getName(), attendanceConfig.getStartTime(), attendanceConfig.getEndTime()));
            return ResultGenerator.genFailResult(MessageConstant.ADD_FAILED_CN);
        }
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 考勤时间配置
     * @title 删除
     * @description 删除考勤时间段配置
     * @method DELETE
     * @url IP/api/attendance/config/delete/{id}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@RequestHeader String token, @PathVariable String id) {
        String account = JwtUtil.getAccount(token);
        AttendanceConfig attendanceConfig = attendanceConfigService.findById(id);
        if (attendanceConfig == null) {
            return ResultGenerator.genSuccessResult(MessageConstant.DOES_NOT_EXIST_CN);
        }
        if (attendanceConfigService.deleteById(id)) {
            logRecordService.save(account, MODULE, OperationConstant.DELETE_CN, String.format("规则：%s，删除成功", attendanceConfig.getName()));
            return ResultGenerator.genSuccessResult();
        } else {
            logRecordService.save(account, MODULE, OperationConstant.DELETE_CN, String.format("规则：%s，删除失败", attendanceConfig.getName()));
            return ResultGenerator.genFailResult(MessageConstant.DELETE_FAILED_CN);
        }
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 考勤时间配置
     * @title 修改
     * @description 修改考勤时间段配置
     * @method PUT
     * @url IP/api/attendance/config/update
     * @header token 必选 string token
     * @json_param {"id":"str","name":"str","startTime":"str","endTime":"str"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @PutMapping("/update")
    public Result update(@RequestHeader String token, @RequestBody @NotNull AttendanceConfig attendanceConfig) {
        String account = JwtUtil.getAccount(token);
        if (attendanceConfigService.update(attendanceConfig)) {
            logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("规则：%s，开始时间：%s，结束时间：%s,修改成功", attendanceConfig.getName(), attendanceConfig.getStartTime(), attendanceConfig.getEndTime()));
            return ResultGenerator.genSuccessResult(attendanceConfig);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("规则：%s，开始时间：%s，结束时间：%s,修改失败", attendanceConfig.getName(), attendanceConfig.getStartTime(), attendanceConfig.getEndTime()));
            return ResultGenerator.genFailResult(MessageConstant.UPDATE_FAILED_CN);
        }
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam @NotNull String id) {
        AttendanceConfig attendanceConfig = attendanceConfigService.findById(id);
        return ResultGenerator.genSuccessResult(attendanceConfig);
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":{"serialVersionUID":0,"pageNum":0,"pageSize":0,"size":0,"orderBy":"str","startRow":0,"endRow":0,"total":0,"pages":0,"list":[{"id": "String","name": "String","startTime": "String","endTime": "String","createTime": 0,"updateTime": 0}],"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":true,"hasNextPage":true,"navigatePages":0,"navigatepageNums":"int[]","navigateFirstPage":0,"navigateLastPage":0}}
     * @catalog 考勤时间配置
     * @title 详情
     * @description 考勤配置详情
     * @method POST
     * @url IP/api/attendance/config/list
     * @header token 必选 string token
     * @json_param {"page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @PostMapping("/list")
    public Result list(@RequestBody SearchDTO searchDTO) {
        PageHelper.startPage(searchDTO.getPage(), searchDTO.getLimit());
        List<AttendanceConfig> list = attendanceConfigService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
