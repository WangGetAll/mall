package com.wjy.sms.controller;

import com.wjy.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;
    @RequestMapping("/send-code")
    public void sendCodeToPhone(@RequestParam String phoneNumber) {
        smsService.sendCodeToPhone(phoneNumber);
    }


}
