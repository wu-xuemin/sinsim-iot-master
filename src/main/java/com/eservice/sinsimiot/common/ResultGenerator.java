package com.eservice.sinsimiot.common;

import com.eservice.sinsimiot.common.ResultCode;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public static Result genSuccessResult() {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static Result genSuccessResult(Object data) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result genSuccessResult(Object data, String message) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(message)
                .setData(data);
    }

    public static Result genPassWordFailResult(String message) {
        return new Result()
                .setCode(ResultCode.PASSWORD_ERROR)
                .setMessage(message);
    }

    public static Result genFailResult(String message) {
        return new Result()
                .setCode(ResultCode.FAIL)
                .setMessage(message);
    }

    public static Result genExcelReadFailResult(String message) {
        return new Result()
                .setCode(ResultCode.EXCEL_READ_ERROR)
                .setMessage(message);
    }

    public static Result genExcelImportFailResult(String message) {
        return new Result()
                .setCode(ResultCode.EXCEL_IMPORT_FAIL)
                .setMessage(message);
    }

}
