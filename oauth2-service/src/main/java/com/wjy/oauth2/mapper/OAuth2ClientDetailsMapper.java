package com.wjy.oauth2.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OAuth2ClientDetailsMapper {
    @Select("select web_server_redirect_uri from oauth_client_details where client_id = #{clientId}")
    String findRedirectUriByClientId(String clientId);
}
