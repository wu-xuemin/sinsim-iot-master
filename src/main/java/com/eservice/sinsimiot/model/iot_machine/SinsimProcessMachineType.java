package com.eservice.sinsimiot.model.iot_machine;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//生产部
@Table(name = "machine_type")
public class SinsimProcessMachineType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 机器类型
     */
    private String name;

    /**
     * 是否为成品机
     */
    private Integer finished;

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
     * 获取机器类型
     *
     * @return name - 机器类型
     */
    public String getName() {
        return name;
    }

    /**
     * 设置机器类型
     *
     * @param name 机器类型
     */
    public void setName(String name) {
        this.name = name;
    }

    public Integer getFinished() {
        return finished;
    }

    public void setFinished(Integer finished) {
        this.finished = finished;
    }
}