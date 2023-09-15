package com.wjy.sms.service;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.wjy.sms.config.TencentSmsConfig;
import com.wjy.sms.processor.RedisProcessor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SmsService {
    @Autowired
    private TencentSmsConfig tencentSmsConfig;

    @Autowired
    private RedisProcessor redisProcessor;

    @SneakyThrows
    public void sendCodeToPhone(String phoneNumber) {
        Credential credential = new Credential(tencentSmsConfig.getSecretId(), tencentSmsConfig.getSecretKey());
        SmsClient smsClient = new SmsClient(credential, tencentSmsConfig.getRegion());
        SendSmsRequest request = new SendSmsRequest();
        request.setSmsSdkAppId(tencentSmsConfig.getAppId());
        request.setSignName(tencentSmsConfig.getSignName());
        request.setTemplateId(tencentSmsConfig.getTemplateId().getPhoneCode());
        
        String code = getRandomCode();
        String[] templateParamSet  = {code};
        request.setTemplateParamSet(templateParamSet);

        String[] phoneNumberSet = {phoneNumber};
        request.setPhoneNumberSet(phoneNumberSet);

        smsClient.SendSms(request);
        redisProcessor.set(phoneNumber, code, 5, TimeUnit.MINUTES);
    }

    private String getRandomCode() {
        return String.valueOf((Math.random() * 9 + 1) * 1000000);
    }
}
