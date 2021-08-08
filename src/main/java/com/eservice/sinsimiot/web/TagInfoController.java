package com.eservice.sinsimiot.web;

import com.eservice.sinsimiot.service.ParkInfoService;
import com.eservice.sinsimiot.service.TagInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
import com.eservice.sinsimiot.core.MessageConstant;
import com.eservice.sinsimiot.core.OperationConstant;
import com.eservice.sinsimiot.model.staff.StaffSearchDTO;
import com.eservice.sinsimiot.model.tag.TagInfo;
import com.eservice.sinsimiot.service.LogRecordService;
import com.eservice.sinsimiot.service.StaffInfoService;
import com.eservice.sinsimiot.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Code Generator
 * @date 2020/03/06.
 */
@RestController
@RequestMapping("/tag/info")
public class TagInfoController {

    private final String MODULE = "标签管理";

    @Resource
    private TagInfoService tagInfoService;
    @Resource
    private LogRecordService logRecordService;
    @Resource
    private ParkInfoService parkInfoService;
    @Resource
    private StaffInfoService staffInfoService;

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 标签管理
     * @title 新增
     * @description 新增标签
     * @method POST
     * @url IP/api/tag/info/add
     * @header token 必选 string token
     * @json_param {"tagName":"标签名称","tagType":"标签类型"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestHeader("token") String token, @RequestBody @NotNull TagInfo tagInfo) {
        String account = JwtUtil.getAccount(token);
        if (tagInfoService.save(tagInfo)) {
            logRecordService.save(account, MODULE, OperationConstant.ADD_CN, String.format("名称：%s，类型：%s，新增成功", tagInfo.getTagName(), tagInfo.getTagType()));
            return ResultGenerator.genSuccessResult(tagInfo);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.ADD_CN, String.format("名称：%s，类型：%s，新增失败", tagInfo.getTagName(), tagInfo.getTagType()));
            return ResultGenerator.genFailResult(MessageConstant.ADD_FAILED_CN);
        }
    }

    /**
     * showdoc
     *
     * @param tagId 必选 string 标签id（tagId）
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 标签管理
     * @title 删除
     * @description 删除指定标签
     * @method DELETE
     * @url IP/api/tag/info/delete/{tagId}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @RequestMapping(value = "/delete/{tagId}", method = RequestMethod.DELETE)
    public Result delete(@RequestHeader("token") String token, @PathVariable String tagId) {
        String account = JwtUtil.getAccount(token);
        TagInfo tagInfo = tagInfoService.findById(tagId);
        if (tagInfo == null) {
            return ResultGenerator.genSuccessResult(tagId, MessageConstant.DOES_NOT_EXIST_CN);
        }
        if (tagInfoService.deleteById(tagId)) {
            parkInfoService.updateParkByTagId(tagId);
            staffInfoService.updateStaffInfoByTag(tagId);
            logRecordService.save(account, MODULE, OperationConstant.DELETE_CN, String.format("名称：%s，类型：%s，删除成功", tagInfo.getTagName(), tagInfo.getTagType()));
            return ResultGenerator.genSuccessResult();
        } else {
            logRecordService.save(account, MODULE, OperationConstant.DELETE_CN, String.format("名称：%s，类型：%s，删除失败", tagInfo.getTagName(), tagInfo.getTagType()));
            return ResultGenerator.genFailResult(MessageConstant.DELETE_FAILED_CN);
        }
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":null}
     * @catalog 标签管理
     * @title 修改
     * @description 修改标签信息
     * @method POST
     * @url IP/api/tag/info/update
     * @header token 必选 string token
     * @json_param {"tagId","标签id","tagName":"标签名称","tagType":"标签类型"}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result update(@RequestHeader("token") String token, @RequestBody @NotNull TagInfo tagInfo) {
        String account = JwtUtil.getAccount(token);
        TagInfo oldTagInfo = tagInfoService.findById(tagInfo.getTagId());
        if (oldTagInfo == null) {
            return ResultGenerator.genFailResult(MessageConstant.DOES_NOT_EXIST_CN);
        }
        if (tagInfoService.update(tagInfo)) {
            logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("名称：%s，类型：%s，修改成功", tagInfo.getTagName(), tagInfo.getTagType()));
            return ResultGenerator.genSuccessResult(tagInfo);
        } else {
            logRecordService.save(account, MODULE, OperationConstant.UPDATE_CN, String.format("名称：%s，类型：%s，修改失败", oldTagInfo.getTagName(), oldTagInfo.getTagType()));
            return ResultGenerator.genFailResult(MessageConstant.DELETE_FAILED_CN);
        }
    }

    /**
     * showdoc
     *
     * @param tagId 必选 string 标签（tagId）
     * @return {"code":200,"message":"SUCCESS","data":{"tagId":"str","tagName":"str","tagType":"str","createTime":1587182365077,"updateTime":1587182365077}}
     * @catalog 标签管理
     * @title 详情
     * @description 标签详细信息
     * @method GET
     * @url IP/api/tag/info/detail/{tagId}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/detail/{tagId}", method = RequestMethod.GET)
    public Result detail(@PathVariable @NotNull String tagId) {
        TagInfo tagInfo = tagInfoService.findById(tagId);
        return ResultGenerator.genSuccessResult(tagInfo);
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":{"serialVersionUID":0,"pageNum":0,"pageSize":0,"size":0,"orderBy":"str","startRow":0,"endRow":0,"total":0,"pages":0,"list":[{"tagId":"str","tagName":"str","type":"str","createTime":1587182365077,"updateTime":1587182365077}],"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":true,"hasNextPage":true,"navigatePages":0,"navigatepageNums":"int[]","navigateFirstPage":0,"navigateLastPage":0}}
     * @catalog 标签管理
     * @title 搜索
     * @description 条件搜索
     * @method POST
     * @url IP/api/tag/info/search
     * @header token 必选 string token
     * @json_param {"condition":"str","tagIds":["str"],"areaIds":["str"],"tagType":"str","page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result search(@RequestBody @NotNull StaffSearchDTO staffSearchDTO) {
        PageHelper.startPage(staffSearchDTO.getPage(), staffSearchDTO.getLimit());
        List<TagInfo> list = tagInfoService.findByCondition(staffSearchDTO);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":[{"tagId":"str","tagName":"str","type":"str","createTime":1587182365077,"updateTime":1587182365077}]}
     * @catalog 标签管理
     * @title 标签列表
     * @description 获取全量标签列表
     * @method GET
     * @url IP/api/tag/info/list
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list() {
        List<TagInfo> list = tagInfoService.findAll();
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * showdoc
     *
     * @param tagName 必选 string 标签名称
     * @param tagType 必选 string 标签类型
     * @return {"code":200,"message":"SUCCESS","data":false}
     * @remark data true 表述重复，false 表示不重复
     * @catalog 标签管理
     * @title 检查标签名称
     * @description 检查标签名称在指定类型的是否重复
     * @method GET
     * @url IP/api/tag/info/check
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark data true 表述重复，false 表示不重复
     * @number 1.0
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public Result check(@RequestParam String tagName, @RequestParam String tagType) {
        return ResultGenerator.genSuccessResult(tagInfoService.checkTagName(tagName, tagType));
    }


}
