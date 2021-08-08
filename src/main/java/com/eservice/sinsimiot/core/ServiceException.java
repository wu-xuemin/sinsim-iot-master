package com.eservice.sinsimiot.core;

/**
* @description :  自定义错误
* @author : silent
*/
public class ServiceException extends RuntimeException {

    private Integer statusCode ;

    private String msg;

    public ServiceException() {}

    public ServiceException(Integer statusCode, String message) {
            this.statusCode = statusCode;
            this.msg = message;
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMsg() {
        return msg;
    }
}
