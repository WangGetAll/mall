package com.wjy.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tencent.sms.template-id")
public class TencentSmsTemplateIdConfig {
    private String phoneCode;
    private String sales;
}
