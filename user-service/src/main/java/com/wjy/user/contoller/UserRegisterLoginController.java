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
        String rawPassword = user.getPassword();
        LoginResult loginResult = userRegisterLoginService.registerWithUsernameAndPassword(user);
        if (!loginResult.isSuccess()) {
            return ResponseUtil.failResponse(null, loginResult.getResponseCode());
        }
        TokenResponse tokenResponse = oAuth2ServiceClient.getToken(AuthorizedGrantType.password.name(),
                user.getUserName(),rawPassword,user.getUserName(),rawPassword);
        return ResponseUtil.okResponse(new LoginResponse(user, tokenResponse));
    }

    @PostMapping("/phone-code")
    public CommonResponse loginOrRegisterWithPhoneAndCode(@RequestParam String phoneNumber,
                                                       @RequestParam String code) {
        LoginResult registerResult = userRegisterLoginService.loginOrRegisterWithPhoneAndCode(phoneNumber, code);
        if (!registerResult.isSuccess()) {
            return ResponseUtil.failResponse(null, registerResult.getResponseCode());
        }
        TokenResponse tokenResponse = oAuth2ServiceClient.getToken(AuthorizedGrantType.client_credentials.name(),
                null, null, phoneNumber, code);

        return ResponseUtil.okResponse(new LoginResponse(registerResult.getUser(), tokenResponse));
    }

    @GetMapping("/gitee")
    public CommonResponse loginOrRegisterWithGitee(HttpServletRequest request) {
        LoginResult loginResult = userRegisterLoginService.loginOrRegisterWithGitee(request);
        User user = loginResult.getUser();
        TokenResponse tokenResponse = oAuth2ServiceClient.getToken(AuthorizedGrantType.client_credentials.name(),
                    null, null, user.getUserName(), user.getUserName());
        return ResponseUtil.okResponse(new LoginResponse(loginResult.getUser(),tokenResponse));
    }


    @PostMapping("/login")
    public CommonResponse loginWithUsernameAndPassword(@RequestParam String username,
                                @RequestParam String password) {
        LoginResult loginResult = userRegisterLoginService.loginWithUsernameAndPassword(username, password);
        if (!loginResult.isSuccess()) {
            return ResponseUtil.failResponse(null, loginResult.getResponseCode());
        } else {
            User user = loginResult.getUser();
            TokenResponse tokenResponse = oAuth2ServiceClient.getToken(AuthorizedGrantType.password.name(),
                    username  , password, username, password);
            return ResponseUtil.okResponse(new LoginResponse(loginResult.getUser(), tokenResponse));
        }
    }
}
