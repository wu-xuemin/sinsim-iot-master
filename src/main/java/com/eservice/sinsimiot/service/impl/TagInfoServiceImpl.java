package com.eservice.sinsimiot.service.impl;

import com.eservice.sinsimiot.service.ParkInfoService;
import com.eservice.sinsimiot.service.TagInfoService;
import com.eservice.sinsimiot.common.AbstractServiceImpl;
import com.eservice.sinsimiot.dao.TagInfoMapper;
import com.eservice.sinsimiot.model.staff.StaffSearchDTO;
import com.eservice.sinsimiot.model.tag.TagInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;


/**
 * Class Description: xxx
 *
 * @author Code Generator
 * @date 2020/03/06.
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TagInfoServiceImpl extends AbstractServiceImpl<TagInfo> implements TagInfoService {
    @Resource
    private TagInfoMapper tagInfoMapper;
    @Resource
    private ParkInfoService parkInfoService;

    @Override
    public List<String> selectTagNameByTagId(List<String> tagIds) {
        try {
            List<String> tagNames = tagInfoMapper.selectTagNameByTagId(tagIds);
            if (tagNames == null || tagNames.size() == 0) {
                return null;
            } else {
                return tagNames;
            }
        } catch (Exception e) {
            log.error("44 row : {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<TagInfo> selectTagInfoByTagId(List<String> tagIds) {
        return tagInfoMapper.selectTagInfoByTagId(tagIds);
    }


    /**
     * 根据条件查询
     *
     * @param staffSearchDTO
     * @return
     */
    @Override
    public List<TagInfo> findByCondition(StaffSearchDTO staffSearchDTO) {
        return tagInfoMapper.findByCondition(staffSearchDTO);
    }

    /**
     * 判断指定类型的标签名称是否重复
     *
     * @param tagName
     * @param type
     * @return
     */
    @Override
    public boolean checkTagName(String tagName, String type) {
        List<TagInfo> tagInfoList = tagInfoMapper.checkTagName(tagName, type);
        if (tagInfoList == null || tagInfoList.size() == 0) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public boolean save(TagInfo model) {
        model.setCreateTime(System.currentTimeMillis());
        model.setTagId(UUID.randomUUID().toString());
        model.setUpdateTime(System.currentTimeMillis());
        if (tagInfoMapper.insertSelective(model) > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean update(TagInfo model) {
        model.setUpdateTime(System.currentTimeMillis());
        if (tagInfoMapper.updateByPrimaryKeySelective(model) > 0) {
            return true;
        } else {
            return false;
        }
    }
}
