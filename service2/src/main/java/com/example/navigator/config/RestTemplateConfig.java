package com.example.navigator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
public class RestTemplateConfig {

    @Value("${server.ssl.trust-store}")
    private Resource trustStore;

    @Value("${server.ssl.trust-store-password}")
    private String trustStorePassword;

    @Bean
    public RestTemplate restTemplate() throws Exception {
        // Load the truststore
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (InputStream is = trustStore.getInputStream()) {
            keyStore.load(is, trustStorePassword.toCharArray());
        }

        // Create SSL context with the truststore
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(keyStore, null)
                .build();

        // Create HTTP client with custom SSL context
        HttpClient httpClient = HttpClients.custom()
                .setSSLContext(sslContext)
                .build();

        // Create request factory with HTTP client
        ClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return new RestTemplate(factory);
    }
}
