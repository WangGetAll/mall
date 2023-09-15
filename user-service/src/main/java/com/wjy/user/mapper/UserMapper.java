package com.wjy.user.mapper;


import com.wjy.user.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select 1 from user where user_name = #{username} limit 1")
    public Integer findByUserName(String username);

    @Select("select id,user_name,user_role, user_email, user_idcard, user_phone, user_province from user where user_name = #{username}")
    public User findUserByUserName(String username);


    @Select("select 1 from user where user_phone = #{phoneNumber} limit 1")
    public Integer findByPhoneNumber(String phoneNumber);


    @Select("select id,user_name,user_role,user_email, user_idcard, user_phone, user_province from user where user_phone = #{phoneNumber}")
    User findUserByPhoneNumber(String phoneNumber);


    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into user(user_name, passwd, user_role) " +
            "values (#{userName}, #{password}, #{userRole})")
    public void insert(User user);


}
