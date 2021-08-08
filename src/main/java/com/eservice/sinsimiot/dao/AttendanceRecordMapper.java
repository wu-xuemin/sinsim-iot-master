package com.eservice.sinsimiot.dao;

import com.eservice.sinsimiot.common.Mapper;
import com.eservice.sinsimiot.model.attendance_record.AttendanceRecord;
import com.eservice.sinsimiot.model.attendance_record.AttendanceDTO;
import com.eservice.sinsimiot.model.attendance_record.AttendanceRecordDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AttendanceRecordMapper extends Mapper<AttendanceRecord> {

    AttendanceRecord fetchStaffAttendance(@Param("staffId") String staffId, @Param("signTime") String signTime);

    List<AttendanceRecord> searchAttendance(AttendanceDTO attendanceDTO);

    List<AttendanceRecord> appSearchAttendance(AttendanceDTO attendanceDTO);

    /**
     * 考勤统计数据
     *
     * @param attendanceDTO
     * @return
     */
    List<AttendanceRecordDTO> countAttendance(AttendanceDTO attendanceDTO);

    /**
     * 去重后的员工工号
     *
     * @param attendanceDTO
     * @return
     */
    List<AttendanceRecord> fetchStaffNum(AttendanceDTO attendanceDTO);

    List<AttendanceRecord> fetchPark(AttendanceDTO attendanceDTO);

    List<AttendanceRecord> fetchTag(AttendanceDTO attendanceDTO);

    int checkRepeatRecord(AttendanceDTO attendanceDTO);
}
