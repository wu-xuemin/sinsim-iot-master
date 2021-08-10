package com.eservice.sinsimiot.service.park;

import com.alibaba.fastjson.JSON;
import com.eservice.sinsimiot.common.RedisService;
import com.eservice.sinsimiot.model.staff.StaffInfo;
import com.eservice.sinsimiot.service.*;
import com.google.common.base.Joiner;
import com.eservice.sinsimiot.model.attendance_config.AttendanceConfig;
import com.eservice.sinsimiot.model.attendance_record.AttendanceRecord;
import com.eservice.sinsimiot.model.park.AccessRecord;
import com.eservice.sinsimiot.model.park_info.ParkInfo;
///import com.hankun.master.service.*;
import com.eservice.sinsimiot.util.DateUtil;
import com.eservice.sinsimiot.util.Util;
import com.eservice.sinsimiot.util.ValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program:jili
 * @author:Mr.xie
 * @create:2020/8/31 17:47
 */
@Service
@Slf4j
public class AccessService {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RedisService redisService;
    @Resource
    private AttendanceRecordService attendanceRecordService;
    @Resource
    private ParkInfoService parkInfoService;
    @Resource
    private StaffInfoService staffInfoService;
    @Resource
    private TagInfoService tagInfoService;
//    @Resource
//    private TagService tagService;
    @Resource
    private AttendanceConfigService attendanceConfigService;

    @Value("${park.ip}")
    private String PARK_BASE_URL;

    private String token;

    /**
     * 数据在处理时，不进行新的查询
     */
    private boolean CHECK = false;


    private static Map<String, String> RECORDS = new HashMap<>();

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");


    /**
     * 查询开始时间,单位为秒
     */
    private Long queryStartTime = 0L;

    private String TIME_KEY = "_queryStartTime";

