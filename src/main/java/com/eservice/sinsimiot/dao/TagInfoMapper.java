package com.eservice.sinsimiot.dao;


import com.eservice.sinsimiot.common.Mapper;
import com.eservice.sinsimiot.model.staff.StaffSearchDTO;
import com.eservice.sinsimiot.model.tag.TagInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : silent
 * @description : 标签管理
 * @date: 2020/3/23 15:43
 */
public interface TagInfoMapper extends Mapper<TagInfo> {

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
    List<TagInfo> checkTagName(@Param("tagName") String tagName, @Param("type") String type);
}
