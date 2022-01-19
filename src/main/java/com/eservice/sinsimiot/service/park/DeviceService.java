package com.eservice.sinsimiot.service.park;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.sinsimiot.core.ResponseCode;
import com.eservice.sinsimiot.service.ParkInfoService;
import com.eservice.sinsimiot.common.ResponseModel;
import com.eservice.sinsimiot.model.park.DeviceInfo;
import com.eservice.sinsimiot.model.park.DeviceSearchDTO;
import com.eservice.sinsimiot.model.park_info.ParkInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program:isc_service
 * @description:查询设备列表
 * @author:Mr.xie
 * @create:2020/7/7 10:04
 */
@Service
@Slf4j
public class DeviceService {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${park.ip}")
    private String PARK_BASE_URL;
    @Resource
    private ParkInfoService parkInfoService;

    private String token;

    private List<DeviceInfo> deviceInfoList = new ArrayList<>();
    private Logger log = LoggerFactory.getLogger(getClass());
    @Scheduled(initialDelay = 2000, fixedDelay = 1000 * 60)
    public void fetchDeviceList() {
        try {
            if (tokenService != null) {
                token = tokenService.getToken();
            }
            if (token != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.ACCEPT, "application/json");
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, token);
                HttpEntity httpEntity = new HttpEntity(headers);
                ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/devices", HttpMethod.GET, httpEntity, String.class);
                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                    String body = responseEntity.getBody();
                    if (body != null) {
                        processDeviceResponse(body);
                    }
                }
            }
        } catch (Exception e) {
            log.error("request device Exception :{}", e.getMessage());
        }

    }

    private void processDeviceResponse(String body) {
        ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
        if (responseModel != null && responseModel.getResult() != null) {
            List<DeviceInfo> tmpList = JSONArray.parseArray(responseModel.getResult(), DeviceInfo.class);
            if (tmpList != null && tmpList.size() > 0) {
                deviceInfoList = tmpList;
                log.info("deviceList : {}", deviceInfoList.size());
            }
        }
    }


    public List<DeviceInfo> getDeviceInfoList(DeviceSearchDTO deviceSearchDTO) {
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        if (deviceSearchDTO.getParkId() != null && !StringUtils.isEmpty(deviceSearchDTO.getParkId())) {
            ParkInfo parkInfo = parkInfoService.findById(deviceSearchDTO.getParkId());
            if (parkInfo != null) {
                deviceInfoList.forEach(device -> {
                    if (device.getDevice_meta().getDevice_name().contains(parkInfo.getParkName())) {
                        if (deviceSearchDTO.getCondition() != null && !StringUtils.isEmpty(deviceSearchDTO.getCondition())) {
                            if (device.getDevice_meta().getDevice_name().contains(deviceSearchDTO.getCondition()) || device.getDevice_id().contains(deviceSearchDTO.getCondition())) {
                                deviceInfos.add(device);
                            }
                        } else {
                            deviceInfos.add(device);
                        }
                    }
                });
                return deviceInfos;
            } else {
                return new ArrayList<>();
            }
        } else if (deviceSearchDTO.getCondition() != null && !StringUtils.isEmpty(deviceSearchDTO.getCondition())) {
            deviceInfoList.forEach(device -> {
                if (deviceSearchDTO.getCondition() != null && !StringUtils.isEmpty(deviceSearchDTO.getCondition())) {
                    if (device.getDevice_meta().getDevice_name().contains(deviceSearchDTO.getCondition()) || device.getDevice_id().contains(deviceSearchDTO.getCondition())) {
                        deviceInfos.add(device);
                    }
                } else {
                    deviceInfos.add(device);
                }
            });
        } else {
            return deviceInfoList;
        }
        return deviceInfos;
    }


    public String getDeviceNameById(String deviceId) {
        if (deviceInfoList != null && deviceInfoList.size() > 0) {
            for (DeviceInfo device : deviceInfoList) {
                if (device.getDevice_id().equals(deviceId)) {
                    return device.getDevice_meta().getDevice_name();
                }
            }
        }
        return null;
    }


}
