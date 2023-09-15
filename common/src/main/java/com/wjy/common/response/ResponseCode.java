package com.wjy.common.response;

public enum ResponseCode {
    SUCCESS(200, "success"),
    BAD_REQUEST(400, "bad request"),
    UNAUTHORIZED(401, "unauthorized"),
    ALREADY_REGISTER(601, "already register"),
    CODE_WRONG(602, "code wrong"),
    CODE_EXPIRE(603, "code expire"),
    USER_NOT_EXIT(604,"user not exit");


    private final Integer code;
    private final String message;

    private ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
