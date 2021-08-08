package com.eservice.sinsimiot.core;

/**
 * @author : silent
 * @description :  HTTPStatus
 */
public enum ResultCode {
    SUCCESS(200, "处理成功"),
    FAIL(400, "处理失败"),
    NOT_FOUND(404, "接口不存在"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    PASSWORD_ERROR(601, "密码错误"),
    EXCEL_READ_ERROR(602, "表格读取错误"),
    EXCEL_IMPORT_FAIL(603,"部分人员导入失败"),
    TOKEN_INVALID(407, "Token无效");

    private final int value;

    private final String reasonPhrase;

    ResultCode(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int getValue() {
        return value;
    }


    public String getReasonPhrase() {
        return reasonPhrase;
    }

}
