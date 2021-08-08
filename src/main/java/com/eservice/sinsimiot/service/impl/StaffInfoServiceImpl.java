package com.eservice.sinsimiot.service.impl;

import com.alibaba.fastjson.JSON;
import com.eservice.sinsimiot.core.*;
import com.eservice.sinsimiot.dao.StaffInfoMapper;
import com.eservice.sinsimiot.model.departing_staff.DepartingStaff;
import com.eservice.sinsimiot.model.park.*;
import com.eservice.sinsimiot.model.staff.StaffInfo;
import com.eservice.sinsimiot.service.DepartingStaffService;
import com.eservice.sinsimiot.service.LogRecordService;
import com.eservice.sinsimiot.service.TagInfoService;
import com.eservice.sinsimiot.util.*;
import com.google.common.base.Joiner;

import com.eservice.sinsimiot.common.AbstractServiceImpl;
import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
///import com.hankun.master.common.ScheduledService;

///import com.hankun.master.core.*;
import com.eservice.sinsimiot.model.attendance_record.AttendanceDTO;
///import com.hankun.master.model.park.*;
import com.eservice.sinsimiot.model.park_info.ParkInfo;
import com.eservice.sinsimiot.model.staff.StaffSearchDTO;
import com.eservice.sinsimiot.model.tag.TagInfo;
import com.eservice.sinsimiot.service.StaffInfoService;
import com.eservice.sinsimiot.service.park.StaffService;
import com.eservice.sinsimiot.service.park.TagService;
///import com.hankun.master.util.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Class Description: xxx
 *
 * @author Code Generator
 * @date 2020/03/06.
 */
@Service
@Transactional
@Slf4j
public class StaffInfoServiceImpl extends AbstractServiceImpl<StaffInfo> implements StaffInfoService {

    private final String MODULE = "员工管理";

    @Value("${path.images}")
    private String imagePath;
    @Value("${path.name}")
    private String excelName;
    @Value("${path.excel}")
    private String excelPath;
    @Value("${url.excel}")
    private String excelUrl;
    @Value("${path.upload}")
    private String uploadPath;

    @Value("${path.export}")
    private String exportPath;

    @Value("${path.zip}")
    private String zipPath;
    @Value("${url.zip}")
    private String zipUrl;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    private StaffInfoMapper staffInfoMapper;

    @Resource
    private TagInfoService tagInfoService;
//    @Resource
//    private ScheduledService scheduledService;
    @Resource
    private TagService tagService;
    @Resource
    private ParkInfoServiceImpl parkInfoService;
    @Resource
    private StaffService staffService;
    @Resource
    private LogRecordService logRecordService;
    @Resource
    private DepartingStaffService departingStaffService;

