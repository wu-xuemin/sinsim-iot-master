package com.eservice.sinsimiot.model.log;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "log_record")
public class LogRecord {
    /**
     * 日志id
     */
    @Id
    @Column(name = "log_id")
    private String logId;

    /**
     * 模块名称
     */
    private String module;

    /**
     * 触发者
     */
    private String operator;

    /**
     * 日志类型
     */
    private String logType;

    /**
     * 记录时间
     */
    @Column(name = "record_time")
    private Long recordTime;

    /**
     * 详细信息
     */
    private String message;

    /**
     * 获取日志id
     *
     * @return log_id - 日志id
     */
    public String getLogId() {
        return logId;
    }

    /**
     * 设置日志id
     *
     * @param logId 日志id
     */
    public void setLogId(String logId) {
        this.logId = logId;
    }

    /**
     * 获取模块名称
     *
     * @return module - 模块名称
     */
    public String getModule() {
        return module;
    }

    /**
     * 设置模块名称
     *
     * @param module 模块名称
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * 获取触发者
     *
     * @return operator - 触发者
     */
    public String getOperator() {
        return operator;
    }

    /**
     * 设置触发者
     *
     * @param operator 触发者
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * 获取日志类型
     *
     * @return logType - 日志类型
     */
    public String getLogType() {
        return logType;
    }

    /**
     * 设置日志类型
     *
     * @param logType 日志类型
     */
    public void setLogType(String logType) {
        this.logType = logType;
    }

    /**
     * 获取记录时间
     *
     * @return record_time - 记录时间
     */
    public Long getRecordTime() {
        return recordTime;
    }

    /**
     * 设置记录时间
     *
     * @param recordTime 记录时间
     */
    public void setRecordTime(Long recordTime) {
        this.recordTime = recordTime;
    }

    /**
     * 获取详细信息
     *
     * @return message - 详细信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置详细信息
     *
     * @param message 详细信息
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
