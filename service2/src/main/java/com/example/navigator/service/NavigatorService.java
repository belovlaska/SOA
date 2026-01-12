package com.example.navigator.service;

import com.example.navigator.client.Service1RestClient;
import com.example.navigator.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NavigatorService {

    @Autowired
    private Service1RestClient service1RestClient;

    public List<RouteDto> getRoutes(Long idFrom, Long idTo, String orderBy) {
        try {
            List<RouteDto> allRoutes = service1RestClient.getRoutes();

            if (allRoutes == null || allRoutes.isEmpty()) {
                return List.of();
            }


            List<RouteDto> sorted = allRoutes.stream()
                    .sorted(getComparator(orderBy))
                    .collect(Collectors.toList());

            return sorted;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch routes from service 1: " + e.getMessage(), e);
        }
    }

    /**
     * Компаратор для сортировки
     * Поддерживает: id, name, distance, creationDate
     * С префиксом "-" для DESC (убывание)
     */
    private Comparator<RouteDto> getComparator(String orderBy) {
        String field = orderBy;
        boolean descending = false;

        if (orderBy != null && orderBy.startsWith("-")) {
            field = orderBy.substring(1);
            descending = true;
        }

        Comparator<RouteDto> comparator = switch (field) {
            case "id" -> Comparator.comparing(RouteDto::getId);
            case "name" -> Comparator.comparing(RouteDto::getName);
            case "distance" -> Comparator.comparing(RouteDto::getDistance,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            case "creationDate" -> Comparator.comparing(RouteDto::getCreationDate,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            default -> Comparator.comparing(RouteDto::getId);
        };

        return descending ? comparator.reversed() : comparator;
    }

    public RouteDto addRoute(Long idFrom, Long idTo, Long distance) {
        try {
            RouteCreateRequestDto dto = new RouteCreateRequestDto();
            dto.setName("Route from " + idFrom + " to " + idTo);
            dto.setDistance(distance);

            CoordinatesDto coords = new CoordinatesDto();
            coords.setX(idFrom);
            coords.setY((float) idTo);
            dto.setCoordinates(coords);

            LocationDto from = new LocationDto();
            from.setX(idFrom.intValue());
            from.setY(idFrom);
            from.setZ(idFrom);
            from.setName("Location " + idFrom);
            dto.setFrom(from);

            LocationDto to = new LocationDto();
            to.setX(idTo.intValue());
            to.setY(idTo);
            to.setZ(idTo);
            to.setName("Location " + idTo);
            dto.setTo(to);

            return service1RestClient.createRoute(dto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add route: " + e.getMessage(), e);
        }
    }
}
