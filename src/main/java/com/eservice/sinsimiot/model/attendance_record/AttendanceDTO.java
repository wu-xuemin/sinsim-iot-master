package com.eservice.sinsimiot.model.attendance_record;

import com.eservice.sinsimiot.common.SearchDTO;

import java.util.List;

/**
 * @author xyh
 * @version v1.0
 * @description TODO
 * @date 2021/5/8 15:04
 */
public class AttendanceDTO extends SearchDTO {

    private String signTime;
    private String signOutTime;
    private String parkId;
    private String staffNum;
    private List<String> tagIds;


    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public String getSignOutTime() {
        return signOutTime;
    }

    public void setSignOutTime(String signOutTime) {
        this.signOutTime = signOutTime;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getStaffNum() {
        return staffNum;
    }

    public void setStaffNum(String staffNum) {
        this.staffNum = staffNum;
    }

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }
}
