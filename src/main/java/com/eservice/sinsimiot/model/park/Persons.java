package com.eservice.sinsimiot.model.park;

/**
 * @program:record_service
 * @description:以图搜人返回信息
 * @author:Mr.xie
 * @create:2020/7/27 11:39
 */
public class Persons {
    private Double score;
    private Staff person;
    private String face_id;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Staff getPerson() {
        return person;
    }

    public void setPerson(Staff person) {
        this.person = person;
    }

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }
}
