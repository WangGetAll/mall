package com.wjy.user.feignclient;

import com.wjy.user.pojo.CheckTokenResponse;
import com.wjy.user.pojo.TokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient("oauth2-service")
public interface OAuth2ServiceClient {
    @PostMapping("/oauth/token")
    TokenResponse getToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret
    );
    @GetMapping("/oauth/check_token")
    CheckTokenResponse checkToken(@RequestParam String token);
}
