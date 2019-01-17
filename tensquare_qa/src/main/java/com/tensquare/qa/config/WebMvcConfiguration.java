package com.tensquare.qa.config;

import com.tensquare.qa.web.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * WebMvc配置类
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    //注入JwtInterceptor
    @Autowired
    private JwtInterceptor jwtInterceptor;

    /**
     * WebMvc中添加拦截器
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                //添加需要拦截器拦截的资源
                .addPathPatterns("/**")
                ;
    }
}
