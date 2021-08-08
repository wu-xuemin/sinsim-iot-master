package com.eservice.sinsimiot.dao;


import com.eservice.sinsimiot.model.staff.StaffInfo;
import com.eservice.sinsimiot.common.Mapper;
import com.eservice.sinsimiot.model.attendance_record.AttendanceDTO;
import com.eservice.sinsimiot.model.staff.StaffSearchDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : silent
 * @description : 员工管理
 * @date: 2020/3/23 14:52
 */
public interface StaffInfoMapper extends Mapper<StaffInfo> {

    /**
     * 根据标签id查询人员
     *
     * @param tagIds
     * @return
     */
    List<StaffInfo> selectByTagId(List<String> tagIds);

    /**
     * 根据条件查询
     *
     * @param staffSearchDTO
     * @return
     */
    List<StaffInfo> findByCondition(StaffSearchDTO staffSearchDTO);

    /**
     * 判断工号是否存在
     * @param staffNumber
     * @return
     */
    List<StaffInfo> checkStaffNumber(@Param("staffNumber") String staffNumber);

    /**
     * 判断卡号是否存在
     * @param cardNumber
     * @return
     */
    List<StaffInfo> checkCardNumber(@Param("cardNumber") String cardNumber);


    /**
     * 判断标签是否被使用
     * @param tagId
     * @return
     */
    List<StaffInfo> checkStaffTag(@Param("tagId") String tagId);


    /**
     * 根据标签id统计人数
     *
     * @param staffSearchDTO
     * @return
     * @Author: liu qing
     * @date: 2020/7/14
     */
    Integer staffInfoByTagIdCount(StaffSearchDTO staffSearchDTO);


    StaffInfo login(@Param("account") String account, @Param("password") String password);

    List<StaffInfo> fetchStaffNums(AttendanceDTO attendanceDTO);
}
