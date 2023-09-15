package com.wjy.common.response;

public class ResponseUtil {
    public static CommonResponse okResponse(Object content) {
        return CommonResponse.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .content(content)
                .build();
    }
    public static CommonResponse failResponse(Object content, ResponseCode responseCode) {
        return CommonResponse.builder()
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .content(content)
                .build();
    }
}
