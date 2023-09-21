package com.wjy.oauth2.filter;


import com.alibaba.fastjson.JSONObject;
import com.wjy.common.response.CommonResponse;
import com.wjy.common.response.ResponseCode;
import com.wjy.common.response.ResponseUtil;
import com.wjy.oauth2.mapper.OAuth2ClientDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@WebFilter(filterName = "OauthFilter")
public class OauthFilter implements Filter {
    @Autowired
    private OAuth2ClientDetailsMapper OAuth2ClientDetailsMapper;

    private String path = "/oauth/authorize";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("拦截到了");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();
        // 不是要拦截的直接放行
        if (!requestURI.equals(path))  {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 请求的跳转路径和数据库中存储的不一样拦截
        String requestRedirectUrl = request.getParameter("redirect_url");
        System.out.println("requestRedirectUrl = " + requestRedirectUrl);
        String clientId = request.getParameter("client_id");
        String redirectUrl = OAuth2ClientDetailsMapper.findRedirectUriByClientId(clientId);
        System.out.println("redirectUrl = " + redirectUrl);
        if (!redirectUrl.equals(requestRedirectUrl)) {
            CommonResponse commonResponse = ResponseUtil.failResponse(null, ResponseCode.URL_WRONG);
            String json = JSONObject.toJSONString(commonResponse);
            returnJson(servletResponse, json);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
    private void returnJson(ServletResponse servletResponse, String json) {
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("application/json");
        try (PrintWriter writer = servletResponse.getWriter();) {
            writer.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
