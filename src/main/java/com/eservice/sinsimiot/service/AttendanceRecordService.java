package com.eservice.sinsimiot.service;

import com.eservice.sinsimiot.common.Service;
import com.eservice.sinsimiot.model.attendance_record.AttendanceRecord;
import com.eservice.sinsimiot.model.attendance_record.AttendanceRecordDTO;
import com.eservice.sinsimiot.model.attendance_record.AttendanceDTO;

import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Mr.xie
 * @date 2021/05/08.
 */
public interface AttendanceRecordService extends Service<AttendanceRecord> {

    /**
     * 根据员工和年-月-日判断员工是否已经存在考勤记录
     *
     * @param staffId
     * @param signTime
     * @return
     */
    AttendanceRecord fetchStaffAttendance(String staffId, String signTime);


    /**
     * 考勤列表搜索
     *
     * @param attendanceDTO
     * @return
     */
    List<AttendanceRecord> searchAttendance(AttendanceDTO attendanceDTO);


    /**
     * 移动端查询记录接口
     * @param attendanceDTO
     * @return
     */
    List<AttendanceRecord> appSearchAttendance(AttendanceDTO attendanceDTO);

    /**
     * 考勤导出
     *
     * @param attendanceDTO
     * @return
     */
    String export(AttendanceDTO attendanceDTO);

    /**
     * 考勤统计
     *
     * @param attendanceDTO
     * @return
     */
    List<AttendanceRecordDTO> countAttendance(AttendanceDTO attendanceDTO);

    /**
     * 导出考勤统计信息
     *
     * @param attendanceRecordDTOS
     * @return
     */
    String exportCounAttendance(List<AttendanceRecordDTO> attendanceRecordDTOS);

    /**
     * 考勤统计中每位员工的考勤详情
     *
     * @param attendanceDTO
     * @return
     */
    List<AttendanceRecordDTO> fetchAttendanceRecordDTODetail(AttendanceDTO attendanceDTO);


    /**
     * 导出
     *
     * @param attendanceDTO
     * @return
     */
    String exportAttendanceRecordDTODetail(AttendanceDTO attendanceDTO);


    List<AttendanceRecordDTO> countUnitReocord(AttendanceDTO attendanceDTO);

    String exportUnitRecord(AttendanceDTO attendanceDTO);

    List<AttendanceRecordDTO> countParkRecord(AttendanceDTO attendanceDTO);

    String exportParkrRecord(AttendanceDTO attendanceDTO);

    void checkRepeatRecord(AttendanceDTO attendanceDTO);

}
