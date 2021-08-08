package com.eservice.sinsimiot.dao;

import com.eservice.sinsimiot.common.Mapper;
import com.eservice.sinsimiot.model.park_info.ParkInfo;
import com.eservice.sinsimiot.model.park_info.ParkInfoDTO;

import java.util.List;

public interface ParkInfoMapper extends Mapper<ParkInfo> {

    List<ParkInfo> findByCondition(ParkInfoDTO parkInfoDTO);

    List<ParkInfo> findParkByTag(String tagId);
}
