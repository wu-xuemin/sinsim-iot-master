package com.eservice.sinsimiot.service.impl;

import com.alibaba.fastjson.JSON;
import com.eservice.sinsimiot.core.Constant;
import com.eservice.sinsimiot.model.staff.StaffInfo;
import com.eservice.sinsimiot.service.ParkInfoService;
import com.eservice.sinsimiot.service.TagInfoService;
import com.eservice.sinsimiot.util.ExcelUtil;
import com.google.common.base.Joiner;
import com.eservice.sinsimiot.common.AbstractServiceImpl;
import com.eservice.sinsimiot.dao.AttendanceRecordMapper;
import com.eservice.sinsimiot.model.attendance_record.AttendanceDTO;
import com.eservice.sinsimiot.model.attendance_record.AttendanceRecord;
import com.eservice.sinsimiot.model.attendance_record.AttendanceRecordDTO;
import com.eservice.sinsimiot.model.park_info.ParkInfo;
import com.eservice.sinsimiot.model.staff.StaffSearchDTO;
import com.eservice.sinsimiot.model.tag.TagInfo;
import com.eservice.sinsimiot.service.AttendanceRecordService;
import com.eservice.sinsimiot.service.StaffInfoService;
import com.eservice.sinsimiot.util.DateUtil;
import com.eservice.sinsimiot.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Class Description: xxx
 *
 * @author Mr.xie
 * @date 2021/05/08.
 */
