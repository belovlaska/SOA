package com.example.navigator.client;

import com.example.navigator.dto.RouteCreateRequestDto;
import com.example.navigator.dto.RouteDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class Service1RestClient {

    private static final Logger logger = Logger.getLogger(Service1RestClient.class.getName());

    @Value("${service1.url:https://localhost:13223}")
    private String service1Url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public List<RouteDto> getRoutes() {
        try {
            String url = service1Url + "/routes?page=0&size=1000";
            logger.info("Fetching routes from: " + url);

            // Получить ответ как Map (без типизации)
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("data")) {
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.get("data");

                if (dataList != null) {
                    // Конвертировать List<Map> в List<RouteDto>
                    return dataList.stream()
                            .map(map -> objectMapper.convertValue(map, RouteDto.class))
                            .toList();
                }
            }
            return List.of();
        } catch (Exception e) {
            logger.severe("Failed to fetch routes from service 1: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch routes from service 1: " + e.getMessage(), e);
        }
    }

    public RouteDto getRouteById(Long id) {
        try {
            String url = service1Url + "/routes/" + id;
            logger.info("Fetching route by id from: " + url);

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            return objectMapper.convertValue(response, RouteDto.class);
        } catch (Exception e) {
            logger.severe("Failed to fetch route by id: " + e.getMessage());
            throw new RuntimeException("Failed to fetch route by id: " + e.getMessage(), e);
        }
    }

    public RouteDto createRoute(RouteCreateRequestDto dto) {
        try {
            String url = service1Url + "/routes";
            logger.info("Creating route at: " + url);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<RouteCreateRequestDto> entity = new HttpEntity<>(dto, headers);

            Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);
            return objectMapper.convertValue(response, RouteDto.class);
        } catch (HttpClientErrorException e) {
            logger.severe("HTTP Error creating route: " + e.getStatusCode() + " - " + e.getMessage());
            throw new RuntimeException("Failed to create route: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.severe("Failed to create route: " + e.getMessage());
            throw new RuntimeException("Failed to create route: " + e.getMessage(), e);
        }
    }
}
