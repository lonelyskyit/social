package com.tensquare.qa;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import utils.IdWorker;
import utils.JwtUtil;

@SpringBootApplication
@EnableEurekaClient
//用来标识开启服务发现功能
@EnableDiscoveryClient
//用来开启Feign功能
@EnableFeignClients
public class ProblemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProblemApplication.class, args);
	}

	@Bean
	public IdWorker idWorker(){
		return new IdWorker(1, 1);
	}
	@Bean
	public JwtUtil jwtUtil(){
		return new utils.JwtUtil();
	}
}
