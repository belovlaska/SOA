package com.example.route.service;

import com.example.route.dto.PageResponseDto;
import com.example.route.dto.RouteCreateDto;
import com.example.route.dto.RouteUpdateDto;
import com.example.route.model.Route;
import com.example.route.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private EntityManager entityManager;

    public Route createRoute(RouteCreateDto dto) {
        Route route = new Route();
        route.setName(dto.getName());
        route.setCoordinates(dto.getCoordinates());
        route.setCreationDate(LocalDate.now());
        route.setFrom(dto.getFrom());
        route.setTo(dto.getTo());
        route.setDistance(dto.getDistance());
        return routeRepository.save(route);
    }

    public PageResponseDto<Route> getAllRoutes(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", "20"));
        String sort = params.getOrDefault("sort", "id");

        List<Route> routes = routeRepository.findAll();
        routes = filterRoutes(routes, params);
        routes = sortRoutes(routes, sort);

        int totalPages = (int) Math.ceil((double) routes.size() / size);
        int start = page * size;
        int end = Math.min(start + size, routes.size());
        List<Route> paged = routes.subList(start, end);

        return new PageResponseDto<>(paged, totalPages);
    }

    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }

    public Route updateRoute(Long id, RouteUpdateDto dto) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        route.setName(dto.getName());
        route.setCoordinates(dto.getCoordinates());
        route.setFrom(dto.getFrom());
        route.setTo(dto.getTo());
        route.setDistance(dto.getDistance());
        return routeRepository.save(route);
    }

    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }

    public long deleteByDistance(Long value) {
        List<Route> toDelete = routeRepository.findByDistance(value);
        routeRepository.deleteAll(toDelete);
        return toDelete.size();
    }

    public long countByDistanceLessThan(Long value) {
        return routeRepository.findByDistance(value).stream()
                .filter(r -> r.getDistance() != null && r.getDistance() < value)
                .count();
    }

    public List<Long> getDistinctDistances() {
        return routeRepository.findDistinctDistances();
    }

    private List<Route> filterRoutes(List<Route> routes, Map<String, String> params) {
        if (params.containsKey("id")) {
            Long id = Long.parseLong(params.get("id"));
            routes = routes.stream().filter(r -> r.getId().equals(id)).collect(Collectors.toList());
        }
        if (params.containsKey("name")) {
            String name = params.get("name");
            routes = routes.stream().filter(r -> r.getName().equals(name)).collect(Collectors.toList());
        }
        if (params.containsKey("namelike")) {
            String namelike = params.get("namelike");
            routes = routes.stream().filter(r -> r.getName().toLowerCase().contains(namelike.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (params.containsKey("distance")) {
            Long distance = Long.parseLong(params.get("distance"));
            routes = routes.stream().filter(r -> r.getDistance() != null && r.getDistance().equals(distance))
                    .collect(Collectors.toList());
        }
        if (params.containsKey("distancegt")) {
            Long distance = Long.parseLong(params.get("distancegt"));
            routes = routes.stream().filter(r -> r.getDistance() != null && r.getDistance() > distance)
                    .collect(Collectors.toList());
        }
        if (params.containsKey("distancegte")) {
            Long distance = Long.parseLong(params.get("distancegte"));
            routes = routes.stream().filter(r -> r.getDistance() != null && r.getDistance() >= distance)
                    .collect(Collectors.toList());
        }
        if (params.containsKey("distancelt")) {
            Long distance = Long.parseLong(params.get("distancelt"));
            routes = routes.stream().filter(r -> r.getDistance() != null && r.getDistance() < distance)
                    .collect(Collectors.toList());
        }
        if (params.containsKey("distancelte")) {
            Long distance = Long.parseLong(params.get("distancelte"));
            routes = routes.stream().filter(r -> r.getDistance() != null && r.getDistance() <= distance)
                    .collect(Collectors.toList());
        }
        if (params.containsKey("creationDatebefore")) {
            LocalDate date = LocalDate.parse(params.get("creationDatebefore"));
            routes = routes.stream().filter(r -> r.getCreationDate().isBefore(date))
                    .collect(Collectors.toList());
        }
        if (params.containsKey("creationDateafter")) {
            LocalDate date = LocalDate.parse(params.get("creationDateafter"));
            routes = routes.stream().filter(r -> r.getCreationDate().isAfter(date))
                    .collect(Collectors.toList());
        }
        if (params.containsKey("fromName")) {
            String fromName = params.get("fromName");
            routes = routes.stream().filter(r -> r.getFrom() != null && r.getFrom().getName().equals(fromName))
                    .collect(Collectors.toList());
        }
        if (params.containsKey("fromNamelike")) {
            String fromNamelike = params.get("fromNamelike");
            routes = routes.stream()
                    .filter(r -> r.getFrom() != null && r.getFrom().getName().toLowerCase().contains(fromNamelike.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (params.containsKey("toName")) {
            String toName = params.get("toName");
            routes = routes.stream().filter(r -> r.getTo() != null && r.getTo().getName().equals(toName))
                    .collect(Collectors.toList());
        }
        if (params.containsKey("toNamelike")) {
            String toNamelike = params.get("toNamelike");
            routes = routes.stream()
                    .filter(r -> r.getTo() != null && r.getTo().getName().toLowerCase().contains(toNamelike.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return routes;
    }

    private List<Route> sortRoutes(List<Route> routes, String sort) {
        String[] sortParts = sort.split(",");
        for (String part : sortParts) {
            boolean desc = part.startsWith("-");
            String field = desc ? part.substring(1) : part;

            switch (field) {
                case "id":
                    routes.sort(Comparator.comparing(Route::getId));
                    break;
                case "name":
                    routes.sort(Comparator.comparing(Route::getName));
                    break;
                case "distance":
                    routes.sort(Comparator.comparing(r -> r.getDistance() == null ? Long.MAX_VALUE : r.getDistance()));
                    break;
                case "creationDate":
                    routes.sort(Comparator.comparing(Route::getCreationDate));
                    break;
            }

            if (desc) {
                Collections.reverse(routes);
            }
        }
        return routes;
    }
}
