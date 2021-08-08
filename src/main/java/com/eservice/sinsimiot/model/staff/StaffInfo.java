package com.eservice.sinsimiot.model.staff;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "staff_info")
public class StaffInfo {
    /**
     * 员工id
     */
    @Id
    @Column(name = "staff_id")
    private String staffId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 工号
     */
    @Column(name = "staff_number")
    private String staffNumber;

    /**
     * 卡号
     */
    @Column(name = "card_number")
    private String cardNumber;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 所属园区
     */
    private String parkId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 入职时间
     */
    @Column(name = "entry_time")
    private String entryTime;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 备注
     */
    private String remark;

    /**
     * 微信昵称
     */
    private String nickname;

    /**
     * 微信open id
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 微信号
     */
    @Column(name = "wechat_number")
    private String wechatNumber;

    /**
     * 微信头像
     */
    @Column(name = "photo_address")
    private String photoAddress;

    /**
     * 是否拉人黑名单
     */
    @Column(name = "is_block")
    private Boolean isBlock;

    /**
     * 已阅读的条款id
     */
    @Column(name = "clause_id")
    private String clauseId;

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
     * 人像id
     */
    @Column(name = "face_ids")
    private String faceIds;

    /**
     * 人员标签
     */
    @Column(name = "tag_ids")
    private String tagIds;

    /**
     * 扩展字段
     */
    private String meta;

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
     * 获取姓名
     *
     * @return name - 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取工号
     *
     * @return staff_number - 工号
     */
    public String getStaffNumber() {
        return staffNumber;
    }

    /**
     * 设置工号
     *
     * @param staffNumber 工号
     */
    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    /**
     * 获取卡号
     *
     * @return card_number - 卡号
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * 设置卡号
     *
     * @param cardNumber 卡号
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * 获取联系方式
     *
     * @return contact - 联系方式
     */
    public String getContact() {
        return contact;
    }

    /**
     * 设置联系方式
     *
     * @param contact 联系方式
     */
    public void setContact(String contact) {
        this.contact = contact;
    }


    /**
     * 获取所属园区
     * @return
     */
    public String getParkId() {
        return parkId;
    }

    /**
     * 设置所属园区
     * @param parkId
     */
    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取入职时间
     *
     * @return entry_time - 入职时间
     */
    public String getEntryTime() {
        return entryTime;
    }

    /**
     * 设置入职时间
     *
     * @param entryTime 入职时间
     */
    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    /**
     * 获取生日
     *
     * @return birthday - 生日
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * 设置生日
     *
     * @param birthday 生日
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取微信昵称
     *
     * @return nickname - 微信昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置微信昵称
     *
     * @param nickname 微信昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取微信open id
     *
     * @return open_id - 微信open id
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置微信open id
     *
     * @param openId 微信open id
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 获取微信号
     *
     * @return wechat_number - 微信号
     */
    public String getWechatNumber() {
        return wechatNumber;
    }

    /**
     * 设置微信号
     *
     * @param wechatNumber 微信号
     */
    public void setWechatNumber(String wechatNumber) {
        this.wechatNumber = wechatNumber;
    }

    /**
     * 获取微信头像
     *
     * @return photo_address - 微信头像
     */
    public String getPhotoAddress() {
        return photoAddress;
    }

    /**
     * 设置微信头像
     *
     * @param photoAddress 微信头像
     */
    public void setPhotoAddress(String photoAddress) {
        this.photoAddress = photoAddress;
    }

    /**
     * 获取是否拉人黑名单
     *
     * @return is_block - 是否拉人黑名单
     */
    public Boolean getIsBlock() {
        return isBlock;
    }

    /**
     * 设置是否拉人黑名单
     *
     * @param isBlock 是否拉人黑名单
     */
    public void setIsBlock(Boolean isBlock) {
        this.isBlock = isBlock;
    }

    /**
     * 获取已阅读的条款id
     *
     * @return clause_id - 已阅读的条款id
     */
    public String getClauseId() {
        return clauseId;
    }

    /**
     * 设置已阅读的条款id
     *
     * @param clauseId 已阅读的条款id
     */
    public void setClauseId(String clauseId) {
        this.clauseId = clauseId;
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

    /**
     * 获取人像id
     *
     * @return face_ids - 人像id
     */
    public String getFaceIds() {
        return faceIds;
    }

    /**
     * 设置人像id
     *
     * @param faceIds 人像id
     */
    public void setFaceIds(String faceIds) {
        this.faceIds = faceIds;
    }

    /**
     * 获取人员标签
     *
     * @return tag_ids - 人员标签
     */
    public String getTagIds() {
        return tagIds;
    }

    /**
     * 设置人员标签
     *
     * @param tagIds 人员标签
     */
    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    /**
     * 获取扩展字段
     *
     * @return meta - 扩展字段
     */
    public String getMeta() {
        return meta;
    }

    /**
     * 设置扩展字段
     *
     * @param meta 扩展字段
     */
    public void setMeta(String meta) {
        this.meta = meta;
    }
}