@Service
@Transactional
@Slf4j
public class AttendanceRecordServiceImpl extends AbstractServiceImpl<AttendanceRecord> implements AttendanceRecordService {
    @Resource
    private AttendanceRecordMapper attendanceRecordMapper;
    @Value("${path.excel}")
    private String excelPath;
    @Value("${url.excel}")
    private String excelUrl;
    @Resource
    private StaffInfoService staffInfoService;
    @Resource
    private ParkInfoService parkInfoService;
    @Resource
    private TagInfoService tagInfoService;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean save(AttendanceRecord model) {
        model.setRecordId(UUID.randomUUID().toString());
        model.setCreateTime(System.currentTimeMillis() / 1000L);
        if (attendanceRecordMapper.insertSelective(model) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(AttendanceRecord model) {
        model.setUpdateTime(System.currentTimeMillis() / 1000L);
        if (attendanceRecordMapper.updateByPrimaryKeySelective(model) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public AttendanceRecord fetchStaffAttendance(String staffId, String signTime) {
        return attendanceRecordMapper.fetchStaffAttendance(staffId, signTime);
    }


    @Override
    public List<AttendanceRecord> searchAttendance(AttendanceDTO attendanceDTO) {
        return attendanceRecordMapper.searchAttendance(attendanceDTO);
    }

    @Override
    public List<AttendanceRecord> appSearchAttendance(AttendanceDTO attendanceDTO) {
        return attendanceRecordMapper.appSearchAttendance(attendanceDTO);
    }

    @Override
    public String export(AttendanceDTO attendanceDTO) {
        List<AttendanceRecord> attendanceRecords = attendanceRecordMapper.searchAttendance(attendanceDTO);
        if (attendanceRecords != null && attendanceRecords.size() > 0) {
            ArrayList<LinkedHashMap> maps = new ArrayList<>();
            attendanceRecords.forEach(record -> {
                LinkedHashMap map = new LinkedHashMap();
                map.put("staffName", record.getStaffName());
                map.put("staffNumber", record.getStaffNumber());
                map.put("staffParkName", record.getStaffParkName());
                map.put("staffTagName", record.getStaffTagName());
                map.put("signDeviceName", record.getSignDeviceName());
                map.put("signTime", record.getSignTime());
                map.put("signOutDeviceName", record.getSignOutDeviceName());
                map.put("signOutTime", record.getSignOutTime());
                if (record.getType() == Constant.RECORD_TYPE) {
                    map.put("type", "刷脸");
                } else {
                    map.put("type", "补签");
                }
                maps.add(map);
            });
            if (maps != null && maps.size() > 0) {
                String[] header = new String[]{"员工姓名", "员工工号", "所属园区", "单位", "签到设备", "签到时间", "签退设备", "签退时间", "状态"};
                String fileName = String.format("考勤信息-%s.xlsx", sdf.format(new Date()));
                try {
                    if (fileName.equals(ExcelUtil.insertDataInSheet(maps, header, excelPath, fileName))) {
                        return excelUrl + fileName;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public List<AttendanceRecordDTO> countAttendance(AttendanceDTO attendanceDTO) {
        List<AttendanceRecordDTO> attendanceRecords = attendanceRecordMapper.countAttendance(attendanceDTO);
        log.info("staff attendance record size : {}", attendanceRecords.size());
        //根据工号保存每位员工的考勤数据
        Map<String, List<AttendanceRecordDTO>> staffAttendanceMap = new HashMap<>();
        List<StaffInfo> staffNums = staffInfoService.fetchStaffNums(attendanceDTO);
        log.info("StaffNums size : {}", staffNums.size());
        try {
            if (attendanceRecords != null && attendanceRecords.size() > 0) {
                if (staffNums != null && staffNums.size() > 0) {
                    staffNums.forEach(staffNum -> {
                        List<AttendanceRecordDTO> staffRecords = new ArrayList<>();
                        for (AttendanceRecordDTO record : attendanceRecords) {
                            if (staffNum.getStaffNumber().equals(record.getStaffNumber())) {
                                staffRecords.add(record);
                            }
                        }
                        if (staffRecords.size() > 0) {
                            staffAttendanceMap.put(staffNum.getStaffNumber(), staffRecords);
                        }
                    });
                }
                if (staffAttendanceMap.size() > 0) {
                    Date beginTime = sdf.parse(attendanceDTO.getSignTime());
                    Date endTime = sdf.parse(attendanceDTO.getSignOutTime());
                    //计算出时间区间中的每一天
                    List<String> dayList = Util.getTimeInterval(beginTime, endTime);
                    //保存每位员工的统计数据
                    List<AttendanceRecordDTO> recordDTOList = new ArrayList<>();
                    for (Map.Entry<String, List<AttendanceRecordDTO>> map : staffAttendanceMap.entrySet()) {
                        List<AttendanceRecordDTO> recordDTOS = map.getValue();
                        int normal = 0;
                        int abNormal = 0;
                        float averageTime = 0.0F;
                        float countTime = 0.0F;
                        AttendanceRecordDTO attendanceRecordDTO = recordDTOS.get(0);
                        StaffInfo staffInfo = staffInfoService.findById(recordDTOS.get(0).getStaffId());
                        if (staffInfo == null) {
                            attendanceRecordDTO.setValid(false);
                        } else {
                            attendanceRecordDTO.setValid(true);
                        }
                        //统计异常数据
                        for (String day : dayList) {
                            boolean isExist = false;
                            for (AttendanceRecordDTO recordDTO : recordDTOS) {
                                if (recordDTO.getSignTime().contains(day)) {
                                    if (StringUtils.isEmpty(recordDTO.getSignOutTime())) {
                                        if (recordDTO.getStatus() == null || !recordDTO.getStatus().equals(Constant.ATTENDANCE_ABNORMAL)) {
                                            recordDTO.setStatus(Constant.ATTENDANCE_ABNORMAL);
                                            update(recordDTO);
                                        }
                                        continue;
                                    } else {
                                        isExist = true;
                                    }
                                }
                            }
                            if (!isExist) {
                                abNormal++;
                            }
                        }
                        //遍历员工的考勤数据，并且做统计
                        for (AttendanceRecordDTO record : recordDTOS) {
                            //过滤异常数据
                            if (!StringUtils.isEmpty(record.getSignTime()) && !StringUtils.isEmpty(record.getSignOutTime())) {
                                normal++;
                                try {
                                    if (record.getStatus() == null || !record.getStatus().equals(Constant.ATTENDANCE_NORMAL)) {
                                        record.setStatus(Constant.ATTENDANCE_NORMAL);
                                        update(record);
                                    }
                                    long signTime = DateUtil.stringToDate(record.getSignTime(), "yyyy-MM-dd HH:mm:ss").getTime() / 1000L;
                                    long signOutTime = DateUtil.stringToDate(record.getSignOutTime(), "yyyy-MM-dd HH:mm:ss").getTime() / 1000L;
                                    //计算当天工作时间，然后汇总
                                    long todayWorkTime = signOutTime - signTime;
                                    double time = todayWorkTime / 3600.0;
                                    //转化为小时，精确到一位小数
                                    float lastTime = Float.valueOf(String.format("%.1f", time));
                                    countTime = Float.valueOf(String.format("%.1f", countTime + lastTime));
                                } catch (Exception e) {
                                    log.error("sginTime:{},signOutime: {}", record.getSignTime(), record.getSignOutTime());
                                    e.printStackTrace();
                                }
                            }
                        }
                        //计算平均时长
                        if (normal != 0) {
                            averageTime = Float.valueOf(String.format("%.1f", countTime / normal));
                            attendanceRecordDTO.setAverageTime(averageTime);
                        } else {
                            attendanceRecordDTO.setAverageTime(averageTime);
                        }
                        attendanceRecordDTO.setSignDeviceName(null);
                        attendanceRecordDTO.setSignOutDeviceName(null);
                        attendanceRecordDTO.setSignTime(null);
                        attendanceRecordDTO.setSignOutTime(null);
                        attendanceRecordDTO.setCreateTime(null);
                        attendanceRecordDTO.setUpdateTime(null);
                        attendanceRecordDTO.setNormal(normal);
                        attendanceRecordDTO.setAbnormal(abNormal);
                        attendanceRecordDTO.setCountTime(countTime);
                        recordDTOList.add(attendanceRecordDTO);
                    }
                    List<String> notAttendanceStaffNums = new ArrayList<>();
                    for (StaffInfo staff : staffNums) {
                        if (staffAttendanceMap.get(staff.getStaffNumber()) == null) {
                            notAttendanceStaffNums.add(staff.getStaffNumber());
                        }
                    }
                    if (notAttendanceStaffNums.size() > 0) {
                        notAttendanceStaffNums.forEach(staffNum -> {
                            StaffInfo staffInfo = staffInfoService.findBy("staffNumber", staffNum);
                            if (staffInfo != null) {
                                AttendanceRecordDTO attendanceRecordDTO = new AttendanceRecordDTO();
                                ParkInfo parkInfo = parkInfoService.findById(staffInfo.getParkId());
                                attendanceRecordDTO.setStaffName(staffInfo.getName());
                                attendanceRecordDTO.setStaffNumber(staffInfo.getStaffNumber());
                                attendanceRecordDTO.setStaffParkId(staffInfo.getParkId());
                                attendanceRecordDTO.setStaffParkName(parkInfo.getParkName());
                                List<String> tagInfos = Arrays.asList(staffInfo.getTagIds().split(","));
                                attendanceRecordDTO.setStaffTagId(staffInfo.getTagIds());
                                List<String> tagNames = tagInfoService.selectTagNameByTagId(tagInfos);
                                attendanceRecordDTO.setStaffTagName(Joiner.on(",").join(tagNames));
                                attendanceRecordDTO.setNormal(0);
                                attendanceRecordDTO.setAbnormal(0);
                                attendanceRecordDTO.setAverageTime(0F);
                                attendanceRecordDTO.setCountTime(0F);
                                attendanceRecordDTO.setValid(true);
                                recordDTOList.add(attendanceRecordDTO);
                            }
                        });
                    }
                    return recordDTOList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("countAttendance exception ");
        }
        return null;
    }


    /**
     * 统计员工在某个时间段内的每天考勤数据
     *
     * @param attendanceDTO
     * @return
     */
    @Override
    public List<AttendanceRecordDTO> fetchAttendanceRecordDTODetail(AttendanceDTO attendanceDTO) {
        List<AttendanceRecordDTO> attendanceRecordDTOList = attendanceRecordMapper.countAttendance(attendanceDTO);
        if (attendanceDTO.getSignTime() != null && attendanceDTO.getSignOutTime() != null) {
            try {
                Map<String, AttendanceRecordDTO> recordDTOMap = new HashMap<>();
                List<AttendanceRecordDTO> attendanceRecordDTOS = new ArrayList<>();
                Date beginTime = sdf.parse(attendanceDTO.getSignTime());
                Date endTime = sdf.parse(attendanceDTO.getSignOutTime());
                //计算出时间区间中的每一天
                List<String> dayList = Util.getTimeInterval(beginTime, endTime);
                if (dayList != null && dayList.size() > 0) {
                    log.info("day list :{}", JSON.toJSONString(dayList));
                    //以 天 为key 保存当天员工的考勤数据
                    for (String day : dayList) {
                        for (AttendanceRecordDTO attendance : attendanceRecordDTOList) {
                            if (attendance.getSignTime().contains(day)) {
                                recordDTOMap.put(day, attendance);
                            }
                        }
                    }

                    for (String time : dayList) {
                        //attendanceRecordDTO: 人员考勤对象
                        AttendanceRecordDTO attendanceRecordDTO = recordDTOMap.get(time);
                        if (attendanceRecordDTO != null) {
                            attendanceRecordDTO.setDay(time);
                            if (!StringUtils.isEmpty(attendanceRecordDTO.getSignTime()) && !StringUtils.isEmpty(attendanceRecordDTO.getSignOutTime())) {
                                long signTime = simpleDateFormat.parse(attendanceRecordDTO.getSignTime()).getTime() / 1000L;
                                long signOutTime = simpleDateFormat.parse(attendanceRecordDTO.getSignOutTime()).getTime() / 1000L;
                                float todayWorkTime = signOutTime - signTime;
                                attendanceRecordDTO.setCountTime((float) (Math.round((todayWorkTime / 3600) * 10)) / 10);
                                attendanceRecordDTO.setStatus(Constant.ATTENDANCE_NORMAL);
                                attendanceRecordDTOS.add(attendanceRecordDTO);
                            } else {
                                attendanceRecordDTO.setCountTime(0F);
                                attendanceRecordDTO.setStatus(Constant.ATTENDANCE_ABNORMAL);
                                attendanceRecordDTOS.add(attendanceRecordDTO);
                            }

                        } else {
                            attendanceRecordDTO = new AttendanceRecordDTO();
                            if (attendanceRecordDTOList != null && attendanceRecordDTOList.size() > 0) {
                                BeanUtils.copyProperties(attendanceRecordDTOList.get(0), attendanceRecordDTO);
                            } else {
                                StaffInfo staffInfo = staffInfoService.findBy("staffNumber", attendanceDTO.getStaffNum());
                                if (staffInfo != null) {
                                    ParkInfo parkInfo = parkInfoService.findById(staffInfo.getParkId());
                                    attendanceRecordDTO.setStaffName(staffInfo.getName());
                                    attendanceRecordDTO.setStaffNumber(staffInfo.getStaffNumber());
                                    attendanceRecordDTO.setStaffParkId(staffInfo.getParkId());
                                    attendanceRecordDTO.setStaffParkName(parkInfo.getParkName());
                                    List<String> tagInfos = Arrays.asList(staffInfo.getTagIds().split(","));
                                    attendanceRecordDTO.setStaffTagId(staffInfo.getTagIds());
                                    List<String> tagNames = tagInfoService.selectTagNameByTagId(tagInfos);
                                    attendanceRecordDTO.setStaffTagName(Joiner.on(",").join(tagNames));
                                }
                            }
                            attendanceRecordDTO.setDay(time);
                            attendanceRecordDTO.setSignTime("--");
                            attendanceRecordDTO.setSignDeviceName("--");
                            attendanceRecordDTO.setSignOutTime("--");
                            attendanceRecordDTO.setSignOutDeviceName("--");
                            attendanceRecordDTO.setCountTime(0F);
                            attendanceRecordDTO.setStatus(Constant.ATTENDANCE_ABNORMAL);
                            attendanceRecordDTOS.add(attendanceRecordDTO);
                        }
                    }
                    return attendanceRecordDTOS;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String exportAttendanceRecordDTODetail(AttendanceDTO attendanceDTO) {
        List<AttendanceRecordDTO> attendanceRecordDTOList = attendanceRecordMapper.countAttendance(attendanceDTO);
        List<StaffInfo> staffNums = staffInfoService.fetchStaffNums(attendanceDTO);
        try {
            if (attendanceDTO.getSignTime() == null && attendanceDTO.getSignOutTime() == null) {
                return null;
            }
            Date beginTime = sdf.parse(attendanceDTO.getSignTime());
            Date endTime = sdf.parse(attendanceDTO.getSignOutTime());
            //计算出时间区间中的每一天
            List<String> dayList = Util.getTimeInterval(beginTime, endTime);
            //根据工号保存每位员工的考勤数据
            Map<String, List<AttendanceRecordDTO>> staffAttendanceMap = new HashMap<>();
            log.info("StaffNums size : {}", staffNums.size());
            if (attendanceRecordDTOList != null && attendanceRecordDTOList.size() > 0) {
                if (staffNums != null && staffNums.size() > 0) {
                    staffNums.forEach(staffNum -> {
                        List<AttendanceRecordDTO> staffRecords = new ArrayList<>();
                        for (AttendanceRecordDTO record : attendanceRecordDTOList) {
                            if (staffNum.getStaffNumber().equals(record.getStaffNumber())) {
                                staffRecords.add(record);
                            }
                        }
                        if (staffRecords.size() > 0) {
                            staffAttendanceMap.put(staffNum.getStaffNumber(), staffRecords);
                        }
                    });
                }
            }
            //保存每位员工的考勤详情
            List<AttendanceRecordDTO> staffAttendRecordList = new ArrayList<>();
            if (staffAttendanceMap.size() > 0) {
                for (Map.Entry<String, List<AttendanceRecordDTO>> map : staffAttendanceMap.entrySet()) {
                    List<AttendanceRecordDTO> recordDTOS = map.getValue();
                    //遍历每位员工的考勤数据，根据传进来的时间区间，进行统计 例： key:2021-05-01,value:attendanceRecord(一条考勤记录)
                    Map<String, AttendanceRecordDTO> recordDTOMap = new HashMap<>();
                    for (String day : dayList) {
                        for (AttendanceRecordDTO record : recordDTOS) {
                            if (record.getSignTime().contains(day)) {
                                recordDTOMap.put(day, record);
                            }
                        }
                    }
                    //再根据时间，来详细表明在时间区间内，那天有考勤，那天没考勤
                    for (String time : dayList) {
                        AttendanceRecordDTO attendanceRecordDTO = recordDTOMap.get(time);
                        if (attendanceRecordDTO != null) {
                            attendanceRecordDTO.setDay(time);
                            if (!StringUtils.isEmpty(attendanceRecordDTO.getSignTime()) && !StringUtils.isEmpty(attendanceRecordDTO.getSignOutTime())) {
                                long signTime = simpleDateFormat.parse(attendanceRecordDTO.getSignTime()).getTime() / 1000L;
                                long signOutTime = simpleDateFormat.parse(attendanceRecordDTO.getSignOutTime()).getTime() / 1000L;
                                float todayWorkTime = signOutTime - signTime;
                                attendanceRecordDTO.setCountTime((float) (Math.round((todayWorkTime / 3600) * 10)) / 10);
                            } else {
                                attendanceRecordDTO.setStatus(Constant.ATTENDANCE_ABNORMAL);
                            }
                            staffAttendRecordList.add(attendanceRecordDTO);
                        } else {
                            attendanceRecordDTO = new AttendanceRecordDTO();
                            BeanUtils.copyProperties(recordDTOS.get(0), attendanceRecordDTO);
                            attendanceRecordDTO.setDay(time);
                            attendanceRecordDTO.setSignTime("--");
                            attendanceRecordDTO.setSignDeviceName("--");
                            attendanceRecordDTO.setSignOutTime("--");
                            attendanceRecordDTO.setSignOutDeviceName("--");
                            attendanceRecordDTO.setCountTime(0F);
                            attendanceRecordDTO.setStatus(Constant.ATTENDANCE_ABNORMAL);
                            staffAttendRecordList.add(attendanceRecordDTO);
                        }
                    }
                }
            } else {
                StaffInfo staffInfo = staffInfoService.findBy("staffNumber", attendanceDTO.getStaffNum());
                if (staffInfo != null) {
                    for (String time : dayList) {
                        AttendanceRecordDTO attendanceRecordDTO = new AttendanceRecordDTO();
                        ParkInfo parkInfo = parkInfoService.findById(staffInfo.getParkId());
                        attendanceRecordDTO.setStaffName(staffInfo.getName());
                        attendanceRecordDTO.setStaffNumber(staffInfo.getStaffNumber());
                        attendanceRecordDTO.setStaffParkId(staffInfo.getParkId());
                        attendanceRecordDTO.setStaffParkName(parkInfo.getParkName());
                        List<String> tagInfos = Arrays.asList(staffInfo.getTagIds().split(","));
                        attendanceRecordDTO.setStaffTagId(staffInfo.getTagIds());
                        List<String> tagNames = tagInfoService.selectTagNameByTagId(tagInfos);
                        attendanceRecordDTO.setStaffTagName(Joiner.on(",").join(tagNames));
                        attendanceRecordDTO.setDay(time);
                        attendanceRecordDTO.setSignTime("--");
                        attendanceRecordDTO.setSignDeviceName("--");
                        attendanceRecordDTO.setSignOutTime("--");
                        attendanceRecordDTO.setSignOutDeviceName("--");
                        attendanceRecordDTO.setCountTime(0F);
                        attendanceRecordDTO.setStatus(Constant.ATTENDANCE_ABNORMAL);
                        attendanceRecordDTO.setValid(true);
                        staffAttendRecordList.add(attendanceRecordDTO);
                    }
                }
            }
            ArrayList<LinkedHashMap> maps = new ArrayList<>();
            if (staffAttendRecordList.size() > 0) {
                for (AttendanceRecordDTO record : staffAttendRecordList) {
                    LinkedHashMap map = new LinkedHashMap();
                    map.put("staffName", record.getStaffName());
                    map.put("staffNum", record.getStaffNumber());
                    map.put("parkName", record.getStaffParkName());
                    map.put("tagName", record.getStaffTagName());
                    map.put("day", record.getDay());
                    map.put("signDeviceName", record.getSignDeviceName());
                    map.put("signTime", record.getSignTime());
                    map.put("signOutDeviceName", record.getSignOutDeviceName());
                    map.put("signOutTime", record.getSignOutTime());
                    map.put("countTime", record.getCountTime());
                    if (record.getStatus().equals(Constant.ATTENDANCE_ABNORMAL)) {
                        map.put("status", "异常");
                    } else {
                        map.put("status", "正常");
                    }
                    maps.add(map);
                }
            }
            if (maps.size() > 0) {
                try {
                    String[] header = new String[]{"员工姓名", "员工工号", "所属园区", "单位", "日期", "签到设备", "上班时间", "签退设备", "下班时间", "总时长", "状态"};
                    String fileName = String.format("考勤详情-%s.xlsx", sdf.format(new Date()));
                    if (fileName.equals(ExcelUtil.insertDataInSheet(maps, header, excelPath, fileName))) {
                        return excelUrl + fileName;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String exportCounAttendance(List<AttendanceRecordDTO> attendanceRecordDTOS) {
        ArrayList<LinkedHashMap> maps = new ArrayList<>();
        attendanceRecordDTOS.forEach(record -> {
            LinkedHashMap map = new LinkedHashMap();
            map.put("staffName", record.getStaffName());
            map.put("staffNum", record.getStaffNumber());
            map.put("staffParkName", record.getStaffParkName());
            map.put("staffTagName", record.getStaffTagName());
            map.put("normal", record.getNormal());
            map.put("abnormal", record.getAbnormal());
            map.put("averageTime", record.getAverageTime());
            map.put("countTime", record.getCountTime());
            if (record.isValid()) {
                map.put("valid", "在职");
            } else {
                map.put("valid", "离职");
            }
            maps.add(map);
        });
        if (maps != null && maps.size() > 0) {
            String[] header = new String[]{"员工姓名", "员工工号", "所属园区", "单位", "正常天数", "异常天数", "平均时间", "总时长", "是否在职"};
            String fileName = String.format("考勤统计信息-%s.xlsx", sdf.format(new Date()));
            try {
                if (fileName.equals(ExcelUtil.insertDataInSheet(maps, header, excelPath, fileName))) {
                    return excelUrl + fileName;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 统计单位考勤数据
     * 整体逻辑，先查出本段时间所有园区信息， 然后遍历园区所有单位，统计在所传进来的时间段内的每一天考勤数据
     *
     * @param attendanceDTO
     * @return
     */
    @Override
    public List<AttendanceRecordDTO> countUnitReocord(AttendanceDTO attendanceDTO) {
        List<AttendanceRecordDTO> recordDTOList = attendanceRecordMapper.countAttendance(attendanceDTO);
        List<AttendanceRecordDTO> list = new ArrayList<>();
        if (attendanceDTO.getSignTime() != null && attendanceDTO.getSignOutTime() != null) {
            try {
                Date beginTime = sdf.parse(attendanceDTO.getSignTime());
                Date endTime = sdf.parse(attendanceDTO.getSignOutTime());
                //计算出时间区间中的每一天
                List<String> dayList = Util.getTimeInterval(beginTime, endTime);
                List<AttendanceRecord> parkIds = attendanceRecordMapper.fetchPark(attendanceDTO);
                if (parkIds != null && parkIds.size() > 0) {
                    parkIds.forEach(parkId -> {
                        //园区放在最上层
                        ParkInfo parkInfo = parkInfoService.findById(parkId.getStaffParkId());
                        if (parkInfo != null) {
                            //获取园区的单位
                            List<String> tagIds = Util.stringToArrayList(parkInfo.getUnit(), ",");
                            if (tagIds != null && tagIds.size() > 0) {
                                //开始遍历单位
                                for (String tagId : tagIds) {
                                    if (StringUtils.isEmpty(tagId)) {
                                        continue;
                                    }
                                    StaffSearchDTO staffSearchDTO = new StaffSearchDTO();
                                    staffSearchDTO.setParkId(parkId.getStaffParkId());
                                    List<String> tags = new ArrayList<>();
                                    tags.add(tagId);
                                    staffSearchDTO.setTagIds(tags);
                                    //汇总该单位在时间段内每天的数据统计
                                    for (String day : dayList) {
                                        Integer normal = 0;
                                        float countTime = 0F;
                                        boolean isVaild = false;
                                        AttendanceRecordDTO attendanceRecordDTO = new AttendanceRecordDTO();
                                        attendanceRecordDTO.setTotalStaff(staffInfoService.findByCondition(staffSearchDTO).size());
                                        for (AttendanceRecordDTO attendance : recordDTOList) {
                                            if (attendance.getStaffParkId().equals(parkId.getStaffParkId()) && attendance.getStaffTagId().equals(tagId) && attendance.getSignTime().contains(day)) {
                                                if (!StringUtils.isEmpty(attendance.getSignTime()) && !StringUtils.isEmpty(attendance.getSignOutTime())) {
                                                    normal++;
                                                    isVaild = true;
                                                    long signTime = DateUtil.stringToDate(attendance.getSignTime(), "yyyy-MM-dd HH:mm:ss").getTime() / 1000L;
                                                    long signOutTime = DateUtil.stringToDate(attendance.getSignOutTime(), "yyyy-MM-dd HH:mm:ss").getTime() / 1000L;
                                                    float todayWorkTime = signOutTime - signTime;
                                                    countTime = countTime + todayWorkTime;
                                                }
                                            }
                                        }
                                        if (isVaild) {
                                            attendanceRecordDTO.setStaffParkId(parkId.getStaffParkId());
                                            attendanceRecordDTO.setStaffParkName(parkInfo.getParkName());
                                            TagInfo tagInfo = tagInfoService.findById(tagId);
                                            if (tagInfo != null) {
                                                attendanceRecordDTO.setStaffTagId(tagInfo.getTagId());
                                                attendanceRecordDTO.setStaffTagName(tagInfo.getTagName());
                                            } else {
                                                attendanceRecordDTO.setStaffTagId(tagId);
                                                attendanceRecordDTO.setStaffTagName("--");
                                                log.error("tagId : {} tagInfo is null: parkName: {}", tagId, parkInfo.getParkName());
                                            }
                                            attendanceRecordDTO.setDay(day);
                                            attendanceRecordDTO.setNormal(normal);
                                            float countWorkTime = Float.valueOf(String.format("%.1f", countTime / 3600));
                                            attendanceRecordDTO.setCountTime(countWorkTime);
                                            if (countWorkTime != 0) {
                                                attendanceRecordDTO.setAverageTime(Float.valueOf(String.format("%.1f", countWorkTime / normal)));
                                            } else {
                                                attendanceRecordDTO.setAverageTime(0F);
                                            }
                                            list.add(attendanceRecordDTO);
                                        }
                                    }
                                }
                            }

                        }
                    });
                }
                return list;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<AttendanceRecordDTO> countParkRecord(AttendanceDTO attendanceDTO) {
        List<AttendanceRecordDTO> recordDTOList = attendanceRecordMapper.countAttendance(attendanceDTO);
        List<AttendanceRecord> parkList = attendanceRecordMapper.fetchPark(attendanceDTO);
        List<AttendanceRecordDTO> attendanceRecordDTOList = new ArrayList<>();
        if (attendanceDTO.getSignTime() != null && attendanceDTO.getSignOutTime() != null) {
            try {
                Date beginTime = sdf.parse(attendanceDTO.getSignTime());
                Date endTime = sdf.parse(attendanceDTO.getSignOutTime());
                List<String> dayList = Util.getTimeInterval(beginTime, endTime);
                if (parkList != null && parkList.size() > 0) {
                    parkList.forEach(park -> {
                        for (String day : dayList) {
                            Integer normal = 0;
                            float countTime = 0F;
                            AttendanceRecordDTO attendanceRecordDTO = new AttendanceRecordDTO();
                            ParkInfo parkInfo = parkInfoService.findById(park.getStaffParkId());
                            if (parkInfo != null) {
                                attendanceRecordDTO.setStaffParkName(parkInfo.getParkName());
                                attendanceRecordDTO.setStaffParkId(parkInfo.getParkId());
                            }
                            StaffSearchDTO staffSearchDTO = new StaffSearchDTO();
                            staffSearchDTO.setParkId(park.getStaffParkId());
                            List<StaffInfo> staffInfos = staffInfoService.findByCondition(staffSearchDTO);
                            if (staffInfos != null) {
                                attendanceRecordDTO.setTotalStaff(staffInfos.size());
                            }
                            for (AttendanceRecordDTO attendance : recordDTOList) {
                                if (attendance.getStaffParkId().equals(park.getStaffParkId()) && attendance.getSignTime().contains(day)) {
                                    if (!StringUtils.isEmpty(attendance.getSignTime()) && !StringUtils.isEmpty(attendance.getSignOutTime())) {
                                        normal++;
                                        long signTime = DateUtil.stringToDate(attendance.getSignTime(), "yyyy-MM-dd HH:mm:ss").getTime() / 1000L;
                                        long signOutTime = DateUtil.stringToDate(attendance.getSignOutTime(), "yyyy-MM-dd HH:mm:ss").getTime() / 1000L;
                                        float todayWorkTime = signOutTime - signTime;
                                        countTime = countTime + todayWorkTime;
                                    }
                                }
                            }
                            attendanceRecordDTO.setDay(day);
                            attendanceRecordDTO.setNormal(normal);
                            float countWorkTime = Float.valueOf(String.format("%.1f", countTime / 3600));
                            attendanceRecordDTO.setCountTime(countWorkTime);
                            if (countWorkTime != 0) {
                                attendanceRecordDTO.setAverageTime(Float.valueOf(String.format("%.1f", countWorkTime / normal)));
                            } else {
                                attendanceRecordDTO.setAverageTime(0F);
                            }
                            attendanceRecordDTOList.add(attendanceRecordDTO);
                        }
                    });
                }
                return attendanceRecordDTOList;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    public String exportUnitRecord(AttendanceDTO attendanceDTO) {
        List<AttendanceRecordDTO> recordDTOList = countUnitReocord(attendanceDTO);
        if (recordDTOList != null && recordDTOList.size() > 0) {
            ArrayList<LinkedHashMap> maps = new ArrayList<>();
            for (AttendanceRecordDTO attendance : recordDTOList) {
                LinkedHashMap map = new LinkedHashMap();
                map.put("parkName", attendance.getStaffParkName());
                map.put("tagName", attendance.getStaffTagName());
                map.put("day", attendance.getDay());
                map.put("total", attendance.getTotalStaff());
                map.put("normal", attendance.getNormal());
                map.put("averageTime", attendance.getAverageTime());
                map.put("count", attendance.getCountTime());
                maps.add(map);
            }
            if (maps.size() > 0) {
                try {
                    String[] header = new String[]{"园区", "单位", "日期", "总人数", "打卡人数", "平均时间", "总时长"};
                    String fileName = String.format("单位考勤统计-%s.xlsx", sdf.format(new Date()));
                    if (fileName.equals(ExcelUtil.insertDataInSheet(maps, header, excelPath, fileName))) {
                        return excelUrl + fileName;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public String exportParkrRecord(AttendanceDTO attendanceDTO) {
        List<AttendanceRecordDTO> recordDTOList = countParkRecord(attendanceDTO);
        if (recordDTOList != null && recordDTOList.size() > 0) {
            ArrayList<LinkedHashMap> maps = new ArrayList<>();
            for (AttendanceRecordDTO attendance : recordDTOList) {
                LinkedHashMap map = new LinkedHashMap();
                map.put("parkName", attendance.getStaffParkName());
                map.put("day", attendance.getDay());
                map.put("total", attendance.getTotalStaff());
                map.put("normal", attendance.getNormal());
                map.put("averageTime", attendance.getAverageTime());
                map.put("count", attendance.getCountTime());
                maps.add(map);
            }
            if (maps.size() > 0) {
                try {
                    String[] header = new String[]{"园区", "日期", "总人数", "打卡人数", "平均时间", "总时长"};
                    String fileName = String.format("园区考勤统计-%s.xlsx", sdf.format(new Date()));
                    if (fileName.equals(ExcelUtil.insertDataInSheet(maps, header, excelPath, fileName))) {
                        return excelUrl + fileName;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void checkRepeatRecord(AttendanceDTO attendanceDTO) {
        int result = attendanceRecordMapper.checkRepeatRecord(attendanceDTO);
        if (result > 0) {
            log.info("update signTime eq signOutTime success :{}", result);
        } else {
            log.warn("update signTime eq signOutTime is result : {}", result);
        }
    }


    /**
     * 检查每位员工当天考勤是否正常
     */
    @Scheduled(cron = "50 59 23 * * ?")
    public void checkAttendance() {
        AttendanceDTO attendanceDTO = new AttendanceDTO();
        attendanceDTO.setSignTime(simpleDateFormat.format(Util.getDateStartTime()));
        attendanceDTO.setSignOutTime(simpleDateFormat.format(Util.getDateEndTime()));
        List<AttendanceRecord> attendanceRecords = attendanceRecordMapper.searchAttendance(attendanceDTO);
        if (attendanceRecords != null) {
            attendanceRecords.forEach(attendanceRecord -> {
                if (StringUtils.isEmpty(attendanceRecord.getSignTime()) || StringUtils.isEmpty(attendanceRecord.getSignOutTime())) {
                    if (attendanceRecord.getStatus() == null || attendanceRecord.getStatus().equals(Constant.ATTENDANCE_NORMAL)) {
                        log.info("staff :{}  time: {} attendance record is abnormal", attendanceRecord.getStaffName(), sdf.format(Util.getDateStartTime()));
                        attendanceRecord.setStatus(Constant.ATTENDANCE_ABNORMAL);
                        update(attendanceRecord);
                    }
                } else {
                    if (attendanceRecord.getStatus() == null && attendanceRecord.getStatus().equals(Constant.ATTENDANCE_ABNORMAL)) {
                        attendanceRecord.setStatus(Constant.ATTENDANCE_NORMAL);
                        update(attendanceRecord);
                    }
                }
            });
        }
        attendanceDTO.setSignTime(simpleDateFormat.format(Util.getThreeDaysAgoDateStartTime()));
        int result = attendanceRecordMapper.checkRepeatRecord(attendanceDTO);
        if (result > 0) {
            log.info("update signTime eq signOutTime success :{}", result);
        } else {
            log.warn("update signTime eq signOutTime is result : {}", result);
        }
    }


}
