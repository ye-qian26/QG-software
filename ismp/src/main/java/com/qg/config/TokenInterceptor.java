package com.qg.config;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.TimeUnit;

import static com.qg.domain.Code.UNAUTHORIZED;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    /**
     * 在请求处理之前调用（Controller方法调用之前）
     */
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 放行登录/注册等白名单
        String uri = request.getRequestURI();
        /*if (uri.startsWith("/auth/login") || uri.startsWith("/auth/register")) {
            return true;
        }*/

        // 2. 验证 Token
        String token = request.getHeader("Authorization");
        //System.out.println(token);

        if (token == null || !stringRedisTemplate.hasKey("login:user:" + token)) {
            response.setStatus(UNAUTHORIZED);
            response.getWriter().write("Token无效");
            return false;
        }

        // 3. 刷新 Token 有效期
        stringRedisTemplate.expire("login:user:" + token, 30, TimeUnit.MINUTES);
        return true;
    }

}
