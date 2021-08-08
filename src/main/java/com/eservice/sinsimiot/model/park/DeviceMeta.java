package com.eservice.sinsimiot.model.park;

/**
 * @program:isc_service
 * @description:设备详细信息
 * @author:Mr.xie
 * @create:2020/7/7 14:26
 */
public class DeviceMeta {
    private Integer direction_type;
    private String location;
    private Integer type;
    private String device_name;
    private String specification;
    private String version;
    private String ip;
    private Integer port;
    private String video_definition_type;
    private String username;
    private String password;
    private Boolean enable;
    private String docking_config;
    private String map_id;
    private String position;

    public Integer getDirection_type() {
        return direction_type;
    }

    public void setDirection_type(Integer direction_type) {
        this.direction_type = direction_type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getVideo_definition_type() {
        return video_definition_type;
    }

    public void setVideo_definition_type(String video_definition_type) {
        this.video_definition_type = video_definition_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getDocking_config() {
        return docking_config;
    }

    public void setDocking_config(String docking_config) {
        this.docking_config = docking_config;
    }

    public String getMap_id() {
        return map_id;
    }

    public void setMap_id(String map_id) {
        this.map_id = map_id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
