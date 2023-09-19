package com.wjy.user.contoller;

import com.wjy.common.response.CommonResponse;
import com.wjy.common.response.ResponseUtil;
import com.wjy.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/info")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("/check-phone-bind")
    public CommonResponse checkPhoneBindStatus  (@RequestHeader String personId){
        boolean isBind = userInfoService.checkPhoneBind(personId);
        return ResponseUtil.okResponse(isBind);
    }

    @RequestMapping("bind-phone")
    public CommonResponse bindPhone(@RequestHeader String personId,
                                    @RequestParam String phoneNumber,
                                    @RequestParam String code) {

        userInfoService.bindPhone(personId, phoneNumber, code);
        return ResponseUtil.okResponse(null);
    }

}
