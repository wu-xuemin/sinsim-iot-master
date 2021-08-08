package com.eservice.sinsimiot.service;

import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.Service;
import com.eservice.sinsimiot.model.staff.StaffInfo;
import com.eservice.sinsimiot.model.staff.StaffSearchDTO;
import com.eservice.sinsimiot.model.attendance_record.AttendanceDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Code Generator
 * @date 2020/03/06.
 */
public interface StaffInfoService extends Service<StaffInfo> {


    /**
     * 解压上传的压缩包
     *
     * @param multipartFile 压缩包
     * @param password      密码
     * @return
     */
    File unZipFile(MultipartFile multipartFile, String password);

    /**
     * 读取Excel表格中的员工数据
     *
     * @param file
     * @return
     */
    String readStaffData(File file);


    /**
     * 根据标签id获取该标签的所有人员
     *
     * @param tagIds
     * @return
     */
    List<StaffInfo> findStaffInfoByTagId(List<String> tagIds);


    /**
     * 根据条件查询
     *
     * @param staffSearchDTO
     * @return
     */
    List<StaffInfo> findByCondition(StaffSearchDTO staffSearchDTO);


    /**
     * 判断工号是否存在
     *
     * @param staffNumber
     * @return
     */
    boolean checkStaffNumber(String staffNumber);

    /**
     * 判断卡号是否存在
     *
     * @param cardNumber
     * @return
     */
    boolean checkCardNumber(String cardNumber);


    /**
     * 判断标签是否被使用
     *
     * @param tagId
     * @return
     */
    List<StaffInfo> checkStaffTag(String tagId);

    /**
     * 修改指定标签的人员
     *
     * @param tagId
     */
    void updateStaffInfoByTag(String tagId);

    /***
     * 导出指定的人员信息
     * @param staffSearchDTO
     * @return
     */
    String exportRecord(StaffSearchDTO staffSearchDTO);

    /***
     * 导出指定的人员信息为zip
     * @param staffSearchDTO
     * @return
     */
    String exportRecordZip(StaffSearchDTO staffSearchDTO);

    /**
     * 根据标签id统计标签人数
     *
     * @param staffSearchDTO
     * @return
     * @Author: liu qing
     * @date: 2020/7/14
     */
    Integer staffInfoByTagIdCount(StaffSearchDTO staffSearchDTO);

    Result saveStaff(StaffInfo staffInfo);

    Result updateStaff(StaffInfo staffInfo, StaffInfo oldStaffInfo);

    boolean deleteStaff(String staffId);

    StaffInfo login(String account, String password);

    boolean settingStaff(StaffInfo staffInfo);

    List<StaffInfo> fetchStaffNums(AttendanceDTO attendanceDTO);
}
