package com.tensquare.qa.web.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt的拦截器
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {
    //注入JwtUtil
    @Autowired
    private JwtUtil jwtUtil;
    /**
     * 前置拦截器
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("经过了JwtInterceptor拦截器...");
        //获取头信息，约定头信息key为Authorization
        final String authorizationHeader = request.getHeader("JwtAuthorization");
        System.out.println("------------------------");
        System.out.println("JwtAuthorization："+authorizationHeader);
        System.out.println("Authorization："+request.getHeader("Authorization"));

        //判断authorizationHeader不为空,并且是"Bearer "开头的
        if(null !=authorizationHeader && authorizationHeader.startsWith("Bearer ")){
            //获取令牌，The part after "Bearer "
            final String token=authorizationHeader.substring(7);
            //获取载荷
            Claims claims = null;
            try {
                claims = jwtUtil.parseJWT(token);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //判断载荷是否为空
            if(null != claims){
                //判断令牌中的自定义载荷中的角色是否是admin（管理员）
                if("admin".equals(claims.get("roles"))){
                    request.setAttribute("admin_claims",claims);
                }
                //判断令牌中的自定义载荷中的角色是否是user（普通用户）
                if("user".equals(claims.get("roles"))){
                    request.setAttribute("user_claims",claims);
                }
            }
        }
        return true;
    }
}
