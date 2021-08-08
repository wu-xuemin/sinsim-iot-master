package com.eservice.sinsimiot.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
import com.eservice.sinsimiot.model.park.DeviceInfo;
import com.eservice.sinsimiot.model.park.DeviceSearchDTO;
import com.eservice.sinsimiot.service.park.DeviceService;
import com.eservice.sinsimiot.util.Util;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xyh
 * @version v1.0
 * @description TODO
 * @date 2021/5/24 15:10
 */
@RequestMapping("/device/info")
@RestController
public class DeviceInfoController {

    @Resource
    private DeviceService deviceService;


    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":{"serialVersionUID":0,"pageNum":0,"pageSize":0,"size":0,"orderBy":"str","startRow":0,"endRow":0,"total":0,"pages":0,"list":[ {"dept_id": null,"device_id": "F0:41:C8:C0:07:AD","device_meta": {"direction_type": 0,"location": "吴园","type": 2,"device_name": "吴园","specification": "STK1W","version": "3.0.4","ip": "","port": 8443,"video_definition_type": null,"username": "","password": "","enable": true,"docking_config": null,"map_id": null,"position": null},"deviceStatus": {"device_add_time": "1619705776","status": "ERROR","status_detail": "DISCONNECT","status_description": "","last_capture_timestamp": 0,"last_heart_beat_timestamp": 0},"connection_type": "ACTIVE"}],"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":true,"hasNextPage":true,"navigatePages":0,"navigatepageNums":"int[]","navigateFirstPage":0,"navigateLastPage":0}}
     * @catalog 设备管理
     * @title 查询
     * @description 设备查询
     * @method POST
     * @url IP/api/device/info/list
     * @header token 必选 string token
     * @json_param {"parkId":"园区ID","condition":"条件","page":0,"limit":0}
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @PostMapping("/list")
    public Result list(@RequestBody DeviceSearchDTO deviceSearchDTO) {
        List<DeviceInfo> deviceInfos = deviceService.getDeviceInfoList(deviceSearchDTO);
        if (deviceInfos != null) {
            PageHelper.startPage(deviceSearchDTO.getPage(), deviceSearchDTO.getLimit());
            PageInfo pageInfo = new PageInfo(deviceInfos);
            pageInfo.setList(Util.pagingQuery(deviceSearchDTO.getPage(), deviceSearchDTO.getLimit(), deviceInfos));
            return ResultGenerator.genSuccessResult(pageInfo);
        } else {
            return ResultGenerator.genFailResult("no data");
        }
    }

}
