package com.eservice.sinsimiot.model.park;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Class Description:
 *
 * @author Wilson Hu
 * @date 8/15/2018
 */
public class Tag {


    public Tag() {

    }

    public Tag(String name, List<String> identity, Map meta) {
        this.meta = meta;
        this.tag_name = name;
        this.visible_identity = identity;
    }

    /**
     * count : 0
     * create_time : 0
     * meta : {}
     * tag_id : string
     * tag_name : string
     * visible_identity : ["STAFF"]
     */
    @JsonProperty("count")
    private int count;
    @JsonProperty("create_time")
    private int create_time;
    @JsonProperty("meta")
    private Map meta;
    @JsonProperty("tag_id")
    private String tag_id;
    @JsonProperty("tag_name")
    private String tag_name;
    @JsonProperty("visible_identity")
    private List<String> visible_identity;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public Map getMeta() {
        return meta;
    }

    public void setMeta(Map meta) {
        this.meta = meta;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public List<String> getVisible_identity() {
        return visible_identity;
    }

    public void setVisible_identity(List<String> visible_identity) {
        this.visible_identity = visible_identity;
    }
}
