package com.eservice.sinsimiot.service;



import com.eservice.sinsimiot.model.log.LogRecord;
import com.eservice.sinsimiot.common.Service;
import com.eservice.sinsimiot.model.log.LogDTO;

import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Code Generator
 * @date 2020/03/06.
 */
public interface LogRecordService extends Service<LogRecord> {
    /**
     * 根据条件查询
     *
     * @param logDTO
     * @return
     */
    List<LogRecord> findByCondition(LogDTO logDTO);

    /**
     * 统一日志保存
     * @param operator 操作人员名称【如：admin】
     * @param module 模块名称【如：员工管理】
     * @param type 操作类型【如：新增】
     * @param message 详细信息【如：张三，zs123】
     * @return
     */
    boolean save(String operator,String module,String type,String message);

    /***
     * 导出指定的人员信息
     * @param logDTO
     * @return
     */
    String export(LogDTO logDTO);
}
