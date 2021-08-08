package com.eservice.sinsimiot.model.account;/**
 * @description : 查询DTO
 * @author : silent
 * @date: 2020/3/20 9:58
 */

/**
 * @author : silent
 * @description : 账号或角色查询DTO
 * @date: 2020/3/20 9:58
 */
public class AccountDTO {

    String condition;
    String roleId;
    Integer page;
    Integer limit;
    String parkId;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
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


    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
