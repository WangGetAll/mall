package com.wjy.user.service;


import com.wjy.common.constant.Constants;
import com.wjy.common.response.CustomException;
import com.wjy.common.response.ResponseCode;
import com.wjy.user.enumeration.AuthorizedGrantType;
import com.wjy.user.mapper.OAuth2ApplyMapper;
import com.wjy.user.mapper.Oauth2ClientDetailsMapper;
import com.wjy.user.pojo.OAuth2Apply;
import com.wjy.user.pojo.OAuth2Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OAuth2ApplyService {
    @Autowired
    private OAuth2ApplyMapper oAuth2ApplyMapper;
    @Autowired
    private Oauth2ClientDetailsMapper oauth2ClientDetailsMapper;

    public void oAuth2Apply(String personId, OAuth2Apply oAuth2Apply){
        String appName = oAuth2Apply.getAppName();
        if (oAuth2ApplyMapper.getByAppName(appName) != null) {
            throw new CustomException(ResponseCode.APP_NAME_EXIT);
        }
        Integer userId = Integer.valueOf(personId) - Constants.OFFSET;
        oAuth2Apply.setUserId(userId);
        oAuth2Apply.setClientId(UUID.randomUUID().toString().replace("-", ""));
        oAuth2Apply.setClientSecret(UUID.randomUUID().toString().replace("-", ""));
        oAuth2Apply.setApprove(0);

        oAuth2ApplyMapper.insert(oAuth2Apply);
    }

    public List<OAuth2Apply> oAuth2ApplyStatus(String personId) {
        Integer userId = Integer.valueOf(personId) - Constants.OFFSET;
        List<OAuth2Apply> oAuth2Applies = oAuth2ApplyMapper.ListOauth2ApplyByUserId(userId);
        return oAuth2Applies;
    }

    @Transactional
    public void approveOAuth2Apply(String appName) {
        OAuth2Apply oAuth2Apply = oAuth2ApplyMapper.getOAuth2ApplyByAppName(appName);
        oAuth2ApplyMapper.updateOAuth2ApplyByAppName(appName);
        OAuth2Client oAuth2Client =
                OAuth2Client.builder()
                        .clientId(oAuth2Apply.getClientId())
                        .clientSecret(new BCryptPasswordEncoder().encode(oAuth2Apply.getClientSecret()))
                        .autoapprove("true")
                        .resourceIds(appName)
                        .scope("web")
                        .authorities(appName)
                        .webServerRedirectUri(oAuth2Apply.getCallbackUrl())
                        .authorizedGrantTypes(AuthorizedGrantType.refresh_token.name().concat(",")
                                .concat(AuthorizedGrantType.authorization_code.name()))
                        .build();
        oauth2ClientDetailsMapper.insert(oAuth2Client);
    }
}
