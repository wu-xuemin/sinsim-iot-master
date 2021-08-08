package com.eservice.sinsimiot.model.park;

/**
 * @program:isc_service
 * @description:设备信息
 * @author:Mr.xie
 * @create:2020/7/7 10:26
 */
public class DeviceInfo {

    private String dept_id;
    private String device_id;
    private DeviceMeta device_meta;
    private DeviceStatus deviceStatus;
    private String connection_type;

    public String getDept_id() {
        return dept_id;
    }

    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public DeviceMeta getDevice_meta() {
        return device_meta;
    }

    public void setDevice_meta(DeviceMeta device_meta) {
        this.device_meta = device_meta;
    }

    public DeviceStatus getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(DeviceStatus deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getConnection_type() {
        return connection_type;
    }

    public void setConnection_type(String connection_type) {
        this.connection_type = connection_type;
    }
}
