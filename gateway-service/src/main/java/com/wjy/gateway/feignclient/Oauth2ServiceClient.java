package com.wjy.gateway.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("oauth2-service")
public interface Oauth2ServiceClient {
    @RequestMapping("/oauth/check_token")
    Map<String, Object> checkToken(@RequestParam("token") String token);
}
