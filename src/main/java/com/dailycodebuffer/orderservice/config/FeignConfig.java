package com.dailycodebuffer.orderservice.config;

import com.dailycodebuffer.orderservice.external.decoder.CustomErrorDecoder;
import feign.codec.ErrorDecoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    ErrorDecoder errorDecoder () {
        return new CustomErrorDecoder();  // Implement your custom error decoder here
    }

}
