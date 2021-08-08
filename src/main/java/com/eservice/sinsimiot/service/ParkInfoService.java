package com.eservice.sinsimiot.service;

import com.eservice.sinsimiot.common.Service;
import com.eservice.sinsimiot.model.park_info.ParkInfo;
import com.eservice.sinsimiot.model.park_info.ParkInfoDTO;

import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Mr.xie
 * @date 2021/05/07.
 */
public interface ParkInfoService extends Service<ParkInfo> {

    List<ParkInfo> findByCondition(ParkInfoDTO parkInfoDTO);

    List<ParkInfo> findByParkOnTag(String tagId);

    void updateParkByTagId(String tagId);

}