    @Override
    public File unZipFile(MultipartFile multipartFile, String password) {
        try {
            File file = FileUtil.multipartFileToFile(multipartFile);
            if (!FileUtil.existsFile(uploadPath)) {
                return null;
            }
            if (UnPackageUtil.unPackZip(file, "", uploadPath)) {
                file = new File(uploadPath + excelName);
                if (file.exists()) {
                    return file;
                }
            }
        } catch (Exception e) {
            log.error("unZipFile error : {}  ", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String readStaffData(File file) {
        ArrayList<StaffInfo> staffInfoArrayList;
        try {
            staffInfoArrayList = ExcelUtil.importRecord(file, StaffInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(" readStaffData : {} ", e.getMessage());
            staffInfoArrayList = null;
        }
        ArrayList<LinkedHashMap> maps = new ArrayList<>();
        if (staffInfoArrayList == null || staffInfoArrayList.size() == 0) {
            LinkedHashMap map = new LinkedHashMap();
            map.put("error", "Excel表格数据读取失败");
            maps.add(map);
        } else {
            for (StaffInfo staffInfo : staffInfoArrayList) {
                boolean isSave = true;
                LinkedHashMap map = new LinkedHashMap();
                ArrayList<String> tagNames = OtherUtil.stringToArrayList(staffInfo.getTagIds(), ",");
                if (staffInfo.getName() == null || staffInfo.getName().isEmpty()) {
                    map.put("name", "姓名不能为空");
                    isSave = false;
                } else {
                    map.put("name", staffInfo.getName());
                }
                if (staffInfo.getStaffNumber() == null || staffInfo.getStaffNumber().isEmpty()) {
                    map.put("staffNumber", "工号不能为空");
                    isSave = false;
                } else {
                    if (checkStaffNumber(staffInfo.getStaffNumber())) {
                        map.put("staffNumber", "工号不能重复");
                        isSave = false;
                    } else {
                        map.put("staffNumber", staffInfo.getStaffNumber());
                    }
                }
                if (staffInfo.getCardNumber() == null || staffInfo.getCardNumber().isEmpty()) {
                    map.put("cardNumber", "");
                } else {
                    if (checkCardNumber(staffInfo.getCardNumber())) {
                        map.put("cardNumber", "卡号不能重复");
                        isSave = false;
                    } else {
                        if (ValidateUtil.validateDigit(staffInfo.getCardNumber())) {
                            map.put("cardNumber", staffInfo.getCardNumber());
                        } else {
                            map.put("cardNumber", "卡号只能是数字");
                            isSave = false;
                        }
                    }
                }
                ParkInfo parkInfo = null;
                String parkName = staffInfo.getParkId();
                if (parkName == null || parkName.isEmpty()) {
                    map.put("parkId", "所属园区不能为空");
                    isSave = false;
                } else {
                    parkInfo = parkInfoService.findBy("parkName", parkName);
                    if (parkInfo == null) {
                        map.put("parkId", "所属园区不能为空");
                        isSave = false;
                    } else {
                        staffInfo.setParkId(parkInfo.getParkId());
                        map.put("parkId", parkName);
                    }
                }
                if (tagNames == null || tagNames.size() == 0) {
                    map.put("tagIds", "标签不能为空");
                    isSave = false;
                } else {
                    ArrayList<String> tagIds = new ArrayList();
                    for (String tagName : tagNames) {
                        TagInfo tag = tagInfoService.findBy("tagName", tagName);
                        if (tag == null) {
                            tag = new TagInfo();
                            tag.setTagName(tagName);
                            tag.setTagType(IdentityType.STAFF);
                            tagInfoService.save(tag);
                        }
                        tagIds.add(tag.getTagId());
                    }
                    if (parkInfo != null) {
                        List<String> parkUnits = Util.stringToArrayList(parkInfo.getUnit(), ",");
                        for (String tagId : tagIds) {
                            if (!parkInfo.getUnit().contains(tagId)) {
                                parkUnits.add(tagId);
                            }
                        }
                        parkInfo.setUnit(Joiner.on(",").join(parkUnits));
                        parkInfoService.update(parkInfo);
                    }

                    staffInfo.setTagIds(String.join(",", tagIds));
                    map.put("tagIds", tagNames);
                }
                String image = ImageUtil.compressedByPath(uploadPath + staffInfo.getFaceIds());
                if (image != null) {
                    //缺少图片质量检测
                    String uuid = UUID.randomUUID().toString();
                    if (ImageUtil.saveBase64ToFile(imagePath, image, uuid)) {
                        staffInfo.setFaceIds(uuid);
                        map.put("img", uuid);
                    } else {
                        map.put("img", "图片格式错误");
                        isSave = false;
                    }
                } else {
                    map.put("img", "图片不存在");
                    isSave = false;
                }
                if (staffInfo.getContact() == null || staffInfo.getContact().isEmpty()) {
                    map.put("contact", "");
                } else {
                    if (ValidateUtil.isSpecialChar(staffInfo.getContact())) {
                        map.put("contact", "联系方式不能包含特殊字符");
                    } else {
                        map.put("contact", staffInfo.getContact());
                    }
                }
                if (isSave) {
                    staffInfo.setStaffId(UUID.randomUUID().toString());
                    try {
                        Staff staff = staffInfoToStaff(staffInfo);
                        if (staff != null) {
                            staff = staffService.createStaff(staff);
                            if (staff.getStaff_id() != null) {
                                staffInfo.setStaffId(staff.getStaff_id());
                                if (save(staffInfo)) {
                                    log.info("staff : {} import success", staffInfo.getName());
                                } else {
                                    map.put("saveError", "数据录入失败，请联系管理员");
                                    maps.add(map);
                                }
                            } else {
                                log.error("satff : {} add to park fail");
                                map.put("saveError", "数据录入到人脸平台失败，请联系管理员");
                                maps.add(map);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("readStaffData Save StaffInfo error : {}  ", e.getMessage());
                        map.put("saveError", e.getMessage());
                        maps.add(map);
                    }
                } else {
                    maps.add(map);
                }
            }
        }
        if (maps.size() > 0) {
            try {
                String[] header = new String[]{"姓名", "工号", "卡号", "园区", "标签", "图片", "联系方式", "其他错误"};
                if (excelName.equals(ExcelUtil.insertDataInSheet(maps, header, excelPath, excelName))) {
                    return excelUrl + excelName;
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error(" readStaffData insertDataInSheet error :  {} ", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 根据标签id获取该标签的所有人员
     *
     * @param tagIds
     * @return
     */
    @Override
    public List<StaffInfo> findStaffInfoByTagId(List<String> tagIds) {
        return staffInfoMapper.selectByTagId(tagIds);
    }


    /**
     * 根据条件查询
     *
     * @param staffSearchDTO
     * @return
     */
    @Override
    public List<StaffInfo> findByCondition(StaffSearchDTO staffSearchDTO) {
        return staffInfoMapper.findByCondition(staffSearchDTO);
    }

    /**
     * 判断工号是否存在
     *
     * @param staffNumber
     * @return
     */
    @Override
    public boolean checkStaffNumber(String staffNumber) {
        List<StaffInfo> staffInfoList = staffInfoMapper.checkStaffNumber(staffNumber);
        if (staffInfoList == null || staffInfoList.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断卡号是否存在
     *
     * @param cardNumber
     * @return
     */
    @Override
    public boolean checkCardNumber(String cardNumber) {
        List<StaffInfo> staffInfoList = staffInfoMapper.checkCardNumber(cardNumber);
        if (staffInfoList == null || staffInfoList.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<StaffInfo> checkStaffTag(String tagId) {
        List<StaffInfo> staffInfos = staffInfoMapper.checkStaffTag(tagId);
        return staffInfos;
    }

    @Override
    public void updateStaffInfoByTag(String tagId) {
        log.info("delete tag : {}", tagId);
        List<StaffInfo> staffInfoList = staffInfoMapper.checkStaffTag(tagId);
        if (staffInfoList != null || staffInfoList.size() > 0) {
            for (StaffInfo staffInfo : staffInfoList) {
                List<String> tagInfos = OtherUtil.stringToArrayList(staffInfo.getTagIds(), ",");
                log.info("staff : {}, oldTag : {}", staffInfo.getName(), staffInfo.getTagIds());
                tagInfos.remove(tagId);
                if (tagInfos != null && tagInfos.size() != 0) {
                    staffInfo.setTagIds(Joiner.on(",").join(tagInfos));
                } else {
                    staffInfo.setTagIds("");
                }
                update(staffInfo);
                log.info("staff : {}, newTag : {}", staffInfo.getName(), staffInfo.getTagIds());
            }
        }

    }

    @Override
    public List<StaffInfo> fetchStaffNums(AttendanceDTO attendanceDTO) {
        return staffInfoMapper.fetchStaffNums(attendanceDTO);
    }

    /***
     * 导出指定的人员信息
     * @param staffSearchDTO
     * @return
     */
    @Override
    public String exportRecord(StaffSearchDTO staffSearchDTO) {
        List<StaffInfo> list = findByCondition(staffSearchDTO);
        if (list != null) {
            ArrayList<LinkedHashMap> maps = new ArrayList<>();
            for (StaffInfo staffInfo : list) {
                LinkedHashMap map = new LinkedHashMap();
                map.put("name", staffInfo.getName());
                map.put("staff", staffInfo.getStaffNumber());
                map.put("card", (staffInfo.getCardNumber() == null) ? "" : staffInfo.getCardNumber());
                ParkInfo parkInfo = parkInfoService.findById(staffInfo.getParkId());
                map.put("park", parkInfo.getParkName());
                ArrayList<String> tagIds = OtherUtil.stringToArrayList(staffInfo.getTagIds(), ",");
                if (tagIds != null) {
                    List<String> tagNames = tagInfoService.selectTagNameByTagId(tagIds);
                    if (tagNames != null && tagNames.size() > 0) {
                        map.put("tag", Joiner.on(",").join(tagNames));
                    } else {
                        map.put("tag", "");
                    }
                } else {
                    map.put("tag", "");
                }
                map.put("image", String.format("%s%s", imagePath, staffInfo.getFaceIds()));
                map.put("contact", (staffInfo.getContact() == null) ? "" : staffInfo.getContact());
                maps.add(map);
            }
            if (maps != null && maps.size() > 0) {
                String[] header = new String[]{"姓名", "工号", "卡号", "园区", "标签", "图片", "联系方式"};
                String fileName = String.format("人员信息-%s.xls", sdf.format(new Date()));
                try {
                    if (fileName.equals(ExcelUtil.insertDataInSheet(maps, header, excelPath, fileName))) {
                        return excelUrl + fileName;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(" export error : {} ", e.getMessage());
                }
            }
        }
        return null;
    }

    /***
     * （新）导出指定的人员信息
     * @param staffSearchDTO
     * @return
     */
    @Override
    public String exportRecordZip(StaffSearchDTO staffSearchDTO) {
        List<StaffInfo> list = findByCondition(staffSearchDTO);
        if (list != null) {
            ArrayList<LinkedHashMap> maps = new ArrayList<>();
            for (StaffInfo staffInfo : list) {
                LinkedHashMap map = new LinkedHashMap();
                map.put("name", staffInfo.getName());
                //员工工号
                map.put("staff", staffInfo.getStaffNumber());
                map.put("birthday", (staffInfo.getCardNumber() == null) ? "" : staffInfo.getBirthday());
                map.put("entryTime", (staffInfo.getCardNumber() == null) ? "" : staffInfo.getEntryTime());
                map.put("contact", (staffInfo.getContact() == null) ? "" : staffInfo.getContact());
                ArrayList<String> tagIds = OtherUtil.stringToArrayList(staffInfo.getTagIds(), ",");
                if (tagIds != null) {
                    List<String> tagNames = tagInfoService.selectTagNameByTagId(tagIds);
                    if (tagNames != null && tagNames.size() > 0) {
                        map.put("tag", Joiner.on(",").join(tagNames));
                    } else {
                        map.put("tag", "");
                    }
                } else {
                    map.put("tag", "");
                }
                map.put("card", (staffInfo.getCardNumber() == null) ? "" : staffInfo.getCardNumber());
                ParkInfo parkInfo = parkInfoService.findById(staffInfo.getParkId());
                map.put("park", parkInfo.getParkName());
                /** 图片copy到文件夹 **/
                String fileName = String.format("%s-%s.jpg", staffInfo.getStaffNumber(), staffInfo.getName());
                if (FileUtil.copyFile(imagePath + staffInfo.getFaceIds(), exportPath + Constant.IMAGE, fileName)) {
                    map.put("path", Constant.IMAGE + fileName);
                } else {
                    map.put("path", "图片不存在");
                }
                map.put("remark", staffInfo.getRemark());
                maps.add(map);
            }
            if (maps != null && maps.size() > 0) {
                String[] header = new String[]{"姓名", "员工号", "出生日期", "入职时间", "联系方式", "标签", "卡号", "园区", "图片路径", "备注"};
                String fileName = String.format("人员信息-%s.xls", sdf.format(new Date()));
                try {
                    if (fileName.equals(ExcelUtil.insertDataInSheet(maps, header, exportPath, fileName))) {
                        //excel生成结束，将整个目录打包成zip
                        String zipName = String.format("人员信息-%s.zip", sdf.format(new Date()));
                        FileUtil.createZip(exportPath, zipPath, zipName);
                        //删除文件
//                        scheduledService.delFile(exportPath, 0);
                        return zipUrl + zipName;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    log.error(" exportRecordZip : {} ", e.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * 根据标签id统计标签人数
     *
     * @param staffSearchDTO
     * @return
     * @Author: liu qing
     * @date: 2020/7/14
     */
    @Override
    public Integer staffInfoByTagIdCount(StaffSearchDTO staffSearchDTO) {
        return staffInfoMapper.staffInfoByTagIdCount(staffSearchDTO);
    }

    @Override
    public Result saveStaff(StaffInfo staffInfo) {
        Staff staff = staffInfoToStaff(staffInfo);
        try {
            Staff addResult = staffService.createStaff(staff);
            if (addResult.getStaff_id() != null) {
                staffInfo.setStaffId(addResult.getStaff_id());
                if (save(staffInfo)) {
                    log.info("save staff : {} to database success", staffInfo.getName());
                    return ResultGenerator.genSuccessResult(staffInfo);
                }
            }
        } catch (ParkException e) {
            log.error("add staff:{} to park exception: {}", staffInfo.getName(), e.getMsg());
            return ResultGenerator.genFailResult(e.getMsg());
        }
        return ResultGenerator.genFailResult(MessageConstant.ADD_FAILED_CN);
    }

    private Staff staffInfoToStaff(StaffInfo staffInfo) {
        Staff staff = new Staff();
        PersonInformation personInformation = new PersonInformation();
        personInformation.setName(staffInfo.getName());
        personInformation.setId(staffInfo.getStaffNumber());
        personInformation.setPhone(staffInfo.getContact());
        if (staffInfo.getEntryTime() != null && !staffInfo.getEntryTime().isEmpty()) {
            personInformation.setEmployed_date(Long.valueOf(staffInfo.getEntryTime()));
        }
        if (staffInfo.getTagIds() != null) {
            List<String> tagIds = Util.stringToArrayList(staffInfo.getTagIds(), ",");
            List<String> tagNames = tagInfoService.selectTagNameByTagId(tagIds);
            if (tagNames != null && tagNames.size() > 0) {
                personInformation.setRemark(Joiner.on(",").join(tagNames));
            } else {
                log.warn("staff: {} tagNames is null ,tagId:{}", staffInfo.getName(), staffInfo.getTagIds());
            }
        } else {
            log.warn("staff: {} tagId is null", staffInfo.getName());
        }
        ParkInfo parkInfo = parkInfoService.findById(staffInfo.getParkId());
        if (parkInfoService != null) {
            Tag tag = tagService.isExistAndAdd(parkInfo.getParkName());
            if (tag != null) {
                staff.setTag_id_list(Util.stringToArrayList(tag.getTag_id(), ","));
            }
        }
        staff.setPerson_information(personInformation);
        if (staffInfo.getCardNumber() != null && !staffInfo.getCardNumber().isEmpty()) {
            staff.setCard_numbers(Util.stringToArrayList(staffInfo.getCardNumber(), ","));
        }
        if (staffInfo.getFaceIds() != null) {
            log.info("staff : {} face image is exit :{}", staffInfo.getName(), staffInfo.getFaceIds());
            File file = new File(imagePath + staffInfo.getFaceIds());
            List<String> images = new ArrayList<>();
            String faceImage = ImageUtil.getFileBase64(file);
            if (faceImage != null) {
                images.add(faceImage);
                staff.setFace_image_content_list(images);
            }
        }
        return staff;
    }

    @Override
    public Result updateStaff(StaffInfo staffInfo, StaffInfo oldStaffInfo) {
        Staff staff = staffService.getStaffById(staffInfo.getStaffId());
        if (staff != null) {
            log.info("staff :{} is exit park", staffInfo.getName());
            PersonInformation personInformation = staff.getPerson_information();
            personInformation.setName(staffInfo.getName());
            personInformation.setId(staffInfo.getStaffNumber());
            personInformation.setPhone(staffInfo.getContact());
            if (staffInfo.getEntryTime() != null && !staffInfo.getEntryTime().isEmpty()) {
                personInformation.setEmployed_date(Long.valueOf(staffInfo.getEntryTime()));
            }
            if (!staffInfo.getTagIds().equals(oldStaffInfo.getTagIds())) {
                List<String> tagIds = Util.stringToArrayList(staffInfo.getTagIds(), ",");
                List<String> tagNames = tagInfoService.selectTagNameByTagId(tagIds);
                log.info("staff: {} tag is change,new tag :{}, old tag:{}", staffInfo.getName(), JSON.toJSONString(tagNames), personInformation.getRemark());
                personInformation.setRemark(Joiner.on(",").join(tagNames));

            }
            if (!staffInfo.getParkId().equals(oldStaffInfo.getParkId())) {
                ParkInfo parkInfo = parkInfoService.findById(staffInfo.getParkId());
                if (parkInfo != null) {
                    Tag tag = tagService.isExistAndAdd(parkInfo.getParkName());
                    if (tag != null) {
                        staff.setTag_id_list(Util.stringToArrayList(tag.getTag_id(), ","));
                    }
                }
                log.info("staff :{} park is change :{}");
            }
            if (staffInfo.getCardNumber() != null && !staffInfo.getCardNumber().isEmpty()) {
                staff.setCard_numbers(Util.stringToArrayList(staffInfo.getCardNumber(), ","));
            }
            personInformation.setPhone(staffInfo.getContact());
            UpdateFace updateFace = new UpdateFace();
            List<String> deleteFaceId = new ArrayList<>();
            if (staff.getFace_list() != null && staff.getFace_list().size() > 0) {
                for (FaceListBean face : staff.getFace_list()) {
                    deleteFaceId.add(face.getFace_id());
                }
            }
            if (!staffInfo.getFaceIds().equals(oldStaffInfo.getFaceIds())) {
                updateFace.setDelete_face_id_list(deleteFaceId);
                File file = new File(imagePath + staffInfo.getFaceIds());
                List<String> images = new ArrayList<>();
                String faceImage = ImageUtil.getFileBase64(file);
                if (faceImage != null) {
                    images.add(faceImage);
                    updateFace.setInsert_face_image_content_list(images);
                }
                log.info("staff : {} face image :{} have update ", staffInfo.getName(), staffInfo.getFaceIds());
                updateFace.setEnforce(true);
            } else {
                updateFace.setDelete_face_id_list(new ArrayList<>());
                log.info("staff : {} face : {}  is not update  old face: {}", staffInfo.getName(), staffInfo.getFaceIds(), oldStaffInfo.getFaceIds());
                updateFace.setEnforce(false);
                updateFace.setInsert_face_image_content_list(new ArrayList<>());
            }
            staff.setUpdate_face(updateFace);
            try {
                if (staffService.updateStaff(staff) == ResponseCode.success) {
                    update(staffInfo);
                    return ResultGenerator.genSuccessResult(staffInfo);
                }
            } catch (ParkException e) {
                log.error("update staff : {} on park exception: {}", staffInfo.getName(), e.getMsg());
                return ResultGenerator.genFailResult(e.getMsg());
            }
        } else {
            log.info("staff:{} is not exit park", staffInfo.getName());
            return saveStaff(staffInfo);
        }
        return ResultGenerator.genFailResult(MessageConstant.UPDATE_FAILED_CN);
    }


    @Override
    public boolean settingStaff(StaffInfo staffInfo) {
        if (staffService.deleteStaff(staffInfo.getStaffId())) {
            log.info("park staff : {} delete success", staffInfo.getName());
        }
        if (deleteById(staffInfo.getStaffId())) {
            log.info("staff : {} departing  remove on staff_info", staffInfo.getName());
        }
        DepartingStaff departingStaff = new DepartingStaff();
        BeanUtils.copyProperties(staffInfo, departingStaff);
        return departingStaffService.save(departingStaff);
    }

    @Override
    public StaffInfo login(String account, String password) {
        return staffInfoMapper.login(account, password);
    }

    @Override
    public boolean deleteStaff(String staffId) {
        StaffInfo staffInfo = findById(staffId);
        Staff staff = staffService.getStaffById(staffId);
        boolean result = false;
        if (staff != null) {
            if (staffService.deleteStaff(staffId)) {
                if (deleteById(staffId)) {
                    if (staffInfo.getFaceIds() != null) {
                        try {
                            File file = new File(imagePath + staffInfo.getFaceIds());
                            log.info("delete staff :{} face image", staffInfo.getName());
                            FileUtil.deleteFile(file);
                            result = true;
                        } catch (Exception e) {
                            log.error("delete staff: {} image error", staffInfo.getName());
                        }
                    }
                    log.info("delete staff: {} on database success", staffInfo.getName());
                }
            }
        } else {
            log.warn("staff :{} no exist park", staffInfo.getName());
            if (deleteById(staffId)) {
                if (staffInfo.getFaceIds() != null) {
                    try {
                        File file = new File(imagePath + staffInfo.getFaceIds());
                        log.info("delete staff :{} face image", staffInfo.getName());
                        FileUtil.deleteFile(file);
                        result = true;
                    } catch (Exception e) {
                        log.error("delete staff: {} image error", staffInfo.getName());
                    }
                }
                log.info("delete staff: {} on database success", staffInfo.getName());
            }
        }
        return result;
    }

    @Override
    public boolean save(StaffInfo model) {
        model.setCreateTime(System.currentTimeMillis());
        model.setUpdateTime(System.currentTimeMillis());
        if (staffInfoMapper.insertSelective(model) > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean update(StaffInfo model) {
        model.setUpdateTime(System.currentTimeMillis());
        if (staffInfoMapper.updateByPrimaryKeySelective(model) > 0) {
            return true;
        } else {
            return false;
        }
    }
}
