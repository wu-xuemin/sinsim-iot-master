package com.eservice.sinsimiot.common;

import com.alibaba.fastjson.JSON;

/**
 * @author silent
 */
public class SearchDTO {
    public SearchDTO() {
        condition = null;
        page = 0;
        limit = 0;
    }

    private Long queryEndTime;
    private Long queryStartTime;
    private String condition;
    private Integer page;
    private Integer limit;

    public Long getQueryEndTime() {
        return queryEndTime;
    }

    public void setQueryEndTime(Long queryEndTime) {
        this.queryEndTime = queryEndTime;
    }

    public Long getQueryStartTime() {
        return queryStartTime;
    }

    public void setQueryStartTime(Long queryStartTime) {
        this.queryStartTime = queryStartTime;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
