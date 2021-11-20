package com.eservice.sinsimiot.model.iot_machine;


//import org.bson.types.ObjectId;

import org.bson.types.ObjectId;

import javax.persistence.*;
import java.util.Date;

/**
 * 这个类是给 mongodb用的
 */
@Table(name = "iot_machine")
public class IotMachineMongo {
    /**
     * iot开头的都是绣花机物联网项目
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private ObjectId id;
    /**
     * 在mysql里也保存iot机器的基本信息（历史信息则包含在mongodb中）
     */
    private ObjectId id;

    private String nameplate;

    /**
     * 机型信息
     */
    @Column(name = "machine_model_info")
    private String machineModelInfo;

    /**
     * 已开机的时间
     */
    private String uptime;

    /**
     * 刺绣（工作）时间
     */
    @Column(name = "working_time")
    private String workingTime;

    /**
     * 停机时间
     */
    @Column(name = "nonworking_time")
    private String nonworkingTime;

    /**
     * 断线次数
     */
    @Column(name = "line_broken_number")
    private String lineBrokenNumber;

    @Column(name = "line_broken_average_time")
    private String lineBrokenAverageTime;

    /**
     * 工件总数
     */
    @Column(name = "product_total_number")
    private String productTotalNumber;

    /**
     * 开机次数
     */
    @Column(name = "power_on_times")
    private String powerOnTimes;

    /**
     * 累计针数
     */
    @Column(name = "needle_total_number")
    private String needleTotalNumber;

    /**
     * 获取iot开头的都是绣花机物联网项目
     *
     * @return id - iot开头的都是绣花机物联网项目
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * 设置iot开头的都是绣花机物联网项目
     *
     * @param id iot开头的都是绣花机物联网项目
     */
    public void setId(ObjectId id) {
        this.id = id;
    }

    /**
     * @return nameplate
     */
    public String getNameplate() {
        return nameplate;
    }

    /**
     * @param nameplate
     */
    public void setNameplate(String nameplate) {
        this.nameplate = nameplate;
    }

    /**
     * 获取机型信息
     *
     * @return machine_model_info - 机型信息
     */
    public String getMachineModelInfo() {
        return machineModelInfo;
    }

    /**
     * 设置机型信息
     *
     * @param machineModelInfo 机型信息
     */
    public void setMachineModelInfo(String machineModelInfo) {
        this.machineModelInfo = machineModelInfo;
    }

    /**
     * 获取已开机的时间
     *
     * @return uptime - 已开机的时间
     */
    public String getUptime() {
        return uptime;
    }

    /**
     * 设置已开机的时间
     *
     * @param uptime 已开机的时间
     */
    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    /**
     * 获取刺绣（工作）时间
     *
     * @return working_time - 刺绣（工作）时间
     */
    public String getWorkingTime() {
        return workingTime;
    }

    /**
     * 设置刺绣（工作）时间
     *
     * @param workingTime 刺绣（工作）时间
     */
    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime;
    }

    /**
     * 获取停机时间
     *
     * @return nonworking_time - 停机时间
     */
    public String getNonworkingTime() {
        return nonworkingTime;
    }

    /**
     * 设置停机时间
     *
     * @param nonworkingTime 停机时间
     */
    public void setNonworkingTime(String nonworkingTime) {
        this.nonworkingTime = nonworkingTime;
    }

    /**
     * 获取断线次数
     *
     * @return line_broken_number - 断线次数
     */
    public String getLineBrokenNumber() {
        return lineBrokenNumber;
    }

    /**
     * 设置断线次数
     *
     * @param lineBrokenNumber 断线次数
     */
    public void setLineBrokenNumber(String lineBrokenNumber) {
        this.lineBrokenNumber = lineBrokenNumber;
    }

    /**
     * @return line_broken_average_time
     */
    public String getLineBrokenAverageTime() {
        return lineBrokenAverageTime;
    }

    /**
     * @param lineBrokenAverageTime
     */
    public void setLineBrokenAverageTime(String lineBrokenAverageTime) {
        this.lineBrokenAverageTime = lineBrokenAverageTime;
    }

    /**
     * 获取工件总数
     *
     * @return product_total_number - 工件总数
     */
    public String getProductTotalNumber() {
        return productTotalNumber;
    }

    /**
     * 设置工件总数
     *
     * @param productTotalNumber 工件总数
     */
    public void setProductTotalNumber(String productTotalNumber) {
        this.productTotalNumber = productTotalNumber;
    }

    /**
     * 获取开机次数
     *
     * @return power_on_times - 开机次数
     */
    public String getPowerOnTimes() {
        return powerOnTimes;
    }

    /**
     * 设置开机次数
     *
     * @param powerOnTimes 开机次数
     */
    public void setPowerOnTimes(String powerOnTimes) {
        this.powerOnTimes = powerOnTimes;
    }

    /**
     * 获取累计针数
     *
     * @return needle_total_number - 累计针数
     */
    public String getNeedleTotalNumber() {
        return needleTotalNumber;
    }

    /**
     * 设置累计针数
     *
     * @param needleTotalNumber 累计针数
     */
    public void setNeedleTotalNumber(String needleTotalNumber) {
        this.needleTotalNumber = needleTotalNumber;
    }

//    //该机器记录的首次创建时间 -->改为要记录历史记录之后，这里就作为创建时间
//    @Column(name = "create_time")
//    private Date createTime;
//
//    /**
//     * 该机器记录的更新时间 -->改为要记录历史记录之后，这里就弃用了
//     */
//    @Column(name = "update_time")
//    private Date updateTime;

    //该机器记录信息的创建账号
    private String user;

//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }
//
//    public Date getUpdateTime() {
//        return updateTime;
//    }
//
//    public void setUpdateTime(Date updateTime) {
//        this.updateTime = updateTime;
//    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    //该机器的花样(虽然可以从机器的机型信息来查询出花样，但是考虑到不同的用户，可能购买不同花样，所以单独建这个字段）
    private String pattern;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    //最后报上来的状态（运行中、故障、空闲），这个状态表明了机器的最后状态
    @Column(name = "last_status")
    private String lastStatus;

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }

    //机器真实状态，是在last_status基础上更新是否离线
    private String machineStatus;

    public String getMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(String machineStatus) {
        this.machineStatus = machineStatus;
    }

    //该机器记录的创建时间
    @Column(name = "created_time")
    private Date createdTime;

    //该机器记录的最后更新时间, 可用于判断是否离线
    @Column(name = "updated_time")
    private Date updatedTime;

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}