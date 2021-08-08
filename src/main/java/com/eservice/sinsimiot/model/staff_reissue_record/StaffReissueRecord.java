package com.eservice.sinsimiot.model.staff_reissue_record;

import javax.persistence.*;

@Table(name = "staff_reissue_record")
public class StaffReissueRecord {
    /**
     * 记录id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 员工id
     */
    @Column(name = "staff_id")
    private String staffId;

    /**
     * 员工姓名
     */
    @Column(name = "staff_name")
    private String staffName;

    /**
     * 员工工号
     */
    @Column(name = "staff_num")
    private String staffNum;

    /**
     * 园区名称
     */
    @Column(name = "staff_park_name")
    private String staffParkName;

    /**
     * 园区id
     */
    @Column(name = "staff_park_id")
    private String staffParkId;

    /**
     * 标签名称
     */
    @Column(name = "staff_tag_name")
    private String staffTagName;

    /**
     * 标签id
     */
    @Column(name = "staff_tag_id")
    private String staffTagId;
    /**
     * 签到时间
     */
    @Column(name = "sign_time")
    private String signTime;

    /**
     * 签退时间
     */
    @Column(name = "sign_out_time")
    private String signOutTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

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
     * @return id - 记录id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置记录id
     *
     * @param id 记录id
     */
    public void setId(String id) {
        this.id = id;
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
     * 获取员工工号
     *
     * @return staff_num - 员工工号
     */
    public String getStaffNum() {
        return staffNum;
    }

    /**
     * 设置员工工号
     *
     * @param staffNum 员工工号
     */
    public void setStaffNum(String staffNum) {
        this.staffNum = staffNum;
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
     * 获取签到时间
     *
     * @return sign_time - 签到时间
     */
    public String getSignTime() {
        return signTime;
    }

    /**
     * 设置签到时间
     *
     * @param signTime 签到时间
     */
    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    /**
     * 获取签退时间
     *
     * @return sign_out_time - 签退时间
     */
    public String getSignOutTime() {
        return signOutTime;
    }

    /**
     * 设置签退时间
     *
     * @param signOutTime 签退时间
     */
    public void setSignOutTime(String signOutTime) {
        this.signOutTime = signOutTime;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(String status) {
        this.status = status;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}
