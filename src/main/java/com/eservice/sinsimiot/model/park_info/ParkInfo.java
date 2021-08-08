package com.eservice.sinsimiot.model.park_info;

import javax.persistence.*;

@Table(name = "park_info")
public class ParkInfo {
    /**
     * 园区id
     */
    @Id
    @Column(name = "park_id")
    private String parkId;

    /**
     * 园区名称
     */
    @Column(name = "park_name")
    private String parkName;

    /**
     * 单位
     */
    private String unit;

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
     * 获取园区id
     *
     * @return park_id - 园区id
     */
    public String getParkId() {
        return parkId;
    }

    /**
     * 设置园区id
     *
     * @param parkId 园区id
     */
    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    /**
     * 获取园区名称
     *
     * @return park_name - 园区名称
     */
    public String getParkName() {
        return parkName;
    }

    /**
     * 设置园区名称
     *
     * @param parkName 园区名称
     */
    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    /**
     * 获取单位
     *
     * @return unit - 单位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置单位
     *
     * @param unit 单位
     */
    public void setUnit(String unit) {
        this.unit = unit;
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