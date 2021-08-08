package com.eservice.sinsimiot.service;

import com.eservice.sinsimiot.common.Service;
import com.eservice.sinsimiot.model.staff.StaffSearchDTO;
import com.eservice.sinsimiot.model.tag.TagInfo;

import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Code Generator
 * @date 2020/03/06.
 */
public interface TagInfoService extends Service<TagInfo> {

    /**
     * 根据id获取名称
     *
     * @param tagIds
     * @return
     */
    List<String> selectTagNameByTagId(List<String> tagIds);

    /**
     * 根据id获取标签信息
     * @param tagIds
     * @return
     */
    List<TagInfo> selectTagInfoByTagId(List<String> tagIds);

    /**
     * 根据条件查询
     *
     * @param staffSearchDTO
     * @return
     */
    List<TagInfo> findByCondition(StaffSearchDTO staffSearchDTO);

    /**
     * 判断指定类型的标签名称是否重复
     *
     * @param tagName
     * @param type
     * @return
     */
    boolean checkTagName(String tagName, String type);
}
