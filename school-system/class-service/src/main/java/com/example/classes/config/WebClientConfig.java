package com.example.classes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient userServiceWebClient() {
        // We configure the base URL for the user-service.
        // "http://user-service:8081" is the address inside the Docker network.
        return WebClient.builder().baseUrl("http://user-service:8081").build();
    }
}