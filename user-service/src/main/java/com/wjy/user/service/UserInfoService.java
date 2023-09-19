package com.wjy.user.service;

import com.wjy.common.constant.Constants;
import com.wjy.common.response.CustomException;
import com.wjy.common.response.ResponseCode;
import com.wjy.user.mapper.UserMapper;
import com.wjy.user.pojo.User;
import com.wjy.user.processor.RedisProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserInfoService {
    @Autowired
    private RedisProcessor redisProcessor;
    @Autowired
    private UserMapper userMapper;
    public boolean checkPhoneBind(String personId) {
        User user = (User) redisProcessor.get(personId);
        if (user != null) {
            System.out.println("redis中有user");
            return user.getUserPhone() != null;
        }
        System.out.println("redis中没有user");
        User userById = userMapper.findUserById(Integer.valueOf(personId) - Constants.OFFSET);
        System.out.println("userById = " + userById);
        if (userById == null) {
            throw new CustomException(ResponseCode.USER_NOT_EXIT);
        }
        redisProcessor.set(personId, userById);
        return userById.getUserPhone() != null;
    }

    public void bindPhone(String personId, String phoneNumber, String code) {
        String cacheCode = redisProcessor.get(phoneNumber).toString();
        if (StringUtils.isEmpty(cacheCode) || !cacheCode.equals(code)) {
            throw new CustomException(ResponseCode.CODE_WRONG);
        }
        Integer id = Integer.valueOf(personId) - Constants.OFFSET;
        if (userMapper.findById(id) == null) {
            throw new CustomException(ResponseCode.USER_NOT_EXIT);
        }
        userMapper.updateUserPhoneById(id, phoneNumber);
    }
}
