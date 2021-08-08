package com.eservice.sinsimiot.model.staff;


import com.eservice.sinsimiot.common.SearchDTO;

import java.util.List;

/**
 * @author : silent
 * @description : 员工/标签查询
 * @date: 2020/3/20 14:45
 */

public class StaffSearchDTO extends SearchDTO {

    public StaffSearchDTO() {
        tagIds = null;
        areaIds = null;
        tagType = null;
        parkId = null;
        account = null;
        password = null;
    }

    List<String> tagIds;
    List<String> areaIds;
    String tagType;
    String parkId;
    String account;
    String password;

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }

    public List<String> getAreaIds() {
        return areaIds;
    }

    public void setAreaIds(List<String> areaIds) {
        this.areaIds = areaIds;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
