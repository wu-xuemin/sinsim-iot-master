package com.eservice.sinsimiot.model.malfunction;

import java.util.Date;
import javax.persistence.*;

public class Malfunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 发生故障的机器的铭牌号
     */
    private String nameplate;

    /**
     * 机器的本次故障的停车时间（在恢复工作时更新该值）
     */
    @Column(name = "machine_stop_time")
    private String machineStopTime;

    /**
     * 故障的原因
     */
    @Column(name = "malfunction_reason")
    private String malfunctionReason;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取发生故障的机器的铭牌号
     *
     * @return nameplate - 发生故障的机器的铭牌号
     */
    public String getNameplate() {
        return nameplate;
    }

    /**
     * 设置发生故障的机器的铭牌号
     *
     * @param nameplate 发生故障的机器的铭牌号
     */
    public void setNameplate(String nameplate) {
        this.nameplate = nameplate;
    }

    /**
     * 获取机器的本次故障的停车时间（在恢复工作时更新该值）
     *
     * @return machine_stop_time - 机器的本次故障的停车时间（在恢复工作时更新该值）
     */
    public String getMachineStopTime() {
        return machineStopTime;
    }

    /**
     * 设置机器的本次故障的停车时间（在恢复工作时更新该值）
     *
     * @param machineStopTime 机器的本次故障的停车时间（在恢复工作时更新该值）
     */
    public void setMachineStopTime(String machineStopTime) {
        this.machineStopTime = machineStopTime;
    }

    /**
     * 获取故障的原因
     *
     * @return malfunction_reason - 故障的原因
     */
    public String getMalfunctionReason() {
        return malfunctionReason;
    }

    /**
     * 设置故障的原因
     *
     * @param malfunctionReason 故障的原因
     */
    public void setMalfunctionReason(String malfunctionReason) {
        this.malfunctionReason = malfunctionReason;
    }

    /**
     * @return created_time
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return updated_time
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}