package com.eservice.sinsimiot.model.attendance_record;

/**
 * @author xyh
 * @version v1.0
 * @description TODO
 * @date 2021/6/18 9:21
 */
public class PushRecordDTO {
    private String startTime;
    private String endTime;
    private String park;
    private String staffNum;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPark() {
        return park;
    }

    public void setPark(String park) {
        this.park = park;
    }

    public String getStaffNum() {
        return staffNum;
    }

    public void setStaffNum(String staffNum) {
        this.staffNum = staffNum;
    }
}
