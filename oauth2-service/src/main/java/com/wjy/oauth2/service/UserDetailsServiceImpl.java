package com.wjy.oauth2.service;

import com.wjy.common.response.CustomException;
import com.wjy.common.response.ResponseCode;
import com.wjy.oauth2.pojo.User;
import com.wjy.oauth2.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findUserByUserName(username);
        if (user == null) {
            throw new CustomException(ResponseCode.USER_NOT_EXIT);
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(),
                user.getPasswd(), AuthorityUtils.createAuthorityList("test"));
    }
}
