package com.wjy.common.response;

public enum ResponseCode {
    SUCCESS("00000", "成功"),
    UNAUTHORIZED("A0301", "访问未授权"),
    TOKEN_NOT_ACTIVE("A0340","用户签名无效"),
    ALREADY_REGISTER("A0111", "用户名已存在"),
    CODE_WRONG("A0131", "短信验证码错误"),
    USER_NOT_EXIT("A0201","用户账户不存在"),
    PARAM_WRONG("A0400", "请求参数错误"),
    URL_WRONG("A0401","跳转地址错误"),
    APP_NAME_EXIT("A0402", "应用名称已存在");


    private final String code;
    private final String message;

    private ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
