package com.wjy.common.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonResponse {
    private Integer code;
    private String message;
    private Object content;
}
