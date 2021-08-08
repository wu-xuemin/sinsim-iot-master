package com.eservice.sinsimiot.service.impl;

import com.eservice.sinsimiot.common.AbstractServiceImpl;
import com.eservice.sinsimiot.dao.ParkInfoMapper;
import com.eservice.sinsimiot.model.park_info.ParkInfo;
import com.eservice.sinsimiot.model.park_info.ParkInfoDTO;
import com.eservice.sinsimiot.service.ParkInfoService;
import com.google.common.base.Joiner;
import com.eservice.sinsimiot.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;


/**
 * Class Description: xxx
 *
 * @author Mr.xie
 * @date 2021/05/07.
 */
@Service
@Transactional
public class ParkInfoServiceImpl extends AbstractServiceImpl<ParkInfo> implements ParkInfoService {
    @Resource
    private ParkInfoMapper parkInfoMapper;

    @Override
    public boolean save(ParkInfo model) {
        model.setParkId(UUID.randomUUID().toString());
        model.setCreateTime(System.currentTimeMillis() / 1000L);
        if (parkInfoMapper.insertSelective(model) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(ParkInfo model) {
        model.setUpdateTime(System.currentTimeMillis() / 1000L);
        if (parkInfoMapper.updateByPrimaryKeySelective(model) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<ParkInfo> findByCondition(ParkInfoDTO parkInfoDTO) {
        return parkInfoMapper.findByCondition(parkInfoDTO);
    }

    @Override
    public List<ParkInfo> findByParkOnTag(String tagId) {
        return parkInfoMapper.findParkByTag(tagId);
    }

    @Override
    public void updateParkByTagId(String tagId) {
        List<ParkInfo> parkInfos = parkInfoMapper.findParkByTag(tagId);
        if (parkInfos != null && parkInfos.size() > 0) {
            for (ParkInfo park : parkInfos) {
                List<String> tagIds = Util.stringToArrayList(park.getUnit(), ",");
                tagIds.remove(tagId);
                park.setUnit(Joiner.on(",").join(tagIds));
                update(park);
            }
        }
    }
}
