package com.wjy.user.pojo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2Apply {
    private Integer id;
    private String appName;
    private String mainPage;
    private String callbackUrl;
    private String logo;
    private String clientId;
    private String clientSecret;
    private Integer approve;
    private Integer userId;
}
