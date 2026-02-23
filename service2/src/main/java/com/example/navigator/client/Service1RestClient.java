package com.example.navigator.client;

import com.example.navigator.dto.RouteCreateRequestDto;
import com.example.navigator.dto.RouteDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ejb.Stateless;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Stateless
public class Service1RestClient {

    private static final Logger logger = Logger.getLogger(Service1RestClient.class.getName());

    /**
     * Базовый URL вызываемого сервиса.
     * По умолчанию совпадает с OpenAPI server.url (https://.../api),
     * но может быть переопределён через системное свойство service1.url.
     */
    private final String service1Url = System.getProperty("service1.url", "http://haproxy:80/api");

    private final ObjectMapper objectMapper;

    public Service1RestClient() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public List<RouteDto> getRoutes() {
        String url = service1Url + "/routes?page=0&size=1000";
        logger.info("Fetching routes from: " + url);

        try (CloseableHttpClient client = createHttpClient()) {
            HttpGet get = new HttpGet(url);
            get.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = client.execute(get)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());
                if (statusCode >= 200 && statusCode < 300) {
                    Map<String, Object> body = objectMapper.readValue(responseBody, Map.class);
                    if (body != null && body.containsKey("data")) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> dataList = (List<Map<String, Object>>) body.get("data");
                        if (dataList != null) {
                            return dataList.stream()
                                    .map(map -> objectMapper.convertValue(map, RouteDto.class))
                                    .toList();
                        }
                    }
                    return List.of();
                } else {
                    throw new RuntimeException("Failed to fetch routes, status: " + statusCode);
                }
            }
        } catch (Exception e) {
            logger.severe("Failed to fetch routes from service 1: " + e.getMessage());
            throw new RuntimeException("Failed to fetch routes from service 1: " + e.getMessage(), e);
        }
    }

    public RouteDto getRouteById(Long id) {
        String url = service1Url + "/routes/" + id;
        logger.info("Fetching route by id from: " + url);

        try (CloseableHttpClient client = createHttpClient()) {
            HttpGet get = new HttpGet(url);
            get.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = client.execute(get)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());
                if (statusCode >= 200 && statusCode < 300) {
                    Map<String, Object> body = objectMapper.readValue(responseBody, Map.class);
                    return objectMapper.convertValue(body, RouteDto.class);
                } else {
                    throw new RuntimeException("Failed to fetch route by id, status: " + statusCode);
                }
            }
        } catch (Exception e) {
            logger.severe("Failed to fetch route by id: " + e.getMessage());
            throw new RuntimeException("Failed to fetch route by id: " + e.getMessage(), e);
        }
    }

    public RouteDto createRoute(RouteCreateRequestDto dto) {
        String url = service1Url + "/routes";
        logger.info("Creating route at: " + url);

        try (CloseableHttpClient client = createHttpClient()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-Type", "application/json");

            String json = objectMapper.writeValueAsString(dto);
            post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8)));

            try (CloseableHttpResponse response = client.execute(post)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());
                if (statusCode >= 200 && statusCode < 300) {
                    Map<String, Object> body = objectMapper.readValue(responseBody, Map.class);
                    return objectMapper.convertValue(body, RouteDto.class);
                } else {
                    throw new RuntimeException("Failed to create route, status: " + statusCode);
                }
            }
        } catch (Exception e) {
            logger.severe("Failed to create route: " + e.getMessage());
            throw new RuntimeException("Failed to create route: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> readJsonAsMap(InputStream is) throws IOException {
        if (is == null) {
            return null;
        }
        return objectMapper.readValue(is, Map.class);
    }

    private CloseableHttpClient createHttpClient() throws Exception {
        // Загружаем truststore из classpath
        InputStream trustStoreStream = getClass().getResourceAsStream("/truststore.jks");

        if (trustStoreStream != null) {
            try (trustStoreStream) {
                KeyStore trustStore = KeyStore.getInstance("JKS");
                trustStore.load(trustStoreStream, "password".toCharArray());

                SSLContext sslContext = SSLContexts.custom()
                        .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                        .build();

                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                        sslContext,
                        new String[]{"TLSv1.2", "TLSv1.3"},
                        null,
                        SSLConnectionSocketFactory.getDefaultHostnameVerifier());

                return HttpClients.custom()
                        .setSSLSocketFactory(sslsf)
                        .build();
            }
        } else {
            logger.warning("Truststore not found, creating HTTP client without SSL validation");
            return HttpClients.createDefault();
        }
    }
}