package com.eservice.sinsimiot.service.impl;


import com.eservice.sinsimiot.common.AbstractServiceImpl;
import com.eservice.sinsimiot.dao.LogRecordMapper;
import com.eservice.sinsimiot.model.log.LogDTO;
import com.eservice.sinsimiot.model.log.LogRecord;
import com.eservice.sinsimiot.service.LogRecordService;
import com.eservice.sinsimiot.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Class Description: xxx
 *
 * @author Code Generator
 * @date 2020/03/06.
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class LogRecordServiceImpl extends AbstractServiceImpl<LogRecord> implements LogRecordService {

    @Value("${path.excel}")
    private String excelPath;
    @Value("${url.excel}")
    private String excelUrl;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    private LogRecordMapper logRecordMapper;

    /**
     * 根据条件查询
     *
     * @param logDTO
     * @return
     */
    @Override
    public List<LogRecord> findByCondition(LogDTO logDTO) {
        return logRecordMapper.findByCondition(logDTO);
    }

    /**
     * 统一日志保存
     *
     * @param operator 操作人员名称【如：admin】
     * @param module   模块名称【如：员工管理】
     * @param type     操作类型【如：新增】
     * @param message  详细信息【如：张三，zs123】
     * @return
     */
    @Override
    public boolean save(String operator, String module, String type, String message) {
        LogRecord logRecord = new LogRecord();
        logRecord.setLogId(UUID.randomUUID().toString());
        logRecord.setOperator(operator);
        logRecord.setModule(module);
        logRecord.setLogType(type);
        logRecord.setRecordTime(System.currentTimeMillis());
        logRecord.setMessage(message);
        return save(logRecord);
    }

    /***
     * 导出指定的人员信息
     * @param logDTO
     * @return
     */
    @Override
    public String export(LogDTO logDTO) {
        List<LogRecord> logRecords = logRecordMapper.findByCondition(logDTO);
        ArrayList<LinkedHashMap> maps = new ArrayList<>();
        for (LogRecord logRecord : logRecords) {
            try {
                LinkedHashMap map = new LinkedHashMap();
                map.put("name", logRecord.getOperator());
                map.put("module", logRecord.getModule());
                map.put("type", logRecord.getLogType());
                map.put("time", logRecord.getRecordTime());
                map.put("message", logRecord.getMessage());
                maps.add(map);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("export 98 : {} ", e.getMessage());
            }
        }
        if (maps != null && maps.size() > 0) {
            String[] header = new String[]{"操作用户", "功能名称", "操作类型", "操作时间", "操作内容"};
            String fileName = String.format("操作日志-%s.xls", sdf.format(new Date()));
            try {
                if (fileName.equals(ExcelUtil.insertDataInSheet(maps, header, excelPath, fileName))) {
                    return excelUrl + fileName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean save(LogRecord model) {
        if (logRecordMapper.insertSelective(model) > 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean update(LogRecord model) {
        if (logRecordMapper.updateByPrimaryKeySelective(model) > 0) {
            return true;
        } else {
            return false;
        }

    }
}
