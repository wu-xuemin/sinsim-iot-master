package com.eservice.sinsimiot.service.impl;

import com.alibaba.fastjson.JSON;
import com.eservice.sinsimiot.common.AbstractServiceImpl;
import com.eservice.sinsimiot.core.Constant;
import com.eservice.sinsimiot.model.attendance_record.AttendanceRecord;
import com.eservice.sinsimiot.model.staff.StaffInfo;
import com.eservice.sinsimiot.model.staff_reissue_record.ReissueDTO;
import com.eservice.sinsimiot.service.*;
import com.eservice.sinsimiot.util.ExcelUtil;
import com.google.common.base.Joiner;
import com.eservice.sinsimiot.dao.StaffReissueRecordMapper;
import com.eservice.sinsimiot.model.park_info.ParkInfo;
import com.eservice.sinsimiot.model.staff_reissue_record.StaffReissueRecord;
///import com.hankun.master.service.*;
import com.eservice.sinsimiot.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Class Description: xxx
 *
 * @author Mr.xie
 * @date 2021/06/01.
 */
@Service
@Transactional
@Slf4j
public class StaffReissueRecordServiceImpl extends AbstractServiceImpl<StaffReissueRecord> implements StaffReissueRecordService {
    @Resource
    private StaffReissueRecordMapper staffReissueRecordMapper;
    @Resource
    private StaffInfoService staffInfoService;
    @Resource
    private ParkInfoService parkInfoService;
    @Resource
    private TagInfoService tagInfoService;
    @Resource
    private AttendanceRecordService attendanceRecordService;
    @Value("${path.excel}")
    private String excelPath;
    @Value("${url.excel}")
    private String excelUrl;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    private Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public boolean save(StaffReissueRecord model) {
        model.setId(UUID.randomUUID().toString());
        model.setCreateTime(System.currentTimeMillis());
        if (staffReissueRecordMapper.insertSelective(model) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(StaffReissueRecord model) {
        model.setUpdateTime(System.currentTimeMillis());
        if (staffReissueRecordMapper.updateByPrimaryKeySelective(model) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public StaffReissueRecord saveReissueRecord(ReissueDTO reissueDTO) {
        StaffInfo staffInfo = staffInfoService.findById(reissueDTO.getStaffId());
        if (staffInfo != null) {
            StaffReissueRecord staffReissueRecord = new StaffReissueRecord();
            ParkInfo parkInfo = parkInfoService.findById(staffInfo.getParkId());
            List<String> tagNames = tagInfoService.selectTagNameByTagId(Util.stringToArrayList(staffInfo.getTagIds(), ","));
            staffReissueRecord.setStaffId(staffInfo.getStaffId());
            staffReissueRecord.setStaffName(staffInfo.getName());
            staffReissueRecord.setStaffNum(staffInfo.getStaffNumber());
            staffReissueRecord.setStaffParkId(parkInfo.getParkId());
            staffReissueRecord.setStaffParkName(parkInfo.getParkName());
            staffReissueRecord.setStaffTagId(staffInfo.getTagIds());
            staffReissueRecord.setStaffTagName(Joiner.on(",").join(tagNames));
            if (!StringUtils.isEmpty(reissueDTO.getSignTime()) && !StringUtils.isEmpty(reissueDTO.getSignOutTime())) {
                staffReissueRecord.setSignTime(reissueDTO.getSignTime());
                staffReissueRecord.setSignOutTime(reissueDTO.getSignOutTime());
            } else if (!StringUtils.isEmpty(reissueDTO.getSignTime())) {
                staffReissueRecord.setSignTime(reissueDTO.getSignTime());
            } else {
                staffReissueRecord.setSignOutTime(reissueDTO.getSignOutTime());
            }
            staffReissueRecord.setStatus(Constant.WAIT_CONFIRM);
            if (save(staffReissueRecord)) {
                log.info("save staff : {} reissuRecord signTime : {} ,signOutTime:{}", staffInfo.getName(), reissueDTO.getSignTime(), reissueDTO.getSignOutTime());
                return staffReissueRecord;
            }
        } else {
            log.warn("staff : {} no exist ,submit reissue record :{}", reissueDTO.getStaffId(), JSON.toJSONString(reissueDTO));
        }
        return null;
    }

    @Override
    public StaffReissueRecord confirmReissueRecord(ReissueDTO reissueDTO) {
        StaffReissueRecord staffReissueRecord = findById(reissueDTO.getRecordId());
        staffReissueRecord.setStatus(reissueDTO.getStatus());
        staffReissueRecord.setRemark(reissueDTO.getRemark());
        if (reissueDTO.getStatus().equals(Constant.CONFIRM)) {
            String time = staffReissueRecord.getSignTime().split(" ")[0];
            AttendanceRecord attendanceRecord = attendanceRecordService.fetchStaffAttendance(staffReissueRecord.getStaffId(), time);
            if (attendanceRecord != null) {
                if (!StringUtils.isEmpty(staffReissueRecord.getSignTime()) && !StringUtils.isEmpty(staffReissueRecord.getSignOutTime())) {
                    attendanceRecord.setSignTime(staffReissueRecord.getSignTime());
                    attendanceRecord.setSignOutTime(staffReissueRecord.getSignOutTime());
                } else if (!StringUtils.isEmpty(staffReissueRecord.getSignOutTime())) {
                    attendanceRecord.setSignOutTime(staffReissueRecord.getSignOutTime());
                } else {
                    attendanceRecord.setSignTime(staffReissueRecord.getSignTime());
                }
                attendanceRecord.setType(Constant.REISSUE);
                if (attendanceRecordService.update(attendanceRecord)){
                    log.info("审核通过");
                }
            } else {
                attendanceRecord = new AttendanceRecord(staffReissueRecord);
                attendanceRecord.setRecordId(staffReissueRecord.getId());
                attendanceRecord.setType(Constant.REISSUE);
                if (attendanceRecordService.save(attendanceRecord)) {
                    log.info("审核通过");
                }
            }

        }
        if (update(staffReissueRecord)) {
            log.info("confirm staff : {} reissuRecord signTime : {} ,signOutTime:{}", staffReissueRecord.getStaffName(), staffReissueRecord.getSignTime(), staffReissueRecord.getSignOutTime());
            return staffReissueRecord;
        }
        return null;
    }


    @Override
    public List<StaffReissueRecord> fetchReissueRecord(ReissueDTO reissueDTO) {
        return staffReissueRecordMapper.fetchReissueRecord(reissueDTO);
    }

    @Override
    public List<StaffReissueRecord> searchRecord(ReissueDTO reissueDTO) {
        return staffReissueRecordMapper.searchRecord(reissueDTO);
    }

    @Override
    public String exportReissueRecord(ReissueDTO reissueDTO) {
        List<StaffReissueRecord> staffReissueRecordList = staffReissueRecordMapper.fetchReissueRecord(reissueDTO);
        if (staffReissueRecordList != null && staffReissueRecordList.size() > 0) {
            ArrayList<LinkedHashMap> maps = new ArrayList<>();
            staffReissueRecordList.forEach(record -> {
                LinkedHashMap map = new LinkedHashMap();
                map.put("name", record.getStaffName());
                map.put("staffNum", record.getStaffNum());
                map.put("park", record.getStaffParkName());
                map.put("unit", record.getStaffTagName());
                map.put("signTime", record.getSignTime());
                map.put("signOutTime", record.getSignOutTime());
                if (record.getStatus().equals(Constant.CONFIRM)) {
                    map.put("status", "审核通过");
                } else if (record.getStatus().equals(Constant.WAIT_CONFIRM)) {
                    map.put("status", "待审核");
                } else {
                    map.put("status", "已拒绝");
                }
                map.put("remark", record.getRemark());
                maps.add(map);
            });
            if (maps.size() > 0) {
                String[] header = new String[]{"姓名", "工号", "园区", "单位", "上班时间", "下班时间", "状态", "备注"};
                String fileName = String.format("补签信息-%s.xlsx", sdf.format(new Date()));
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
}
