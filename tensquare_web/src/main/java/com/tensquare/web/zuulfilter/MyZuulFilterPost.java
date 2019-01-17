package com.tensquare.web.zuulfilter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

@Component
public class MyZuulFilterPost extends ZuulFilter {
    @Override
    public String filterType() {
        return "post";//前置过滤器
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
        System.out.println("web前台网关，经过了zuul后置过滤器...");
        return null;
    }
}
