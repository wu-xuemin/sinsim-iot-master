package com.eservice.sinsimiot.model.park;

/**
 * @program:isc_service
 * @description:设备基本状态
 * @author:Mr.xie
 * @create:2020/7/7 14:43
 */
public class DeviceStatus {
    private String device_add_time;
    private String status;
    private String status_detail;
    private String status_description;
    private Integer last_capture_timestamp;
    private Integer last_heart_beat_timestamp;

    public String getDevice_add_time() {
        return device_add_time;
    }

    public void setDevice_add_time(String device_add_time) {
        this.device_add_time = device_add_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_detail() {
        return status_detail;
    }

    public void setStatus_detail(String status_detail) {
        this.status_detail = status_detail;
    }

    public String getStatus_description() {
        return status_description;
    }

    public void setStatus_description(String status_description) {
        this.status_description = status_description;
    }

    public Integer getLast_capture_timestamp() {
        return last_capture_timestamp;
    }

    public void setLast_capture_timestamp(Integer last_capture_timestamp) {
        this.last_capture_timestamp = last_capture_timestamp;
    }

    public Integer getLast_heart_beat_timestamp() {
        return last_heart_beat_timestamp;
    }

    public void setLast_heart_beat_timestamp(Integer last_heart_beat_timestamp) {
        this.last_heart_beat_timestamp = last_heart_beat_timestamp;
    }
}
