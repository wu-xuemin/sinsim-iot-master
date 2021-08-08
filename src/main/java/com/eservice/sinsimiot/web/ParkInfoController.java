package com.eservice.sinsimiot.web;

import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
import com.eservice.sinsimiot.core.Constant;
import com.eservice.sinsimiot.core.MessageConstant;
import com.eservice.sinsimiot.core.OperationConstant;
import com.eservice.sinsimiot.model.park_info.ParkInfo;
import com.eservice.sinsimiot.model.park_info.ParkInfoDTO;
import com.eservice.sinsimiot.model.tag.TagDTO;
import com.eservice.sinsimiot.service.LogRecordService;
import com.eservice.sinsimiot.service.ParkInfoService;
import com.eservice.sinsimiot.service.TagInfoService;
import com.eservice.sinsimiot.util.JwtUtil;
import com.eservice.sinsimiot.util.Util;
import com.google.common.base.Joiner;
import com.eservice.sinsimiot.model.tag.TagInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Mr.xie
 * @date 2021/05/07.
 */
@RestController
@RequestMapping("/park/info")
public class ParkInfoController {

    private final String MODULE = "园区管理";
    @Resource
    private LogRecordService logRecordService;

    @Resource
    private ParkInfoService parkInfoService;
    @Resource
    private TagInfoService tagInfoService;


    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 园区管理
     * @title 新增
     * @description 新增园区
     * @method POST
     * @url http://10.250.62.84:9010/api/park/info/add
     * @header token 必选 string token
     * @json_param {"parkName":"园区名称","unit":"单位(标签id)"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败   接口IP为:http://10.250.62.84:9010
     * @number 1.0
     */
    @PostMapping("/add")
    public Result add(@RequestHeader String token, @RequestBody @NotNull ParkInfo parkInfo) {
        String account = JwtUtil.getAccount(token);
        ParkInfo isExit = parkInfoService.findBy("parkName", parkInfo.getParkName());
        if (isExit != null) {
            return ResultGenerator.genFailResult("park is exit");
        }
        if (parkInfoService.save(parkInfo)) {
            logRecordService.save(account, MODULE, OperationConstant.ADD_CN, String.format("园区：%s，新增成功", parkInfo.getParkName()));
            return ResultGenerator.genSuccessResult(parkInfo);
        }
        return ResultGenerator.genFailResult(MessageConstant.ADD_FAILED_CN);
    }


    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 园区管理
     * @title 标签新增
     * @description 园区新增标签
     * @method POST
     * @url http://10.250.62.84:9010/api/park/info/tag/add
     * @header token 必选 string token
     * @json_param {"tagName":"标签名称","parkId":"园区id"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败   接口IP为:http://10.250.62.84:9010
     * @number 1.0
     */
    @PostMapping("/tag/add")
    public Result tagAdd(@RequestHeader String token, @RequestBody TagDTO tagDTO) {
        String account = JwtUtil.getAccount(token);
        if (tagDTO != null) {
            TagInfo tagInfo = tagInfoService.findBy("tagName", tagDTO.getTagName());
            if (tagInfo != null) {
                return ResultGenerator.genFailResult(MessageConstant.EXIST_CN);
            } else {
                tagInfo = new TagInfo();
            }
            tagInfo.setTagName(tagDTO.getTagName());
            tagInfo.setTagType(Constant.STAFF);
            if (tagInfoService.save(tagInfo)) {
                logRecordService.save(account, MODULE, OperationConstant.ADD_CN, String.format("名称：%s，新增成功", tagInfo.getTagName()));
                ParkInfo parkInfo = parkInfoService.findById(tagDTO.getParkId());
                if (parkInfo == null) {
                    return ResultGenerator.genFailResult(MessageConstant.DOES_NOT_EXIST_CN);
                } else {
                    List<String> tagIds = Util.stringToArrayList(parkInfo.getUnit(), ",");
                    if (tagIds == null) {
                        new ArrayList<>();
                        tagIds.add(tagInfo.getTagId());
                    } else {
                        tagIds.add(tagInfo.getTagId());
                    }
                    parkInfo.setUnit(Joiner.on(",").join(tagIds));
                    if (parkInfoService.update(parkInfo)) {
                        logRecordService.save(account, MODULE, OperationConstant.ADD_CN, String.format("园区：%s，新增成功", tagInfo.getTagName()));
                        return ResultGenerator.genSuccessResult(parkInfo);
                    }
                }
            }
        }
        return ResultGenerator.genFailResult(MessageConstant.ADD_FAILED_CN);
    }

    /**
     * showdoc
     *
     * @param parkName 必选 string 园区名称
     * @return {"code":200,"message":"SUCCESS","data":false}
     * @remark data true 表述重复，false 表示不重复
     * @catalog 园区管理
     * @title 检查园区名称
     * @description 检查园区名称是否重复
     * @method GET
     * @url IP/api/park/info/check
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark data true 表述重复，false 表示不重复
     * @number 1.0
     */
    @GetMapping("/check")
    public Result check(@RequestParam String parkName) {
        ParkInfo parkInfo = parkInfoService.findBy("parkName", parkName);
        if (parkInfo != null) {
            return ResultGenerator.genSuccessResult(true);
        } else {
            return ResultGenerator.genSuccessResult(false);
        }
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 园区管理
     * @title 删除
     * @description 删除园区
     * @method DELETE
     * @url IP/api/park/info/delete/{id}
     * @header token 必选 string token}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@RequestHeader String token, @PathVariable String id) {
        String account = JwtUtil.getAccount(token);
        ParkInfo parkInfo = parkInfoService.findById(id);
        if (parkInfoService.deleteById(id)) {
            logRecordService.save(account, MODULE, OperationConstant.DELETE_CN, String.format("园区：%s，删除成功", parkInfo.getParkName()));
            return ResultGenerator.genSuccessResult(id);
        }
        return ResultGenerator.genFailResult(MessageConstant.DELETE_FAILED_CN);
    }


    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 园区管理
     * @title 修改
     * @description 修改园区
     * @method PUT
     * @url IP/api/park/info/update
     * @header token 必选 string token}
     * @json_param {"parkName":"园区名称","unit":"单位(标签id)"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @PutMapping("/update")
    public Result update(@RequestHeader String token, @RequestBody @NotNull ParkInfo parkInfo) {
        String account = JwtUtil.getAccount(token);
        if (parkInfoService.update(parkInfo)) {
            logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("园区：%s，修改成功", parkInfo.getParkName()));
            return ResultGenerator.genSuccessResult(parkInfo);
        }
        return ResultGenerator.genFailResult(MessageConstant.UPDATE_FAILED_CN);
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable @NotNull String id) {
        ParkInfo parkInfo = parkInfoService.findById(id);
        return ResultGenerator.genSuccessResult(parkInfo);
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":{"serialVersionUID":0,"pageNum":0,"pageSize":0,"size":0,"orderBy":"str","startRow":0,"endRow":0,"total":0,"pages":0,"list":[{"parkId": "f35c9b5f-8d0e-42c7-86ec-0a70dcd2bf21","parkName": "粤园","unit": "237966e8-93df-46d3-b139-bcb276ecd87b","createTime": 1620364827,"updateTime": null}],"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":true,"hasNextPage":true,"navigatePages":0,"navigatepageNums":"int[]","navigateFirstPage":0,"navigateLastPage":0}}
     * @catalog 园区管理
     * @title 搜索
     * @description 条件搜索
     * @method POST
     * @url IP/api/park/info/list
     * @header token 必选 string token
     * @json_param {"tagIds":["str"],"name":"str","page":0,"size":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @PostMapping("/list")
    public Result list(@RequestBody ParkInfoDTO parkInfoDTO) {
        PageHelper.startPage(parkInfoDTO.getPage(), parkInfoDTO.getSize());
        List<ParkInfo> list = parkInfoService.findByCondition(parkInfoDTO);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":{"serialVersionUID":0,"pageNum":0,"pageSize":0,"size":0,"orderBy":"str","startRow":0,"endRow":0,"total":0,"pages":0,"list":[{"tagId":"str","tagName":"str","type":"str","createTime":1587182365077,"updateTime":1587182365077}],"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":true,"hasNextPage":true,"navigatePages":0,"navigatepageNums":"int[]","navigateFirstPage":0,"navigateLastPage":0}}
     * @catalog 园区管理
     * @title 根据园区id获取标签信息
     * @description 根据园区id获取标签信息
     * @method POST
     * @url IP/api/park/info/tag
     * @header token 必选 string token
     * @json_param {"parkId":"str","page":1,"size":10}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/tag", method = RequestMethod.POST)
    public Result getParkTag(@RequestBody ParkInfoDTO parkInfoDTO) {
        if (parkInfoDTO != null) {
            ParkInfo parkInfo = parkInfoService.findById(parkInfoDTO.getParkId());
            if (parkInfo != null) {
                PageHelper.startPage(parkInfoDTO.getPage(), parkInfoDTO.getSize());
                List<TagInfo> tagInfos = tagInfoService.selectTagInfoByTagId(Util.stringToArrayList(parkInfo.getUnit(), ","));
                PageInfo pageInfo = new PageInfo(tagInfos);
                return ResultGenerator.genSuccessResult(pageInfo);
            } else {
                return ResultGenerator.genFailResult("not found parkInfo");
            }
        } else {
            return ResultGenerator.genFailResult("parkId is null");
        }
    }
}
