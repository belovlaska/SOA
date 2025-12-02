package com.example.navigator.client;

import com.example.navigator.dto.RouteCreateRequestDto;
import com.example.navigator.dto.RouteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;

@Component
public class Service1RestClient {

    @Value("${service1.url:https://localhost:8443}")
    private String service1Url;

    @Autowired
    private RestTemplate restTemplate;

    public List<RouteDto> getRoutes() {
        try {
            String url = service1Url + "/routes";
            RouteDto[] routes = restTemplate.getForObject(url, RouteDto[].class);
            return Arrays.asList(routes != null ? routes : new RouteDto[0]);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch routes from service 1: " + e.getMessage());
        }
    }

    public RouteDto getRouteById(Long id) {
        try {
            String url = service1Url + "/routes/" + id;
            return restTemplate.getForObject(url, RouteDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch route by id: " + e.getMessage());
        }
    }

    public RouteDto createRoute(RouteCreateRequestDto dto) {
        try {
            String url = service1Url + "/routes";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<RouteCreateRequestDto> entity = new HttpEntity<>(dto, headers);
            return restTemplate.postForObject(url, entity, RouteDto.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to create route: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create route: " + e.getMessage());
        }
    }
}
