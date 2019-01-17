package com.tensquare.manager;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerZuulFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    //注入jwtutil
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Object run() throws ZuulException {
        System.out.println("经过了后台管理网站的网关过滤器");

        //目标；获取token，进行校验，如果通过，转发到具体微服务，如果不通过，则直接响应回去。
        //获取token
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String authorizationHeader = request.getHeader("Authorization");


        //2. 其他请求处理
        //1）处理CORS跨域请求，因为跨域请求的第一次请求是预请求，不带头信息的，因此要过滤掉。
        if(request.getMethod().equals("OPTIONS")){
            System.out.println("跨域的第一次请求，直接放行");
            return null;
        }
        //2）处理一些特殊请求，比如登录请求，就直接放行
        //获取请求的URL
        String url=request.getRequestURL().toString();
        //如果是管理员登录
        if(url.indexOf("/admin/login")>0){
            System.out.println("管理员的登录请求，直接放行："+url);
            return null;
        }

        //判断authorizationHeader不为空,并且是"Bearer "开头的
        if(null !=authorizationHeader && authorizationHeader.startsWith("Bearer ")) {
            //获取令牌，The part after "Bearer "
            final String token = authorizationHeader.substring(7);
            //获取载荷
            Claims claims = null;
            try {
                claims = jwtUtil.parseJWT(token);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //是否是管理员身份
            if(null!=claims && "admin".equals(claims.get("roles"))){
                //是管理员身份，则
                //头信息转发
                currentContext.addZuulRequestHeader("JwtAuthorization",authorizationHeader);
                //放行
                return null;
            }
        }

        //没有管理员身份，要直接响应，不再继续转发
        //是否继续转发
        currentContext.setSendZuulResponse(false);
        //无权访问该资源的状态码
        currentContext.setResponseStatusCode(401);
        //设置响应编码
        currentContext.getResponse().setContentType("text/html;charset=UTF-8");
        //给响应写点东西
//        currentContext.getResponse().getWriter().print("您无权访问");
        currentContext.setResponseBody("您无权访问");
        //结束
        return null;
    }
}
