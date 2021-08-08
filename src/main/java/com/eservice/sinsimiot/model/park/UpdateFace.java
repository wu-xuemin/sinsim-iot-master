package com.eservice.sinsimiot.model.park;

import java.util.List;

/**
 * @Description: 修改人脸图片信息
 * @Author: ZT
 * @CreateDate: 2019/3/15 10:54
 */
public class UpdateFace {

    List<String> delete_face_id_list;
    boolean enforce;
    List<String> insert_face_image_content_list;

    public List<String> getDelete_face_id_list() {
        return delete_face_id_list;
    }

    public void setDelete_face_id_list(List<String> delete_face_id_list) {
        this.delete_face_id_list = delete_face_id_list;
    }

    public boolean isEnforce() {
        return enforce;
    }

    public void setEnforce(boolean enforce) {
        this.enforce = enforce;
    }

    public List<String> getInsert_face_image_content_list() {
        return insert_face_image_content_list;
    }

    public void setInsert_face_image_content_list(List<String> insert_face_image_content_list) {
        this.insert_face_image_content_list = insert_face_image_content_list;
    }
}
