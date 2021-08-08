package com.eservice.sinsimiot.model.park_info;

import java.util.List;

/**
 * @author xyh
 * @version v1.0
 * @description TODO
 * @date 2021/5/7 13:22
 */
public class ParkInfoDTO {

    private Integer page;
    private Integer size;
    private String name;
    private List<String> tagIds;
    private String parkId;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }
}
