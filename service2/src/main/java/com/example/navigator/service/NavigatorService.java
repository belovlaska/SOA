package com.example.navigator.service;

import com.example.navigator.client.Service1RestClient;
import com.example.navigator.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NavigatorService {

    @Autowired
    private Service1RestClient service1RestClient;

    public List<RouteDto> getRoutes(Long idFrom, Long idTo, String orderBy) {
        // Пример логики: получить все маршруты и отфильтровать/отсортировать
        List<RouteDto> allRoutes = service1RestClient.getRoutes();
        // Здесь можно добавить фильтрацию по idFrom, idTo и сортировку по orderBy
        return allRoutes;
    }

    public RouteDto addRoute(Long idFrom, Long idTo, Long distance) {
        RouteCreateRequestDto dto = new RouteCreateRequestDto();
        dto.setName("Route from " + idFrom + " to " + idTo);
        dto.setDistance(distance);
        // Пример заполнения координат и локаций
        CoordinatesDto coords = new CoordinatesDto();
        coords.setX(0L);
        coords.setY(0.0f);
        dto.setCoordinates(coords);

        LocationDto from = new LocationDto();
        from.setX(0);
        from.setY(0L);
        from.setZ(0L);
        from.setName("From");
        dto.setFrom(from);

        LocationDto to = new LocationDto();
        to.setX(1);
        to.setY(1L);
        to.setZ(1L);
        to.setName("To");
        dto.setTo(to);

        return service1RestClient.createRoute(dto);
    }
}
