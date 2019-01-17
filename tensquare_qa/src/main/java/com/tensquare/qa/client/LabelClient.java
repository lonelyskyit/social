package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.LabelClientImpl;
import dto.ResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * feign调用标签微服务的接口
 */
//@FeignClient("tensquare-base")//调用某个微服务的名字，最终：DESKTOP-1CG46P9:tensquare-base:9001
@FeignClient(value="tensquare-base",
        //一旦微服务调不通，则使用本地默认实现处理
        fallback = LabelClientImpl.class)
public interface LabelClient {

    /**
     * 被调用的微服务的业务接口路径
     * @param id
     * @return
     */
    @GetMapping("/label/{id}")
    ResultDTO listById(@PathVariable("id") String id);
}
