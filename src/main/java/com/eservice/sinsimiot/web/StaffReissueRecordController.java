package com.eservice.sinsimiot.web;

import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
import com.eservice.sinsimiot.core.MessageConstant;
import com.eservice.sinsimiot.core.OperationConstant;
import com.eservice.sinsimiot.model.staff_reissue_record.ReissueDTO;
import com.eservice.sinsimiot.model.staff_reissue_record.StaffReissueRecord;
import com.eservice.sinsimiot.service.LogRecordService;
import com.eservice.sinsimiot.service.StaffReissueRecordService;
import com.eservice.sinsimiot.util.JwtUtil;
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
 * @date 2021/06/01.
 */
@RestController
@RequestMapping("/staff/reissue/record")
public class StaffReissueRecordController {

    private final String MODULE = "补签管理";
    @Resource
    private LogRecordService logRecordService;
    @Resource
    private StaffReissueRecordService staffReissueRecordService;

    @PostMapping("/add")
    public Result add(@RequestBody @NotNull ReissueDTO reissueDTO) {
        StaffReissueRecord staffReissueRecord = staffReissueRecordService.saveReissueRecord(reissueDTO);
        if (staffReissueRecord != null) {
            return ResultGenerator.genSuccessResult(staffReissueRecord);
        } else {
            return ResultGenerator.genFailResult(MessageConstant.ADD_FAILED_CN);
        }
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam String id) {
        staffReissueRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(@RequestBody @NotNull StaffReissueRecord staffReissueRecord) {
        staffReissueRecordService.update(staffReissueRecord);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/confirm")
    public Result confirm(@RequestBody ReissueDTO reissueDTO) {
        StaffReissueRecord staffReissueRecord = staffReissueRecordService.confirmReissueRecord(reissueDTO);
        if (staffReissueRecord != null) {
            return ResultGenerator.genSuccessResult(staffReissueRecord);
        } else {
            return ResultGenerator.genFailResult("请稍后再试");
        }
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam @NotNull String id) {
        StaffReissueRecord staffReissueRecord = staffReissueRecordService.findById(id);
        return ResultGenerator.genSuccessResult(staffReissueRecord);
    }

    @PostMapping("/list")
    public Result list(@RequestBody ReissueDTO reissueDTO) {
        PageHelper.startPage(reissueDTO.getPage(), reissueDTO.getLimit());
        List<StaffReissueRecord> list = staffReissueRecordService.fetchReissueRecord(reissueDTO);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/search")
    public Result search(@RequestBody ReissueDTO reissueDTO) {
        PageHelper.startPage(reissueDTO.getPage(), reissueDTO.getLimit());
        List<StaffReissueRecord> list = staffReissueRecordService.searchRecord(reissueDTO);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/export")
    public Result export(@RequestHeader String token, @RequestBody ReissueDTO reissueDTO) {
        String account = JwtUtil.getAccount(token);
        String fileName = staffReissueRecordService.exportReissueRecord(reissueDTO);
        if (fileName != null) {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, String.format("补签信息导出成功"));
            return ResultGenerator.genSuccessResult(fileName);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, String.format("补签信息导出失败"));
            return ResultGenerator.genFailResult(MessageConstant.EXPORT_FAILED_CN);
        }
    }
}
