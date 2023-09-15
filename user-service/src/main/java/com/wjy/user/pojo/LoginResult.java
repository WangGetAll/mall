package com.wjy.user.pojo;

import com.wjy.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class LoginResult {
    private boolean success;
    private ResponseCode responseCode;
    private User user;

    public LoginResult(boolean success, ResponseCode responseCode) {
        this.success = success;
        this.responseCode = responseCode;
    }
}
