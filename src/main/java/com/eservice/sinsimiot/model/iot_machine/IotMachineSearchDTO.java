package com.eservice.sinsimiot.model.iot_machine;


import com.eservice.sinsimiot.common.SearchDTO;

import java.util.Date;
import java.util.List;

/**
 * @author : eservice
 * @description : IotMachine DTO查询
 */
public class IotMachineSearchDTO extends SearchDTO {

    public IotMachineSearchDTO() {
        nameplate = null;
        machineModelInfo = null;
        uptime = null;
        workingTime = null;
        nonworkingTime = null;
        lineBrokenNumber = null;
        lineBrokenAverageTime = null;
        productTotalNumber = null;
        powerOnTimes = null;
        needleTotalNumber = null;
        user = null;
        createTime = null;
        updateTime = null;
    }

    String nameplate;
    String machineModelInfo;
    String uptime;
    String workingTime;
    String nonworkingTime;
    String lineBrokenNumber;
    String lineBrokenAverageTime;
    String productTotalNumber;
    String powerOnTimes;
    String needleTotalNumber;

    String user;
    Date createTime;
    Date updateTime;

    public String getNameplate() {
        return nameplate;
    }

    public void setNameplate(String nameplate) {
        this.nameplate = nameplate;
    }

    public String getMachineModelInfo() {
        return machineModelInfo;
    }

    public void setMachineModelInfo(String machineModelInfo) {
        this.machineModelInfo = machineModelInfo;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime;
    }

    public String getNonworkingTime() {
        return nonworkingTime;
    }

    public void setNonworkingTime(String nonworkingTime) {
        this.nonworkingTime = nonworkingTime;
    }

    public String getLineBrokenNumber() {
        return lineBrokenNumber;
    }

    public void setLineBrokenNumber(String lineBrokenNumber) {
        this.lineBrokenNumber = lineBrokenNumber;
    }

    public String getLineBrokenAverageTime() {
        return lineBrokenAverageTime;
    }

    public void setLineBrokenAverageTime(String lineBrokenAverageTime) {
        this.lineBrokenAverageTime = lineBrokenAverageTime;
    }

    public String getProductTotalNumber() {
        return productTotalNumber;
    }

    public void setProductTotalNumber(String productTotalNumber) {
        this.productTotalNumber = productTotalNumber;
    }

    public String getPowerOnTimes() {
        return powerOnTimes;
    }

    public void setPowerOnTimes(String powerOnTimes) {
        this.powerOnTimes = powerOnTimes;
    }

    public String getNeedleTotalNumber() {
        return needleTotalNumber;
    }

    public void setNeedleTotalNumber(String needleTotalNumber) {
        this.needleTotalNumber = needleTotalNumber;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
