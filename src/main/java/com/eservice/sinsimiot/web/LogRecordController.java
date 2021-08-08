package com.eservice.sinsimiot.web;

import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
import com.eservice.sinsimiot.core.MessageConstant;
import com.eservice.sinsimiot.core.OperationConstant;
import com.eservice.sinsimiot.model.log.LogRecord;
import com.eservice.sinsimiot.service.LogRecordService;
import com.eservice.sinsimiot.util.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.eservice.sinsimiot.model.log.LogDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* Class Description: xxx
* @author Code Generator
* @date 2020/03/06.
*/
@RestController
@RequestMapping("/log/record")
public class LogRecordController {

    private final String MODULE = "操作日志";

    @Resource
    private LogRecordService logRecordService;



    /**
     *  showdoc
     * @catalog 日志
     * @title 搜索
     * @description 日志记录查询
     * @method POST
     * @url IP/api/log/record/search
     * @header token 必选 string token
     * @json_param {"condition":"str","beginTime":"str","endTime":"str","type":"str","module":"str","page":0,"limit":0}
     * @return {"code":200,"message":"SUCCESS","data":[{"logId":"str","module":"str","operator":"str","type":"str","recordTime":1587109956904,"message":"str"}]}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result search(@RequestBody @NotNull LogDTO logDTO) {
        PageHelper.startPage(logDTO.getPage(), logDTO.getLimit());
        List<LogRecord> list = logRecordService.findByCondition(logDTO);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 日志
     * @title 导出
     * @description 日志录导出
     * @method POST
     * @url IP/api/log/record/export
     * @header token 必选 string token
     * @json_param {"condition":"查询条件","beginTime":"开始日期","endTime":"结束日期","type":"类型","module":"模块名称"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public Result exportVisitRecord(@RequestHeader("token") String token, @RequestBody @NotNull LogDTO logDTO) {
        String account = JwtUtil.getAccount(token);
        String fileName = logRecordService.export(logDTO);
        if (fileName != null) {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, "操作日志记录导出成功");
            return ResultGenerator.genSuccessResult(fileName);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.EXPORT_CN, "操作日志记录导出失败");
            return ResultGenerator.genFailResult(MessageConstant.EXPORT_FAILED_CN);
        }
    }

}
