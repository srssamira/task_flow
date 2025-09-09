package com.task_flow.task_flow.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class StackspotConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
