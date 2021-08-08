package com.eservice.sinsimiot.core;

/**
 * @author xyh
 * @version v1.0
 * @description TODO
 * @date 2021/5/13 11:45
 */
public class ParkException extends Exception {
    private Integer statusCode;

    private String msg;

    public ParkException() {

    }

    public ParkException(Integer code, String msg) {
        this.statusCode = code;
        this.msg = msg;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
