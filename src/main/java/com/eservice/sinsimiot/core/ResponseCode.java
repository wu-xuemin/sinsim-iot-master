package com.eservice.sinsimiot.core;


/**
 * @program:isc_service
 * @description:API网关返回错误码
 * @author:Mr.xie
 * @create:2020/6/29 17:21
 */
public class ResponseCode {

    public static final int OK = 200;

    public static final int YT_RESULT_FAIL = 1;

    public static final int YT_IMAGE_FAIL = -10000;

    public static final int YT_NO_FACE = -11000;

    public static final int SUCCESS_CODE = 200000;
    /**
     * Token失效
     */
    public static final int TOKEN_INVALID = 403;

    public static final String SUCCESS = "success";

    public static final int IMAGE_INVALID = -11001;

    public static final int success = 0;

    public static final String CHEKOUT_SUCCESS = "0";


}
