package com.eservice.sinsimiot.model.attendance_record;

import com.eservice.sinsimiot.model.staff_reissue_record.StaffReissueRecord;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Table(name = "attendance_record")
public class AttendanceRecord {

    /**
     * 记录id
     */
    @Id
    @Column(name = "record_id")
    private String recordId;

    /**
     * 员工姓名
     */
    @Column(name = "staff_name")
    private String staffName;

    /**
     * 员工id
     */
    @Column(name = "staff_id")
    private String staffId;

    /**
     * 员工工号
     */
    @Column(name = "staff_number")
    private String staffNumber;

    /**
     * 所属园区名称
     */
    @Column(name = "staff_park_name")
    private String staffParkName;

    /**
     * 所属园区id
     */
    @Column(name = "staff_park_id")
    private String staffParkId;

    /**
     * 员工标签名称
     */
    @Column(name = "staff_tag_name")
    private String staffTagName;

    /**
     * 员工标签id
     */
    @Column(name = "staff_tag_id")
    private String staffTagId;

    /**
     * 上班打卡设备
     */
    @Column(name = "sign_device_name")
    private String signDeviceName;

    /**
     * 下班打卡设备
     */
    @Column(name = "sign_out_device_name")
    private String signOutDeviceName;

    /**
     * 上班签到时间
     */
    @Column(name = "sign_time")
    private String signTime;

    /**
     * 下班签到时间
     */
    @Column(name = "sign_out_time")
    private String signOutTime;

    /**
     * 考勤状态
     */
    private String status;

    /**
     * 考勤类型
     */
    private Integer type;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Long updateTime;

    /**
     * 获取记录id
     *
     * @return record_id - 记录id
     */
    public String getRecordId() {
        return recordId;
    }

    /**
     * 设置记录id
     *
     * @param recordId 记录id
     */
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    /**
     * 获取员工姓名
     *
     * @return staff_name - 员工姓名
     */
    public String getStaffName() {
        return staffName;
    }

    /**
     * 设置员工姓名
     *
     * @param staffName 员工姓名
     */
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    /**
     * 获取员工id
     *
     * @return staff_id - 员工id
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * 设置员工id
     *
     * @param staffId 员工id
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    /**
     * 获取员工工号
     *
     * @return staff_number - 员工工号
     */
    public String getStaffNumber() {
        return staffNumber;
    }

    /**
     * 设置员工工号
     *
     * @param staffNumber 员工工号
     */
    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    /**
     * 获取所属园区名称
     *
     * @return staff_park_name - 所属园区名称
     */
    public String getStaffParkName() {
        return staffParkName;
    }

    /**
     * 设置所属园区名称
     *
     * @param staffParkName 所属园区名称
     */
    public void setStaffParkName(String staffParkName) {
        this.staffParkName = staffParkName;
    }

    /**
     * 获取所属园区id
     *
     * @return staff_park_id - 所属园区id
     */
    public String getStaffParkId() {
        return staffParkId;
    }

    /**
     * 设置所属园区id
     *
     * @param staffParkId 所属园区id
     */
    public void setStaffParkId(String staffParkId) {
        this.staffParkId = staffParkId;
    }

    /**
     * 获取员工标签名称
     *
     * @return staff_tag_name - 员工标签名称
     */
    public String getStaffTagName() {
        return staffTagName;
    }

    /**
     * 设置员工标签名称
     *
     * @param staffTagName 员工标签名称
     */
    public void setStaffTagName(String staffTagName) {
        this.staffTagName = staffTagName;
    }

    /**
     * 获取员工标签id
     *
     * @return staff_tag_id - 员工标签id
     */
    public String getStaffTagId() {
        return staffTagId;
    }

    /**
     * 设置员工标签id
     *
     * @param staffTagId 员工标签id
     */
    public void setStaffTagId(String staffTagId) {
        this.staffTagId = staffTagId;
    }

    /**
     * 获取上班打卡设备
     *
     * @return sign_device_name - 上班打卡设备
     */
    public String getSignDeviceName() {
        return signDeviceName;
    }

    /**
     * 设置上班打卡设备
     *
     * @param signDeviceName 上班打卡设备
     */
    public void setSignDeviceName(String signDeviceName) {
        this.signDeviceName = signDeviceName;
    }

    /**
     * 获取下班打卡设备
     *
     * @return sign_out_device_name - 下班打卡设备
     */
    public String getSignOutDeviceName() {
        return signOutDeviceName;
    }

    /**
     * 设置下班打卡设备
     *
     * @param signOutDeviceName 下班打卡设备
     */
    public void setSignOutDeviceName(String signOutDeviceName) {
        this.signOutDeviceName = signOutDeviceName;
    }

    /**
     * 获取上班签到时间
     *
     * @return sign_time - 上班签到时间
     */
    public String getSignTime() {
        return signTime;
    }

    /**
     * 设置上班签到时间
     *
     * @param signTime 上班签到时间
     */
    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    /**
     * 获取下班签到时间
     *
     * @return sign_out_time - 下班签到时间
     */
    public String getSignOutTime() {
        return signOutTime;
    }

    /**
     * 设置下班签到时间
     *
     * @param signOutTime 下班签到时间
     */
    public void setSignOutTime(String signOutTime) {
        this.signOutTime = signOutTime;
    }

    /**
     * 获取考勤状态
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置考勤状态
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Long getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public AttendanceRecord() {
    }

    public AttendanceRecord(StaffReissueRecord staffReissueRecord) {
        this.staffName = staffReissueRecord.getStaffName();
        this.staffId = staffReissueRecord.getStaffId();
        this.staffNumber = staffReissueRecord.getStaffNum();
        this.staffParkName = staffReissueRecord.getStaffParkName();
        this.staffParkId = staffReissueRecord.getStaffParkId();
        this.staffTagName = staffReissueRecord.getStaffTagName();
        this.staffTagId = staffReissueRecord.getStaffTagId();
        if (!StringUtils.isEmpty(staffReissueRecord.getSignTime()) && !StringUtils.isEmpty(staffReissueRecord.getSignOutTime())) {
            this.signDeviceName = staffReissueRecord.getStaffParkName();
            this.signTime = staffReissueRecord.getSignTime();
            this.signOutDeviceName = staffReissueRecord.getStaffParkName();
            this.signOutTime = staffReissueRecord.getSignOutTime();
        } else if (!StringUtils.isEmpty(staffReissueRecord.getSignOutTime())) {
            this.signOutDeviceName = staffReissueRecord.getStaffParkName();
            this.signOutTime = staffReissueRecord.getSignOutTime();
        } else {
            this.signDeviceName = staffReissueRecord.getStaffParkName();
            this.signTime = staffReissueRecord.getSignTime();
        }
    }
}
