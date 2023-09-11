package com.wjy.gateway.filter;

import com.wjy.gateway.feignclient.Oauth2ServiceClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {
    @Lazy
    @Autowired
    private Oauth2ServiceClient oauth2ServiceClient;

    // 存放可以直接放行的路径（正则表达式）
    private Set<String> permitPaths = new ConcurrentSkipListSet<>();
    // 正则校验器
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();


    public AuthenticationFilter() {
        this.permitPaths.add("/**/oauth/**");
        this.permitPaths.add("/**/user/register/**");
    }


    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        // 特殊路径直接放行
        if (checkPermit(path)) {
            return chain.filter(exchange);
        }

        String token = request.getHeaders().getFirst("Authorization");
        // Map<String, Object> result = oauth2ServiceClient.checkToken(token);
        // 异步调用checkToken
        CompletableFuture<Map<String, Object>> future = CompletableFuture.supplyAsync(() -> oauth2ServiceClient.checkToken(token));
        Map<String, Object> result = future.get();
        boolean active = (boolean) result.get("active");
        // 如果无效
        if (!active) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        return chain.filter(exchange);
    }

    // 值小的过滤器在过滤器链的前面
    @Override
    public int getOrder() {
        return 0;
    }

    private boolean checkPermit(String requestPath) {
        return permitPaths.stream()
                .anyMatch(permitPath -> ANT_PATH_MATCHER.match(permitPath, requestPath));

    }
}
