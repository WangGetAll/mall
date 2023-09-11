package com.wjy.oauth2.config;

import com.wjy.oauth2.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity //启用Spring Security的Web安全性功能
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // 注入用于处理身份认证请求的认证管理器
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 配置AuthenticationManagerBuilder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new PasswordEncoder() {
                    @Override
                    public String encode(CharSequence rawPassword) {
                        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                        return bCryptPasswordEncoder.encode(rawPassword);
                    }

                    @Override
                    public boolean matches(CharSequence rawPassword, String encodedPassword) {
                        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
                    }
                });
    }

    // 配置了应用程序的 HTTP安全性
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated() // 要求所有请求都需要身份验证。
                .and().httpBasic() //启用基本身份验证，允许用户通过用户名和密码进行认证。
                .and().cors()   // 启用跨域资源共享（CORS）支持，以允许跨域请求
                .and().csrf().disable(); // 禁用 CSRF（跨站请求伪造）保护，通常在无状态的 RESTful API 中使用。
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/swagger-*"); // /swagger- 开头的路径不需要身份验证
    }
}
