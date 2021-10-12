package com.eservice.sinsimiot.model.pattern;

import javax.persistence.*;

public class Pattern {
    /**
     * 花样
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 花样的所有者
     */
    private String user;

    /**
     * 花样名称
     */
    @Column(name = "pattern_name")
    private String patternName;

    /**
     * “花样数目”
     */
    @Column(name = "pattern_number")
    private String patternNumber;

    /**
     * 总针数
     */
    @Column(name = "needles_total")
    private String needlesTotal;

    /**
     * 刺绣时间
     */
    @Column(name = "embroidery_time")
    private String embroideryTime;

    /**
     * 工件数量
     */
    @Column(name = "parts_total_number")
    private Integer partsTotalNumber;

    /**
     * 获取花样
     *
     * @return id - 花样
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置花样
     *
     * @param id 花样
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取花样的所有者
     *
     * @return user - 花样的所有者
     */
    public String getUser() {
        return user;
    }

    /**
     * 设置花样的所有者
     *
     * @param user 花样的所有者
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * 获取花样名称
     *
     * @return pattern_name - 花样名称
     */
    public String getPatternName() {
        return patternName;
    }

    /**
     * 设置花样名称
     *
     * @param patternName 花样名称
     */
    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    /**
     * 获取“花样数目”
     *
     * @return pattern_number - “花样数目”
     */
    public String getPatternNumber() {
        return patternNumber;
    }

    /**
     * 设置“花样数目”
     *
     * @param patternNumber “花样数目”
     */
    public void setPatternNumber(String patternNumber) {
        this.patternNumber = patternNumber;
    }

    /**
     * 获取总针数
     *
     * @return needles_total - 总针数
     */
    public String getNeedlesTotal() {
        return needlesTotal;
    }

    /**
     * 设置总针数
     *
     * @param needlesTotal 总针数
     */
    public void setNeedlesTotal(String needlesTotal) {
        this.needlesTotal = needlesTotal;
    }

    /**
     * 获取刺绣时间
     *
     * @return embroidery_time - 刺绣时间
     */
    public String getEmbroideryTime() {
        return embroideryTime;
    }

    /**
     * 设置刺绣时间
     *
     * @param embroideryTime 刺绣时间
     */
    public void setEmbroideryTime(String embroideryTime) {
        this.embroideryTime = embroideryTime;
    }

    /**
     * 获取工件数量
     *
     * @return parts_total_number - 工件数量
     */
    public Integer getPartsTotalNumber() {
        return partsTotalNumber;
    }

    /**
     * 设置工件数量
     *
     * @param partsTotalNumber 工件数量
     */
    public void setPartsTotalNumber(Integer partsTotalNumber) {
        this.partsTotalNumber = partsTotalNumber;
    }
}