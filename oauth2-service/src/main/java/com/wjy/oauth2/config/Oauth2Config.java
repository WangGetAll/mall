package com.wjy.oauth2.config;

import com.wjy.oauth2.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;


@Configuration
@EnableAuthorizationServer // 启用OAuth2授权服务器的功能，用于颁发令牌和管理OAuth2客户端
public class Oauth2Config extends AuthorizationServerConfigurerAdapter {
    // OAuth2 认证服务器所需的数据源
    @Autowired
    private DataSource dataSource;

    // 用于加载用户详细信息的实现类
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // 用于处理身份认证请求的认证管理器
    @Autowired
    private AuthenticationManager authenticationManager;

    // 注入用于令牌存储的类
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    // 注入用于令牌服务的类
    @Bean
    @Primary
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setAccessTokenValiditySeconds(30 * 24 * 60 * 60);
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    // 注入密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 注入用于客户详情存储的类
    @Bean
    public ClientDetailsService clientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    // 配置客户详情存储的类
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
    }

    // 配置 OAuth2 授权服务器的安全性
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients() // 允许客户端进行表单认证
                .checkTokenAccess("permitAll()");    // 设置检查令牌的访问权限
    }
    // 配置 OAuth2 授权服务器的端点
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.userDetailsService(userDetailsService)  // 用户详细信息服务、。
                .tokenServices(defaultTokenServices())   //  令牌服务
                .authenticationManager(authenticationManager); // 身份验证管理器
    }
}
