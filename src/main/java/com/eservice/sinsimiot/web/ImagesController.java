package com.eservice.sinsimiot.web;

import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
import com.eservice.sinsimiot.util.ImageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName imageController
 * @Description 图片管理
 * @Author silent
 * @Date 2021/4/16 上午9:50
 */
@RestController
@RequestMapping("/images")
public class ImagesController {

    @Value("${path.images}")
    private String IMAGES_PATH;

    /**
     * showdoc
     *
     * @return {"code":200,"message":"SUCCESS","data":"图片id"}
     * @json_param {"base64":"图片base64"}
     * @catalog 图片管理
     * @title 图片上传
     * @description 接收上传图片，并检查是否合格
     * @method POST
     * @url IP/api/images/quality/verify
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark
     * @number 1.0
     */
    @RequestMapping(value = "/quality/verify", method = RequestMethod.POST)
    public Result qualityVerify(@RequestBody Map<String, String> map) {
        try {
            String image = map.get("base64");
            if (image != null) {
                String uuid = UUID.randomUUID().toString();
                if (ImageUtil.saveBase64ToFile(IMAGES_PATH, image, uuid)) {
                    return ResultGenerator.genSuccessResult(uuid);
                } else {
                    return ResultGenerator.genFailResult("图片保存失败");
                }
            } else {
                return ResultGenerator.genFailResult("获取图片base64值，为空");
            }
        } catch (Exception e) {
            return ResultGenerator.genFailResult("获取上传图片失败");
        }
    }


    /**
     * showdoc
     *
     * @param imageId 必选 人员faceId
     * @return {"code":200,"message":"SUCCESS","data":"base64"}
     * @catalog 图片管理
     * @title 图片查询
     * @description 获取对应人员照片base64
     * @method GET
     * @url IP/api/images/{imageId}
     * @header token 必选 string token
     * @return_param code  Integer 状态码
     * @return_param message String 提示信息
     * @return_param data Object 返回数据
     * @remark 返回的状态码：200 成功，其他为失败
     * @number 1.0
     */
    @RequestMapping(value = "/{imageId}", method = RequestMethod.GET)
    public Result image(@PathVariable String imageId) {
        if (imageId != null) {
            File file = new File(IMAGES_PATH + imageId);
            String imageBase64 = ImageUtil.getFileBase64(file);
            if (imageBase64 != null) {
                return ResultGenerator.genSuccessResult(imageBase64);
            }
        }
        return ResultGenerator.genFailResult("Not Found");
    }

}
