package com.wjy.oauth2.mapper;

import com.wjy.oauth2.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper  {

    @Select("select user_name, passwd, user_role from user where user_name = #{userName}")
    User findUserByUserName(String userName);
}
