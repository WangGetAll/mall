package com.wjy.user.contoller;

import com.wjy.common.response.CommonResponse;
import com.wjy.common.response.ResponseUtil;
import com.wjy.user.enumeration.AuthorizedGrantType;
import com.wjy.user.feignclient.OAuth2ServiceClient;
import com.wjy.user.pojo.*;
import com.wjy.user.service.UserRegisterLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/user/register")
public class UserRegisterLoginController {

    @Autowired
    private UserRegisterLoginService userRegisterLoginService;

    @Autowired
    private OAuth2ServiceClient oAuth2ServiceClient;

    @PostMapping("/name-password")
    public CommonResponse registerWithUsernameAndPassword(@RequestBody User user) {
        user = userRegisterLoginService.registerWithUsernameAndPassword(user);
        return ResponseUtil.okResponse(user);
    }

    @PostMapping("/login")
    public CommonResponse loginWithUsernameAndPassword(@RequestParam String username,
                                                       @RequestParam String password) {
        User user = userRegisterLoginService.loginWithUsernameAndPassword(username, password);
        TokenResponse tokenResponse = oAuth2ServiceClient.getToken(AuthorizedGrantType.password.name(),
                    username, password, username, password);
        return ResponseUtil.okResponse(new LoginResponse(user, tokenResponse));

    }

    @PostMapping("/phone-code")
    public CommonResponse loginOrRegisterWithPhoneAndCode(@RequestParam String phoneNumber,
                                                       @RequestParam String code) {
        User user = userRegisterLoginService.loginOrRegisterWithPhoneAndCode(phoneNumber, code);
        TokenResponse tokenResponse = oAuth2ServiceClient.getToken(AuthorizedGrantType.client_credentials.name(),
                null, null, phoneNumber, code);

        return ResponseUtil.okResponse(new LoginResponse(user, tokenResponse));
    }

    @GetMapping("/gitee")
    public CommonResponse loginOrRegisterWithGitee(HttpServletRequest request) {
        User user = userRegisterLoginService.loginOrRegisterWithGitee(request);
        TokenResponse tokenResponse = oAuth2ServiceClient.getToken(AuthorizedGrantType.client_credentials.name(),
                    null, null, user.getUserName(), user.getUserName());
        return ResponseUtil.okResponse(new LoginResponse(user,tokenResponse));
    }
    // 手机号和gitee方式的注册登录，service中可以直接完成登录 1.如果未注册先注册， 2.用户信息存储到redis 3.getToken
    // 用户名密码方式，分开注册登录。登录:1.用户信息存储到redis 2.getToken
    // 感觉似乎应该先获取token后，再存储到redis。而且getToken方法应该在servcie中执行


}
