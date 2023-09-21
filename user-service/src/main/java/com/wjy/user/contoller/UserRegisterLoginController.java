package com.wjy.user.contoller;

import com.wjy.common.response.CommonResponse;
import com.wjy.common.response.ResponseUtil;
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

    @PostMapping("/name-password")
    public CommonResponse registerWithUsernameAndPassword(@RequestBody User user) {
        user = userRegisterLoginService.registerWithUsernameAndPassword(user);
        return ResponseUtil.okResponse(user);
    }

    @PostMapping("/login")
    public CommonResponse loginWithUsernameAndPassword(@RequestParam String username,
                                                       @RequestParam String password) {
        LoginResponse loginResponse = userRegisterLoginService.loginWithUsernameAndPassword(username, password);
        return ResponseUtil.okResponse(loginResponse);
    }

    @PostMapping("/phone-code")
    public CommonResponse loginOrRegisterWithPhoneAndCode(@RequestParam String phoneNumber,
                                                       @RequestParam String code) {
        LoginResponse loginResponse =
                userRegisterLoginService.loginOrRegisterWithPhoneAndCode(phoneNumber, code);

        return ResponseUtil.okResponse(loginResponse);
    }

    @GetMapping("/gitee")
    public CommonResponse loginOrRegisterWithGitee(HttpServletRequest request) {
        LoginResponse loginResponse = userRegisterLoginService.loginOrRegisterWithGitee(request);
        return ResponseUtil.okResponse(loginResponse);
    }

}
