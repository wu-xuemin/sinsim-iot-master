package com.eservice.sinsimiot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.sinsimiot.common.ResponseModel;
import com.eservice.sinsimiot.core.ParkException;
import com.eservice.sinsimiot.core.ResponseCode;
import com.eservice.sinsimiot.model.park.Staff;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author HT
 */
@Component
@Slf4j
public class StaffService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${park.ip}")
    private String PARK_BASE_URL;
    /**
     * Token
     */
    private String token;
    /**
     * 员工列表
     */
    private ArrayList<Staff> staffList = new ArrayList<>();
    @Autowired
    private TokenService tokenService;
    private Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 每分钟获取一次需要签到的员工信息
     */
    @Scheduled(initialDelay = 1000, fixedRate = 1000 * 60 * 3)
    public void fetchStaffScheduled() {
        if (token == null && tokenService != null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.ACCEPT, "application/json");
            headers.add("Authorization", token);
            HttpEntity entity = new HttpEntity(headers);
            try {
                String url = PARK_BASE_URL + "/staffs?page=0&size=0";
                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                    String body = responseEntity.getBody();
                    if (body != null) {
                        processStaffResponse(body);
                    } else {
                        fetchStaffScheduled();
                    }
                }
            } catch (HttpClientErrorException exception) {
                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    token = tokenService.getToken();
                    if (token != null) {
                        fetchStaffScheduled();
                    }
                }
            }
        }
    }


    private void processStaffResponse(String body) {
        ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
        if (responseModel != null && responseModel.getResult() != null) {
            ArrayList<Staff> tmpList = (ArrayList<Staff>) JSONArray.parseArray(responseModel.getResult(), Staff.class);
            if (tmpList != null && tmpList.size() != 0) {
                staffList = tmpList;
                log.info("staff : {} ", staffList.size());
            }
        }
    }


    public ArrayList<Staff> getStaffList() {
        return staffList;
    }

    public Staff createStaff(Staff staff) throws ParkException {
        if (tokenService != null) {
            if (token == null) {
                token = tokenService.getToken();
            }
            if (token != null) {
                HashMap<String, Object> postParameters = new HashMap<>();
                ArrayList<Staff> staffList = new ArrayList<>();
                staffList.add(staff);
                postParameters.put("staff_list", staffList);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, tokenService.getToken());
                HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
                try {
                    ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/staffs", httpEntity, String.class);
                    log.info("create staff park result : {}", responseEntity);
                    if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                        ResponseModel responseModel = JSONObject.parseObject(responseEntity.getBody(), ResponseModel.class);
                        if (responseModel != null) {
                            List<ResponseModel> responseModels = JSONObject.parseArray(responseModel.getResult(), ResponseModel.class);
                            ResponseModel responseModel1 = responseModels.get(0);
                            if (responseModel1 != null && responseModel1.getRtn() == 0) {
                                try {
                                    Staff staffAdd = JSONObject.parseObject(responseModel1.getResult(), Staff.class);
                                    //新增新员工数据
                                    this.staffList.add(staffAdd);
                                    log.info("method : [createStaff]  add person : {} to YT park success", staff.getPerson_information().getName());
                                    return staffAdd;
                                } catch (Exception e) {
                                    log.warn("JSON to Object failed. ===> {} ", e.getMessage());
                                }
                                fetchStaffScheduled();
                                return staff;
                            } else {
                                log.warn("createStaff failed. ===> {} ", responseModel1.getMessage());
                                throw new ParkException(responseModel1.getRtn(), responseModel1.getMessage());
                            }
                        }
//                        else if (responseModel.getRtn() == ResponseCode.YT_IMAGE_FAIL || responseModel.getRtn() == ResponseCode.IMAGE_INVALID || responseModel.getRtn() == ResponseCode.YT_NO_FACE) {
//                            log.warn("createStaff failed. ===> {} ", responseEntity.getBody());
//                            throw new ParkException(responseModel.getRtn(), responseModel.getMessage());
//                        }
                    }
                } catch (HttpClientErrorException exception) {
                    if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                        token = tokenService.getToken();
                        if (token != null) {
                            return createStaff(staff);
                        }
                    }
                }
            } else {
                log.error("Token is null, create staff error!");
            }
        }
        return staff;
    }

    public int updateStaff(Staff staff) throws ParkException {
        if (tokenService != null) {
            if (token == null) {
                token = tokenService.getToken();
            }
            if (token != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, token);
                HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(staff), headers);
                try {
                    ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/staffs/" + staff.getStaff_id(), HttpMethod.PUT, httpEntity, String.class);
                    log.info("修改人员接口返回值: {}", responseEntity);
                    if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                        ResponseModel responseModel = JSONObject.parseObject(responseEntity.getBody(), ResponseModel.class);
                        if (responseModel != null && responseModel.getRtn() == 0) {
                            try {
                                Staff staffUpdate = JSONObject.parseObject(responseModel.getResult(), Staff.class);
                                //移除修改前的员工数据
                                this.staffList.remove(staff);
                                //添加修改后的员工数据
                                this.staffList.add(staffUpdate);
                                return responseModel.getRtn();
                            } catch (Exception e) {
                                log.warn("JSON to Object failed. ===> {} ", e.getMessage());
                            }
                            fetchStaffScheduled();
                            throw new ParkException(responseModel.getRtn(), responseModel.getMessage());
                        } else if (responseModel.getRtn() == ResponseCode.YT_IMAGE_FAIL || responseModel.getRtn() == ResponseCode.IMAGE_INVALID) {
                            log.warn("updateStaff failed. ===> {} ", responseEntity.getBody());
                            throw new ParkException(responseModel.getRtn(), responseModel.getMessage());
                        }
                    }
                } catch (HttpClientErrorException exception) {
                    if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                        token = tokenService.getToken();
                        if (token != null) {
                            return updateStaff(staff);
                        }
                    }
                }
            } else {
                log.error("Token is null, update staff error!");
            }
        }
        return ResponseCode.YT_RESULT_FAIL;
    }

    public boolean deleteStaff(String staffId) {
        log.info("start delete staff :{}", staffId);
        if (tokenService != null) {
            if (token == null) {
                token = tokenService.getToken();
            }
            if (token != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.ACCEPT, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, token);
                HttpEntity entity = new HttpEntity(headers);
                try {
                    ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/staffs/" + staffId, HttpMethod.DELETE, entity, String.class);
                    log.info("delete staff :{} park result :{}", staffId, responseEntity);
                    if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                        ResponseModel responseModel = JSONObject.parseObject(responseEntity.getBody(), ResponseModel.class);
                        if (responseModel != null && responseModel.getRtn() == 0) {
                            fetchStaffScheduled();
                            return true;
                        }
                    }
                } catch (HttpClientErrorException exception) {
                    if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                        token = tokenService.getToken();
                        if (token != null) {
                            return deleteStaff(staffId);
                        }
                    }
                }
            }
        }
        return false;
    }

    public Staff isExist(String jobNo) {
        if (staffList != null && staffList.size() > 0) {
            for (Staff staff : staffList) {
                if (jobNo.equals(staff.getPerson_information().getId())) {
                    return staff;
                }
            }
        }
        return null;
    }

    /**
     * 临时测试
     *
     * @param empNo
     * @return
     */
    public Staff getStaffByEmpNo(String empNo) {
        if (staffList != null && staffList.size() > 0) {
            for (Staff staff : staffList) {
                if (empNo.equals(staff.getPerson_information().getId())) {
                    return staff;
                }
            }
        }
        return null;
    }

    /**
     * 根据staff_id获取员工信息
     *
     * @param id
     * @return
     */
    public Staff getStaffById(String id) {
        if (staffList != null && staffList.size() > 0) {
            for (Staff staff : staffList) {
                if (id.equals(staff.getStaff_id())) {
                    return staff;
                }
            }
        }
        return null;
    }


}
