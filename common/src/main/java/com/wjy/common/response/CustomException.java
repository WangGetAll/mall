package com.wjy.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private ResponseCode responseCode;
}
