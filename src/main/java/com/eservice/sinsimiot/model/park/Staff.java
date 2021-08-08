package com.eservice.sinsimiot.model.park;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * 员工modal
 *
 * @author HT
 */
public class Staff {
    @JsonProperty("tag_id_list")
    private List<String> tag_id_list;
    @JsonProperty("upload_time")
    private Integer upload_time;
    @JsonProperty("person_information")
    private PersonInformation person_information;
    @JsonProperty("face_list")
    private List<FaceListBean> face_list;
    @JsonProperty("identity")
    private String identity;
    @JsonProperty("meta")
    private Map meta;
    @JsonProperty("scene_image_id")
    private String scene_image_id;
    @JsonProperty("staff_id")
    private String staff_id;
    @JsonProperty("card_numbers")
    private List<String> card_numbers;
    @JsonProperty("face_image_content_list")
    private List<String> face_image_content_list;
    @JsonProperty("update_face")
    private UpdateFace update_face;

    private String person_id;

    public List<String> getTag_id_list() {
        return tag_id_list;
    }

    public void setTag_id_list(List<String> tag_id_list) {
        this.tag_id_list = tag_id_list;
    }

    public Integer getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(Integer upload_time) {
        this.upload_time = upload_time;
    }

    public PersonInformation getPerson_information() {
        return person_information;
    }

    public void setPerson_information(PersonInformation person_information) {
        this.person_information = person_information;
    }

    public List<FaceListBean> getFace_list() {
        return face_list;
    }

    public void setFace_list(List<FaceListBean> face_list) {
        this.face_list = face_list;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Map getMeta() {
        return meta;
    }

    public void setMeta(Map meta) {
        this.meta = meta;
    }

    public String getScene_image_id() {
        return scene_image_id;
    }

    public void setScene_image_id(String scene_image_id) {
        this.scene_image_id = scene_image_id;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public List<String> getCard_numbers() {
        return card_numbers;
    }

    public void setCard_numbers(List<String> card_numbers) {
        this.card_numbers = card_numbers;
    }

    public List<String> getFace_image_content_list() {
        return face_image_content_list;
    }

    public void setFace_image_content_list(List<String> face_image_content_list) {
        this.face_image_content_list = face_image_content_list;
    }

    public UpdateFace getUpdate_face() {
        return update_face;
    }

    public void setUpdate_face(UpdateFace update_face) {
        this.update_face = update_face;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    /**
     * 目前判断相同的条件是staffId
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Staff) {
            Staff person = (Staff) obj;
            if (person.getStaff_id().equals(staff_id)) {
                return true;
            }
            return false;
        } else {
            return super.equals(obj);
        }
    }
}
