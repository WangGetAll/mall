package com.wjy.user.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckTokenResponse {
    private List<String> aud;
    @JsonProperty("user_name")
    private String userName;
    private List<String> scope;
    private boolean active;
    private long exp;
    private List<String> authorities;
    @JsonProperty("client_id")
    private String clientId;
}