    @Scheduled(initialDelay = 2000, fixedDelay = 1000 * 10)
    public void fetchAccessRecord() {
//        if (!CHECK) {
//            if (queryStartTime == 0L) {
//                queryStartTime = redisService.get(TIME_KEY) == null ? Util.getDateStartTime().getTime() / 1000L : (long) redisService.get(TIME_KEY);
//            }
//            log.info("queryStartTime : {}", queryStartTime);
//            long queryEndTime = System.currentTimeMillis() / 1000L;
//            log.info("queryEndTime : {}", queryEndTime);
//            querySignRecord(queryStartTime, queryEndTime);
//        }
    }
//
//    public void querySignRecord(Long startTime, Long endTime) {
//        if (token == null && tokenService != null) {
//            token = tokenService.getToken();
//        }
//        try {
//            if (token != null) {
//                HttpHeaders headers = new HttpHeaders();
//                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
//                headers.add(HttpHeaders.AUTHORIZATION, token);
//                HashMap<String, Object> postParameters = new HashMap<>();
//                postParameters.put("start_timestamp", startTime);
//                postParameters.put("end_timestamp", endTime);
//                ArrayList<String> identity = new ArrayList<>();
//                identity.add(Constant.STAFF);
//                postParameters.put("identity_list", identity);
//                List<String> pass = new ArrayList<>();
//                pass.add("PASS");
//                pass.add("CARD_PASS");
//                postParameters.put("pass_result_list", pass);
//                HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
//                log.info("request park visit_record/query  : {}", JSON.toJSONString(postParameters));
//                ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/access/record", httpEntity, String.class);
//                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
//                    String body = responseEntity.getBody();
//                    if (body != null) {
//                        log.info("park visit_record/query  result :{}", body);
//                        ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
//                        if (responseModel != null && responseModel.getResult() != null) {
//                            List<AccessRecord> accessRecords = JSONArray.parseArray(responseModel.getResult(), AccessRecord.class);
//                            if (accessRecords != null && accessRecords.size() > 0) {
//                                //往前移五分钟
//                                queryStartTime = endTime - 300;
//                                redisService.set(TIME_KEY, queryStartTime);
//                                processAccessRecordResponse(accessRecords);
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (HttpClientErrorException e) {
//            log.error("fetchAccessRecord is error :{}", e.getMessage());
//            if (e.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
//                token = tokenService.getToken();
//                if (token != null) {
//                    fetchAccessRecord();
//                }
//            }
//        }
//    }

    public void processAccessRecordResponse(List<AccessRecord> accessRecords) {
        Collections.reverse(accessRecords);
        CHECK = true;
        List<AttendanceConfig> attendanceConfigs = attendanceConfigService.findAll();
        for (AccessRecord accessRecord : accessRecords) {
            String signTime = simpleDateFormat.format(new Date(accessRecord.getTimestamp() * 1000));
            String nowTime = signTime.split(" ")[1];
            try {
                if (attendanceConfigs != null) {
                    boolean valid = ValidateUtil.isTimeRange(nowTime, attendanceConfigs.get(0).getStartTime(), attendanceConfigs.get(0).getEndTime());
                    if (valid) {
                        recordToAttendance(accessRecord, signTime);
                    } else {
                        log.info("signTime : {} is failure", signTime);
                    }
                } else {
                    log.warn("not foun attendance config");
                    //没有配置考勤有效时间段
                    recordToAttendance(accessRecord, signTime);
                }
            } catch (Exception e) {
                CHECK = false;
                e.printStackTrace();
            }
        }
        CHECK = false;
    }

    public void recordToAttendance(AccessRecord accessRecord, String signTime) {
        String recordTime = RECORDS.get(accessRecord.getId());
        if (recordTime != null && recordTime.equals(signTime)) {
            return;
        }
        log.info("staff:{} sign time:{}", accessRecord.getPerson().getPerson_information().getName(), signTime);
        String deviceName = deviceService.getDeviceNameById(accessRecord.getDevice_id());
        String time = sdf.format(new Date(accessRecord.getTimestamp() * 1000));
        StaffInfo staffInfo = staffInfoService.findById(accessRecord.getPerson().getPerson_id());
        if (staffInfo != null) {
            ParkInfo parkInfo = parkInfoService.findById(staffInfo.getParkId());
            AttendanceRecord attendanceRecord = attendanceRecordService.fetchStaffAttendance(staffInfo.getStaffId(), time);
            List<String> tagNames = tagInfoService.selectTagNameByTagId(Util.stringToArrayList(staffInfo.getTagIds(), ","));
            if (attendanceRecord == null) {
                RECORDS.put(accessRecord.getId(), signTime);
                log.info("staff: {} not sign-in today ,start added sign-in time:{}", staffInfo.getName(), signTime);
                attendanceRecord = new AttendanceRecord();
                attendanceRecord.setSignDeviceName(deviceName);
                attendanceRecord.setSignTime(signTime);
                attendanceRecord.setStaffId(staffInfo.getStaffId());
                attendanceRecord.setStaffName(staffInfo.getName());
                attendanceRecord.setStaffNumber(staffInfo.getStaffNumber());
                attendanceRecord.setStaffParkId(parkInfo.getParkId());
                attendanceRecord.setStaffParkName(parkInfo.getParkName());
                attendanceRecord.setStaffTagName(Joiner.on(",").join(tagNames));
                attendanceRecord.setStaffTagId(staffInfo.getTagIds());
                if (attendanceRecordService.save(attendanceRecord)) {
                    log.info("staff : {} attendance sign time save success", staffInfo.getName());
                }
            } else {
                RECORDS.put(accessRecord.getId(), signTime);
                log.info("staff : {} have sign-in time, added sign-out time : {}", staffInfo.getName(), signTime);
                if (attendanceRecord.getSignOutTime() != null) {
                    Long singOutime = DateUtil.stringToDate(attendanceRecord.getSignOutTime(), "yyyy-MM-dd HH:mm:ss").getTime() / 1000L;
                    //防止刷脸数据中间有漏，后面重推，数据回来后，刷脸时间却小于数据库中已记录的时间
                    if (accessRecord.getTimestamp() > singOutime) {
                        attendanceRecord.setSignOutDeviceName(deviceName);
                        attendanceRecord.setSignOutTime(signTime);
                        if (attendanceRecordService.update(attendanceRecord)) {
                            log.info("staff : {} attendance sign out time save success", staffInfo.getName());
                        }
                    } else {
                        //数据库记录的签到时间
                        Long morningSignTime = DateUtil.stringToDate(attendanceRecord.getSignTime(), "yyyy-MM-dd HH:mm:ss").getTime() / 1000L;
                        if (accessRecord.getTimestamp() < morningSignTime) {
                            attendanceRecord.setSignDeviceName(deviceName);
                            attendanceRecord.setSignTime(signTime);
                            if (attendanceRecordService.update(attendanceRecord)) {
                                log.info("staff : {} attendance sign time:{} < database signTime:{}", staffInfo.getName(), morningSignTime);
                            }
                        }
                        log.warn("record time  :{} < database signOuttime: {} not update", signTime, attendanceRecord.getSignOutTime());
                    }
                } else {
                    log.info("staff: {} signTime : {} ，attendance record signTime : {}", attendanceRecord.getStaffName(), signTime, attendanceRecord.getSignTime());
                    Long staffSignTime = DateUtil.stringToDate(attendanceRecord.getSignTime(), "yyyy-MM-dd HH:mm:ss").getTime() / 1000L;
                    if (accessRecord.getTimestamp() < staffSignTime) {
                        attendanceRecord.setSignOutTime(attendanceRecord.getSignTime());
                        attendanceRecord.setSignOutDeviceName(attendanceRecord.getSignDeviceName());
                        attendanceRecord.setSignTime(signTime);
                        attendanceRecord.setSignDeviceName(deviceName);
                    } else if (signTime.equals(attendanceRecord.getSignTime())) {
                        log.info("staff : {} sign out time ==  signTime: {}", accessRecord.getPerson().getPerson_information().getName(), attendanceRecord.getSignTime());
                        return;
                    } else {
                        log.info("staff :{} is not exist signOutime, save signOutTime:{}", JSON.toJSONString(accessRecord), signTime);
                        attendanceRecord.setSignOutTime(signTime);
                        attendanceRecord.setSignOutDeviceName(deviceName);
                    }
                    if (attendanceRecordService.update(attendanceRecord)) {
                        log.info("staff : {} attendance sign out time save success", staffInfo.getName());
                    }
                }
            }
        } else {
            log.error("staff : {} staff_num :{} id:{} is not exit database", accessRecord.getPerson().getPerson_information().getName(), accessRecord.getPerson().getPerson_information().getId(), accessRecord.getPerson().getPerson_id());
        }
    }

//
//    public List<AccessRecord> queryAccrecord(Long startTime, Long endTime, String parkId, String staffNum) {
//        if (token == null) {
//            token = tokenService.getToken();
//        }
//        if (token != null) {
//            HashMap<String, Object> postParameters = new HashMap<>(3);
//            postParameters.put("start_timestamp", startTime);
//            postParameters.put("end_timestamp", endTime);
//            List<String> identityList = new ArrayList<>();
//            List<String> passResults = new ArrayList<>();
//            identityList.add(Constant.STAFF);
//            passResults.add("PASS");
//            passResults.add("CARD_PASS");
//            postParameters.put("identity_list", identityList);
//            postParameters.put("pass_result_list", passResults);
//            if (parkId != null) {
//                ParkInfo parkInfo = parkInfoService.findById(parkId);
//                if (parkInfo != null) {
//                    List<String> tagIds = tagService.getTagIds(Util.stringToArrayList(parkInfo.getParkName(), ","));
//                    postParameters.put("tag_id_list", tagIds);
//                }
//            }
//            if (staffNum != null) {
//                StaffInfo staffInfo = staffInfoService.findBy("staffNumber", staffNum);
//                if (staffInfo != null) {
//                    postParameters.put("person_id_list", Util.stringToArrayList(staffInfo.getStaffNumber(), ","));
//                }
//            }
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
//            headers.add(HttpHeaders.AUTHORIZATION, token);
//            HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
//            try {
//                log.info("method : [queryAccrecord]  查询 /access/record 接口入参: {}", JSON.toJSONString(postParameters));
//                ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/access/record", httpEntity, String.class);
//                if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
//                    String body = responseEntity.getBody();
//                    if (body != null) {
//                        ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
//                        if (responseModel != null && responseModel.getResult() != null) {
//                            ArrayList<AccessRecord> tempList = (ArrayList<AccessRecord>) JSONArray.parseArray(responseModel.getResult(), AccessRecord.class);
//                            if (tempList != null && tempList.size() > 0) {
//                                log.info("method : [queryAccrecord] result tempList : {}", tempList.size());
//                                return tempList;
//                            }
//                        }
//                    }
//                }
//            } catch (HttpClientErrorException exception) {
//                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
//                    //token失效,重新获取token后再进行数据请求
//                    token = tokenService.getToken();
//                    return queryAccrecord(startTime, endTime, parkId, staffNum);
//                }
//                exception.printStackTrace();
//                log.error("HttpClientErrorException  : {} ", exception.getMessage());
//            }
//        }
//        return null;
//    }


    @Scheduled(cron = "00 59 23 * * ?")
    public void checkAttendance() {
//        log.info("start checkAttendance......");
//        Long startTime = Util.getThreeDaysAgoDateStartTime().getTime() / 1000L;
//        Long endTime = Util.getDateEndTime().getTime() / 1000L;
//        List<AccessRecord> accessRecords = queryAccrecord(startTime, endTime, null, null);
//        if (accessRecords != null && accessRecords.size() > 0) {
//            processAccessRecordResponse(accessRecords);
//        }
    }

    @Scheduled(cron = "00 00 00 * * ?")
    public void clearRecord() {
        if (RECORDS.size() > 0) {
            RECORDS.clear();
        }
    }


}
