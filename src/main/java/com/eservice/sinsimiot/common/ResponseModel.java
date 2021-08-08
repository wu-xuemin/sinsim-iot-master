package com.eservice.sinsimiot.common;

/**
 * @author zt
 */
public class ResponseModel {
    private String message;

    private String result;
    private String data;
    private Boolean success;

    private int rtn;
    private int code;
    private String msg;
    private int total;

    ///success    fail
    private String status;
    private String errorCode;
    private String datas;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRtn() {
        return rtn;
    }

    public String getMessage() {
        return message;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setRtn(int rtn) {
        this.rtn = rtn;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
