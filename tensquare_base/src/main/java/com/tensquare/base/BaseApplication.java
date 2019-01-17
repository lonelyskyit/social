package com.tensquare.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import utils.IdWorker;

@SpringBootApplication//组合主
@EnableEurekaClient// 键
public class BaseApplication {
    //入口
    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class,args);
    }

    /**
     * id生成器
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        return  new IdWorker(1,1);
    }
}
