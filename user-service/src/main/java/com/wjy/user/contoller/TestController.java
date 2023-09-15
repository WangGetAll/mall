package com.wjy.user.contoller;

import com.wjy.user.feignclient.OAuth2ServiceClient;
import com.wjy.user.pojo.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    @Autowired
    private OAuth2ServiceClient oAuth2ServiceClient;


    @GetMapping("/test")
    public String test() {
        return "hello";
    }

    @PostMapping("/test/feign")
    public TokenResponse test( @RequestParam("grant_type") String grantType,
                               @RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("client_id") String clientId,
                               @RequestParam("client_secret") String clientSecret) {
        TokenResponse token = oAuth2ServiceClient.getToken(grantType, username, password, clientId, clientSecret);
        return token;
    }

}
