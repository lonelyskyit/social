package com.tensquare.web;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebZuulFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";//前置过滤器
    }

    @Override
    public int filterOrder() {
        return 0;//执行的顺序，数字越小越先执行。
    }

    @Override
    public boolean shouldFilter() {
        return true;//是否启用过滤器
    }

    @Override
    public Object run() throws ZuulException {
        //过滤器的具体功能
        System.out.println("web前台网关，经过了zuul前置过滤器...");

        //目标：获取头信息，转发出去
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String authorizationHeader = request.getHeader("Authorization");

        if(null!=authorizationHeader){
            currentContext.addZuulRequestHeader("JwtAuthorization",authorizationHeader);
        }

        //放行
        return null;
    }
}
