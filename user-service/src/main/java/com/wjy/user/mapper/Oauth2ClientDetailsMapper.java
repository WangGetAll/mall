package com.wjy.user.mapper;

import com.wjy.user.pojo.OAuth2Client;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface Oauth2ClientDetailsMapper {
    @Select("select 1 from oauth_client_details where client_id = #{clientId} limit 1")
    public Integer findByClientId(String clientId);

    @Insert("insert into oauth_client_details(client_id, client_secret, resource_ids, authorized_grant_types, scope, authorities) " +
            "values (#{clientId}, #{clientSecret}, #{resourceIds}, #{authorizedGrantTypes}, #{scope}, #{authorities})")
    public void insert(OAuth2Client oAuth2Client);
}
