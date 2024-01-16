package com.elhg.usuario.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MyConfig {

    @Bean
    @LoadBalanced
    //https://www.geeksforgeeks.org/spring-boot-loadbalanced-annotation-with-example/
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
