package com.eservice.sinsimiot.model.park;

import java.util.List;


public class AccessRecord {
    private String id;
    private double score;
    private String device_id;
    private List<String> access_policy_id_list;
    private Staff person;
    private String identity;
    private String face_image_id;
    private String scene_image_id;
    private String face_id;
    private String record_type;
    private String track_id;
    private Long timestamp;
    private String pass_result;


    public List<String> getAccess_policy_id_list() {
        return access_policy_id_list;
    }

    public void setAccess_policy_id_list(List<String> access_policy_id_list) {
        this.access_policy_id_list = access_policy_id_list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    public String getFace_image_id() {
        return face_image_id;
    }

    public void setFace_image_id(String face_image_id) {
        this.face_image_id = face_image_id;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPass_result() {
        return pass_result;
    }

    public void setPass_result(String pass_result) {
        this.pass_result = pass_result;
    }

    public Staff getPerson() {
        return person;
    }

    public void setPerson(Staff person) {
        this.person = person;
    }

    public String getRecord_type() {
        return record_type;
    }

    public void setRecord_type(String record_type) {
        this.record_type = record_type;
    }

    public String getScene_image_id() {
        return scene_image_id;
    }

    public void setScene_image_id(String scene_image_id) {
        this.scene_image_id = scene_image_id;
    }

    public String getTrack_id() {
        return track_id;
    }

    public void setTrack_id(String track_id) {
        this.track_id = track_id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
