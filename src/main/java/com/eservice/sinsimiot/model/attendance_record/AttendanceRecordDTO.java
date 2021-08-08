package com.eservice.sinsimiot.model.attendance_record;

/**
 * @author xyh
 * @version v1.0
 * @description TODO
 * @date 2021/5/10 14:33
 */
public class AttendanceRecordDTO extends AttendanceRecord {

    private String day;
    private Integer normal;
    private Integer abnormal;
    private Float averageTime;
    private Float countTime;
    private boolean valid;
    private Integer totalStaff;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getNormal() {
        return normal;
    }

    public void setNormal(Integer normal) {
        this.normal = normal;
    }

    public Integer getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(Integer abnormal) {
        this.abnormal = abnormal;
    }

    public Float getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(Float averageTime) {
        this.averageTime = averageTime;
    }

    public Float getCountTime() {
        return countTime;
    }

    public void setCountTime(Float countTime) {
        this.countTime = countTime;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Integer getTotalStaff() {
        return totalStaff;
    }

    public void setTotalStaff(Integer totalStaff) {
        this.totalStaff = totalStaff;
    }
}
