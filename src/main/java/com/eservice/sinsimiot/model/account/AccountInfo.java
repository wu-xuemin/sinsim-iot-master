package com.eservice.sinsimiot.model.account;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "account_info")
public class AccountInfo {
    /**
     * 账号id
     */
    @Id
    @Column(name = "account_id")
    private String accountId;

    /**
     * 用户名称
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 账号名称
     */
    private String account;

    /**
     * 账号密码
     */
    private String password;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private String roleId;

    /**
     * 所属园区
     */
    private String parkId;

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
     * 获取账号id
     *
     * @return account_id - 账号id
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * 设置账号id
     *
     * @param accountId 账号id
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * 获取用户名称
     *
     * @return user_name - 用户名称
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名称
     *
     * @param userName 用户名称
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取账号名称
     *
     * @return account - 账号名称
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置账号名称
     *
     * @param account 账号名称
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取账号密码
     *
     * @return password - 账号密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置账号密码
     *
     * @param password 账号密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取角色id
     *
     * @return role_id - 角色id
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * 设置角色id
     *
     * @param roleId 角色id
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
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
