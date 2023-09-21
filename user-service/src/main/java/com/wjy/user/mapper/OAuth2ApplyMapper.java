package com.wjy.user.mapper;

import com.wjy.user.pojo.OAuth2Apply;
import com.wjy.user.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OAuth2ApplyMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into oauth_apply(app_name, main_page, callback_url, logo, client_id, client_secret, approve, user_id) " +
            "values (#{appName}, #{mainPage}, #{callbackUrl}, #{logo}, #{clientId}, #{clientSecret}, #{approve}, #{userId})")
    void insert(OAuth2Apply OAuth2Apply);


    @Select("select app_name, main_page, callback_url, logo, client_id, client_secret, approve " +
            "from oauth_apply where user_id = #{userId}")
    List<OAuth2Apply> ListOauth2ApplyByUserId(Integer userId);

    @Select("select callback_url, client_id, client_secret from oauth_apply where app_name = #{appName}")
    OAuth2Apply getOAuth2ApplyByAppName(String apppName);

    @Select("select 1 from oauth_apply where app_name = #{appName}")
    Integer getByAppName(String appName);

    @Update("update oauth_apply set approve = 1 where app_name = #{appName}")
    void updateOAuth2ApplyByAppName(String appName);
}
