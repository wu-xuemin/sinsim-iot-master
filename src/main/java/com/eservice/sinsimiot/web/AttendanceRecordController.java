package com.eservice.sinsimiot.web;

import com.eservice.sinsimiot.core.MessageConstant;
import com.eservice.sinsimiot.core.OperationConstant;
import com.eservice.sinsimiot.model.attendance_record.AttendanceRecord;
import com.eservice.sinsimiot.service.LogRecordService;
import com.eservice.sinsimiot.service.park.AccessService;
import com.eservice.sinsimiot.util.JwtUtil;
import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
import com.eservice.sinsimiot.model.attendance_record.AttendanceDTO;
import com.eservice.sinsimiot.model.attendance_record.AttendanceRecordDTO;
import com.eservice.sinsimiot.service.AttendanceRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.eservice.sinsimiot.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Mr.xie
 * @date 2021/05/08.
 */
@RestController
@RequestMapping("/attendance/record")
@Slf4j
public class AttendanceRecordController {

    private final String MODULE = "考勤管理";
    @Resource
    private AttendanceRecordService attendanceRecordService;
    @Resource
    private LogRecordService logRecordService;
    @Resource
    private AccessService accessService;


    @PostMapping("/detail")
    public Result detail(@RequestParam @NotNull String id) {
        AttendanceRecord attendanceRecord = attendanceRecordService.findById(id);
        return ResultGenerator.genSuccessResult(attendanceRecord);
    }


    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":{"serialVersionUID":0,"pageNum":0,"pageSize":0,"size":0,"orderBy":"str","startRow":0,"endRow":0,"total":0,"pages":0,"list":[{"recordId": "97ee4580-c404-4f3a-94d9-eeb17e3c774f","staffName": "谢宇涵","staffId": "6096489b040ad7000124cc49","staffNumber": "hkxyh123","staffParkName": "粤园","staffParkId": "280bb99d-da85-4b28-8cb1-dff2558c100c","staffTagName": "监管单位","staffTagId": "121c0703-283a-4d73-8ed5-7e9186ed6fc7","signDeviceName": "考勤测试","signOutDeviceName": "考勤测试","signTime": "2021-05-10 09:53:25","signOutTime": "2021-05-10 10:10:30","createTime": 1620611617,"updateTime": 1620612641}],"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":true,"hasNextPage":true,"navigatePages":0,"navigatepageNums":"int[]","navigateFirstPage":0,"navigateLastPage":0}}
     * @catalog 考勤管理
     * @title 详情
     * @description 考勤详情
     * @method POST
     * @url IP/api/attendance/record/list
     * @header token 必选 string token
     * @json_param {"condition":"str","signTime":"str","signOutTime":"str","parkId":"str","staffNum":"str","tagIds":[],"page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @PostMapping("/list")
    public Result list(@RequestBody AttendanceDTO attendanceDTO) {
        PageHelper.startPage(attendanceDTO.getPage(), attendanceDTO.getLimit());
        List<AttendanceRecord> list = attendanceRecordService.searchAttendance(attendanceDTO);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/search")
    public Result search(@RequestBody AttendanceDTO attendanceDTO) {
        PageHelper.startPage(attendanceDTO.getPage(), attendanceDTO.getLimit());
        List<AttendanceRecord> list = attendanceRecordService.appSearchAttendance(attendanceDTO);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":"str"}
     * @catalog 考勤管理
     * @title 导出
     * @description 考勤信息导出
     * @method POST
     * @url IP/api/attendance/record/export
     * @header token 必选 string token
     * @json_param {"condition":"str","signTime":"str","signOutTime":"str","parkId":"str","staffNum":"str","tagIds":[],"page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，data为下载路径，其他为失败
     * @number 1.0
     */
    @PostMapping("/export")
    public Result export(@RequestHeader String token, @RequestBody AttendanceDTO attendanceDTO) {
        String account = JwtUtil.getAccount(token);
        String url = attendanceRecordService.export(attendanceDTO);
        if (url != null) {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, String.format("考勤信息导出成功"));
            return ResultGenerator.genSuccessResult(url);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, String.format("考勤信息导出失败"));
            return ResultGenerator.genFailResult(MessageConstant.EXPORT_FAILED_CN);
        }
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":{"serialVersionUID":0,"pageNum":0,"pageSize":0,"size":0,"orderBy":"str","startRow":0,"endRow":0,"total":0,"pages":0,"list":[{"recordId": "241de41e-bb24-4326-9d20-3cb69cfefd92","staffName": "111","staffId": "60966214040ad7000124d3df","staffNumber": "333","staffParkName": "燕园","staffParkId": "da4bdd4d-8915-40e7-b131-8104e322e498","staffTagName": "设计单位","staffTagId": "e365c873-b066-4909-b8fb-0991c10bc308","signDeviceName": null,"signOutDeviceName": null,"signTime": null,"signOutTime": null,"status": "normal","createTime": null,"updateTime": null,"normal": 1,"abnormal": 0,"averageTime": 7.2,"countTime": 7.2,"valid":true}],"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":true,"hasNextPage":true,"navigatePages":0,"navigatepageNums":"int[]","navigateFirstPage":0,"navigateLastPage":0}}
     * @catalog 考勤管理
     * @title 统计
     * @description 考勤统计
     * @method POST
     * @url IP/api/attendance/record/countAttendance
     * @header token 必选 string token
     * @json_param {"condition":"str","signTime":"str","signOutTime":"str","parkId":"str","staffNum":"str","tagIds":[],"page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @PostMapping("/countAttendance")
    public Result countAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        List<AttendanceRecordDTO> recordDTOS = attendanceRecordService.countAttendance(attendanceDTO);
        if (recordDTOS != null) {
            PageInfo pageInfo = new PageInfo(recordDTOS);
            pageInfo.setList(Util.pagingQuery(attendanceDTO.getPage(), attendanceDTO.getLimit(), recordDTOS));
            return ResultGenerator.genSuccessResult(pageInfo);
        }
        return ResultGenerator.genFailResult("no data");
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":"str"}
     * @catalog 考勤管理
     * @title 考勤统计导出
     * @description 考勤统计信息导出
     * @method POST
     * @url IP/api/attendance/record/export/countAttendance
     * @header token 必选 string token
     * @json_param {"condition":"str","signTime":"str","signOutTime":"str","parkId":"str","staffNum":"str","tagIds":[],"page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，data为下载路径，其他为失败
     * @number 1.0
     */
    @PostMapping("/export/countAttendance")
    public Result exportCountAttendance(@RequestHeader String token, @RequestBody AttendanceDTO attendanceDTO) {
        String account = JwtUtil.getAccount(token);
        List<AttendanceRecordDTO> recordDTOS = attendanceRecordService.countAttendance(attendanceDTO);
        if (recordDTOS != null && recordDTOS.size() > 0) {
            String fileName = attendanceRecordService.exportCounAttendance(recordDTOS);
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, String.format("考勤统计信息导出成功"));
            return ResultGenerator.genSuccessResult(fileName);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, String.format("考勤统计信息导出失败"));
            return ResultGenerator.genFailResult(MessageConstant.EXPORT_FAILED_CN);
        }
    }


    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":{"serialVersionUID":0,"pageNum":0,"pageSize":0,"size":0,"orderBy":"str","startRow":0,"endRow":0,"total":0,"pages":0,"list":[{"recordId": "241de41e-bb24-4326-9d20-3cb69cfefd92","staffName": "111","staffId": "60966214040ad7000124d3df","staffNumber": "333","staffParkName": "燕园","staffParkId": "da4bdd4d-8915-40e7-b131-8104e322e498","staffTagName": "设计单位","staffTagId": "e365c873-b066-4909-b8fb-0991c10bc308","signDeviceName": null,"signOutDeviceName": null,"signTime": null,"signOutTime": null,"status": "normal","createTime": null,"updateTime": null,"day":"2021-05-01","normal": 1,"abnormal": 0,"averageTime": 7.2,"countTime": 7.2}],"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":true,"hasNextPage":true,"navigatePages":0,"navigatepageNums":"int[]","navigateFirstPage":0,"navigateLastPage":0}}
     * @catalog 考勤管理
     * @title 考勤详情
     * @description 考勤统计详情
     * @method POST
     * @url IP/api/attendance/record/countAttendance/detail
     * @header token 必选 string token
     * @json_param {"condition":"str","signTime":"str","signOutTime":"str","parkId":"str","staffNum":"str","tagIds":[],"page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @PostMapping("/countAttendance/detail")
    public Result countAttendanceDetail(@RequestBody AttendanceDTO attendanceDTO) {
        List<AttendanceRecordDTO> attendanceRecordDTOS = attendanceRecordService.fetchAttendanceRecordDTODetail(attendanceDTO);
        PageInfo pageInfo = new PageInfo(attendanceRecordDTOS);
        pageInfo.setList(Util.pagingQuery(attendanceDTO.getPage(), attendanceDTO.getLimit(), attendanceRecordDTOS));
        return ResultGenerator.genSuccessResult(pageInfo);
    }


    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":"str"}
     * @catalog 考勤管理
     * @title 考勤详情导出
     * @description 考勤详情导出
     * @method POST
     * @url IP/api/attendance/record/export/countAttendance/detail
     * @header token 必选 string token
     * @json_param {"condition":"str","signTime":"str","signOutTime":"str","parkId":"str","staffNum":"str","tagIds":[],"page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，data为下载路径，其他为失败
     * @number 1.0
     */
    @PostMapping("/export/countAttendance/detail")
    public Result exportAttendanceRecordDTODetail(@RequestHeader String token, @RequestBody AttendanceDTO attendanceDTO) {
        String account = JwtUtil.getAccount(token);
        String fileName = attendanceRecordService.exportAttendanceRecordDTODetail(attendanceDTO);
        if (fileName != null) {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, String.format("考勤详情信息导出成功"));
            return ResultGenerator.genSuccessResult(fileName);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, String.format("考勤详情信息导出失败"));
            return ResultGenerator.genFailResult(MessageConstant.EXPORT_FAILED_CN);
        }
    }


    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":"str"}
     * @catalog 考勤管理
     * @title 补推考勤
     * @description 补推考勤数据
     * @method POST
     * @url IP/api/attendance/record/push/record
     * @json_param {"startTime":"2021-06-15 00:00:00","endTime":"2021-06-18 23:59:59","park":"豫园","staffNum":"str"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，data为下载路径，其他为失败
     * @number 1.0
     */
//    @PostMapping("/push/record")
//    public Result pushRecord(@RequestBody PushRecordDTO pushRecordDTO) {
//        if (pushRecordDTO != null) {
//            if (!StringUtils.isEmpty(pushRecordDTO.getStartTime()) && !StringUtils.isEmpty(pushRecordDTO.getEndTime())) {
//                long queryStartTime = DateUtil.stringToDate(pushRecordDTO.getStartTime(), "yyyy-MM-dd HH:mm:ss").getTime() / 1000L;
//                long queryEndTime = DateUtil.stringToDate(pushRecordDTO.getEndTime(), "yyyy-MM-dd HH:mm:ss").getTime() / 1000L;
//                List<AccessRecord> accessRecords = accessService.queryAccrecord(queryStartTime, queryEndTime, pushRecordDTO.getPark(), pushRecordDTO.getStaffNum());
//                if (accessRecords != null) {
//                    accessService.processAccessRecordResponse(accessRecords);
//                    return ResultGenerator.genSuccessResult();
//                } else {
//                    return ResultGenerator.genSuccessResult("该时段数据为空");
//                }
//            } else {
//                return ResultGenerator.genFailResult("请输入开始时间和结束时间");
//            }
//        }
//
//        return ResultGenerator.genFailResult("补推出错");
//    }


    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":{"serialVersionUID":0,"pageNum":0,"pageSize":0,"size":0,"orderBy":"str","startRow":0,"endRow":0,"total":0,"pages":0,"list":[{"recordId": null,"staffName": null,"staffId": null,"staffNumber": null,"staffParkName": "三亚海棠湾","staffParkId": "3c8d71bb-7f7b-4a5f-aadd-99726e425640","staffTagName": "广州宏达工程顾问集团有限公司","staffTagId": "927fdea8-8c31-409b-ab23-df281182c8ae","signDeviceName": null,"signOutDeviceName": null,"signTime": null,"signOutTime": null,"status": null,"createTime": null,"updateTime": null,"day": "2021-05-21","normal": 10,"abnormal": null,"averageTime": 2.1,"countTime": 20.8,"totalStaff": null}],"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":true,"hasNextPage":true,"navigatePages":0,"navigatepageNums":"int[]","navigateFirstPage":0,"navigateLastPage":0}}
     * @catalog 考勤管理
     * @title 单位考勤统计
     * @description 单位考勤统计
     * @method POST
     * @url IP/api/attendance/record/count/unit
     * @header token 必选 string token
     * @json_param {"condition":"str","signTime":"str","signOutTime":"str","parkId":"str","staffNum":"str","tagIds":[],"page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @PostMapping("/count/unit")
    public Result countUnit(@RequestBody AttendanceDTO attendanceDTO) {
        List<AttendanceRecordDTO> attendanceRecordDTOS = attendanceRecordService.countUnitReocord(attendanceDTO);
        PageInfo pageInfo = new PageInfo(attendanceRecordDTOS);
        pageInfo.setList(Util.pagingQuery(attendanceDTO.getPage(), attendanceDTO.getLimit(), attendanceRecordDTOS));
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":"str"}
     * @catalog 考勤管理
     * @title 单位考勤统计导出
     * @description 单位考勤统计导出
     * @method POST
     * @url IP/api/attendance/record/export/unit
     * @header token 必选 string token
     * @json_param {"condition":"str","signTime":"str","signOutTime":"str","parkId":"str","staffNum":"str","tagIds":[],"page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，data为下载路径，其他为失败
     * @number 1.0
     */
    @PostMapping("/export/unit")
    public Result exportUnit(@RequestHeader String token, @RequestBody AttendanceDTO attendanceDTO) {
        String account = JwtUtil.getAccount(token);
        String fileName = attendanceRecordService.exportUnitRecord(attendanceDTO);
        if (fileName != null) {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, String.format("单位考勤统计导出成功"));
            return ResultGenerator.genSuccessResult(fileName);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, String.format("单位考勤统计导出事变"));
            return ResultGenerator.genFailResult(MessageConstant.EXPORT_FAILED_CN);
        }
    }


    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":"str"}
     * @catalog 考勤管理
     * @title 园区考勤统计导出
     * @description 园区考勤统计导出
     * @method POST
     * @url IP/api/attendance/record/export/park
     * @header token 必选 string token
     * @json_param {"condition":"str","signTime":"str","signOutTime":"str","parkId":"str","staffNum":"str","tagIds":[],"page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，data为下载路径，其他为失败
     * @number 1.0
     */
    @PostMapping("/export/park")
    public Result exportPark(@RequestHeader String token, @RequestBody AttendanceDTO attendanceDTO) {
        String account = JwtUtil.getAccount(token);
        String fileName = attendanceRecordService.exportParkrRecord(attendanceDTO);
        if (fileName != null) {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, String.format("园区考勤统计导出成功"));
            return ResultGenerator.genSuccessResult(fileName);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, String.format("园区考勤统计导出事变"));
            return ResultGenerator.genFailResult(MessageConstant.EXPORT_FAILED_CN);
        }
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":{"serialVersionUID":0,"pageNum":0,"pageSize":0,"size":0,"orderBy":"str","startRow":0,"endRow":0,"total":0,"pages":0,"list":[{"recordId": null,"staffName": null,"staffId": null,"staffNumber": null,"staffParkName": "三亚海棠湾","staffParkId": "3c8d71bb-7f7b-4a5f-aadd-99726e425640","staffTagName": "","staffTagId": "","signDeviceName": null,"signOutDeviceName": null,"signTime": null,"signOutTime": null,"status": null,"createTime": null,"updateTime": null,"day": "2021-05-21","normal": 10,"abnormal": null,"averageTime": 2.1,"countTime": 20.8,"totalStaff": null}],"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":true,"hasNextPage":true,"navigatePages":0,"navigatepageNums":"int[]","navigateFirstPage":0,"navigateLastPage":0}}
     * @catalog 考勤管理
     * @title 园区考勤统计
     * @description 园区考勤统计
     * @method POST
     * @url IP/api/attendance/record/count/park
     * @header token 必选 string token
     * @json_param {"condition":"str","signTime":"str","signOutTime":"str","parkId":"str","staffNum":"str","tagIds":[],"page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @PostMapping("/count/park")
    public Result countPark(@RequestBody AttendanceDTO attendanceDTO) {
        List<AttendanceRecordDTO> attendanceRecordDTOS = attendanceRecordService.countParkRecord(attendanceDTO);
        PageInfo pageInfo = new PageInfo(attendanceRecordDTOS);
        pageInfo.setList(Util.pagingQuery(attendanceDTO.getPage(), attendanceDTO.getLimit(), attendanceRecordDTOS));
        return ResultGenerator.genSuccessResult(pageInfo);
    }


    @PostMapping("/check/repeaat")
    public Result checkRepeatRcord(@RequestBody AttendanceDTO attendanceDTO) {
        attendanceRecordService.checkRepeatRecord(attendanceDTO);
        return ResultGenerator.genSuccessResult();
    }

}
