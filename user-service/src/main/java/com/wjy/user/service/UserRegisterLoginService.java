package com.wjy.user.service;


import com.alibaba.fastjson.JSONObject;
import com.wjy.common.response.CustomException;
import com.wjy.common.response.ResponseCode;
import com.wjy.user.config.GiteeConfig;
import com.wjy.user.enumeration.AuthorizedGrantType;
import com.wjy.user.enumeration.RegisterType;
import com.wjy.user.mapper.Oauth2ClientDetailsMapper;
import com.wjy.user.mapper.UserMapper;
import com.wjy.user.pojo.OAuth2Client;
import com.wjy.user.pojo.User;
import com.wjy.user.processor.RedisProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Service
public class UserRegisterLoginService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Oauth2ClientDetailsMapper oauth2ClientDetailsMapper;

    @Autowired
    private RedisProcessor redisProcessor;

    @Autowired
    private GiteeConfig giteeConfig;

    @Transactional
    public User registerWithUsernameAndPassword(User user) {
        // 已经注册
        if (userMapper.findByUserName(user.getUserName()) != null
                || oauth2ClientDetailsMapper.findByClientId(user.getUserName()) != null) {
            User userByUserName = userMapper.findUserByUserName(user.getUserName());
           throw new CustomException(ResponseCode.ALREADY_REGISTER);
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);

        OAuth2Client oAuth2Client = OAuth2Client.builder()
                .clientId(user.getUserName())
                .clientSecret(encodePassword)
                .resourceIds(RegisterType.USER_PASSWORD.name()) //此用户是如何注册的
                .authorizedGrantTypes(AuthorizedGrantType.refresh_token.name().concat(",").concat(AuthorizedGrantType.password.name())) // 此用户可以通过什么方式获取token
                .scope("web")
                .authorities(RegisterType.USER_PASSWORD.name())
                .build();
        // user信息和client信息存储到mysql
        saveUserAndOAuth2ClientToDB(user, oAuth2Client);
        user.setPassword(null);
        return user;
    }
    public User loginWithUsernameAndPassword(String username, String password) {
        User user = userMapper.findUserByUserName(username);
        if (user == null) {
            throw new CustomException(ResponseCode.USER_NOT_EXIT);
        }
        saveUserToRedis(user);
        return  user;
    }


    @Transactional
    public User loginOrRegisterWithPhoneAndCode(String phoneNumber, String code) {
        // 已经注册
        if (userMapper.findByPhoneNumber(phoneNumber) != null) {
           User user = userMapper.findUserByPhoneNumber(phoneNumber);
           saveUserToRedis(user);
           return user;
        }
        // 短息验证码错误
        String cacheCode = String.valueOf(redisProcessor.get(phoneNumber));
        if (StringUtils.isEmpty(cacheCode) || !cacheCode.equals(code)) {
            throw new CustomException(ResponseCode.CODE_WRONG);
        }
        // 注册
        User user = User.builder()
                .userName(getDefaultUserName(phoneNumber))
                .password("")
                .userPhone(phoneNumber)
                .userRole(RegisterType.PHONE_NUMBER.name())
                .build();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(code);
        OAuth2Client oAuth2Client = OAuth2Client.builder()
                .clientId(phoneNumber)
                .clientSecret(encode)
                .resourceIds(RegisterType.PHONE_NUMBER.name())
                .authorizedGrantTypes(AuthorizedGrantType.refresh_token.name()
                        .concat(",").concat(AuthorizedGrantType.client_credentials.name()))
                .scope("web")
                .authorities(RegisterType.PHONE_NUMBER.name())
                .build();
            saveUserAndOAuth2ClientToDB(user, oAuth2Client);
            saveUserToRedis(user);
        return user;
    }

    @Transactional
    public User loginOrRegisterWithGitee(HttpServletRequest request) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        if (!giteeConfig.getState().equals(state)) {
            throw new CustomException(ResponseCode.PARAM_WRONE);
        }
        String tokenUrl = String.format(giteeConfig.getTokenUrl(),
                giteeConfig.getClientId(), giteeConfig.getClientSecret(),
                giteeConfig.getCallBack(), code);
        RestTemplate restTemplate = new RestTemplate();
        JSONObject jsonObject = restTemplate.postForObject(tokenUrl, null, JSONObject.class);
        String accessToken = jsonObject.get("access_token").toString();
        String userUrl = String.format(giteeConfig.getUserUrl(), accessToken);
        JSONObject userInfo = restTemplate.getForObject(userUrl, JSONObject.class);
        String username = giteeConfig.getState().concat(userInfo.get("name").toString());
        // 已经注册
        if (userMapper.findByUserName(username) != null)  {
            User user = userMapper.findUserByUserName(username);
            saveUserToRedis(user);
            return user;
        }
        User user = User.builder()
                .userName(username)
                .password("")
                .userRole(RegisterType.THRID_PART.name())
                .build();
        OAuth2Client oAuth2Client = OAuth2Client.builder()
                .clientId(username)
                .clientSecret(new BCryptPasswordEncoder().encode(username))
                .resourceIds(RegisterType.THRID_PART.name())
                .authorizedGrantTypes(AuthorizedGrantType.refresh_token.name()
                        .concat(",").concat(AuthorizedGrantType.client_credentials.name()))
                .scope("web")
                .authorities(RegisterType.THRID_PART.name())
                .build();
        saveUserAndOAuth2ClientToDB(user, oAuth2Client);
        saveUserToRedis(user);
        return user;
    }
    // 用手机号注册时，生成一个默认的用户名
    private String getDefaultUserName(String phoneNumber) {
        return System.currentTimeMillis() + phoneNumber.substring(phoneNumber.length() - 4);
    }


    private void saveUserAndOAuth2ClientToDB(User user, OAuth2Client oAuth2Client) {
        userMapper.insert(user);
        oauth2ClientDetailsMapper.insert(oAuth2Client);
    }

    private void saveUserToRedis(User user) {
        Integer id = user.getId();
        String personId = id + 10000000 + "";
        redisProcessor.set(personId, user, 30,TimeUnit.DAYS);
    }


}
