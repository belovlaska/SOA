package com.example.navigator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.net.ssl.SSLContext;
import java.security.KeyStore;

@Configuration
public class WebConfig implements WebMvcConfigurer {
//
//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) throws Exception {
//        // Загрузи truststore
//        KeyStore trustStore = KeyStore.getInstance("JKS");
//        trustStore.load(
//                getClass().getResourceAsStream("/truststore.jks"),
//                "password".toCharArray()
//        );
//
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//        sslContext.init(null, null, null);
//
//        return builder.build();
//    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());  // ← ДОБАВЬ ЭТО
        return mapper;
    }
}
