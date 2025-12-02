package com.example.route.controller;

import com.example.route.dto.*;
import com.example.route.model.Route;
import com.example.route.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping
    public ResponseEntity<?> createRoute(@Valid @RequestBody RouteCreateDto dto, HttpServletRequest request) {
        try {
            Route route = routeService.createRoute(dto);
            String location = request.getRequestURL().toString() + "/" + route.getId();
            return ResponseEntity.created(URI.create(location)).body(route);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponseDto(409, "Conflict: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRoutes(@RequestParam Map<String, String> params) {
        try {
            PageResponseDto<Route> response = routeService.getAllRoutes(params);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDto(400, "Bad Request: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRouteById(@PathVariable Long id) {
        try {
            Optional<Route> route = routeService.getRouteById(id);
            return route.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDto(404, "Not Found: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoute(@PathVariable Long id, @Valid @RequestBody RouteUpdateDto dto) {
        try {
            Route route = routeService.updateRoute(id, dto);
            return ResponseEntity.ok(route);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponseDto(404, "Not Found: " + e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDto(400, "Bad Request: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoute(@PathVariable Long id) {
        try {
            Optional<Route> route = routeService.getRouteById(id);
            if (route.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponseDto(404, "Not Found"));
            }
            routeService.deleteRoute(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(500, "Server Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/distance/{value}")
    public ResponseEntity<?> deleteByDistance(@PathVariable Long value) {
        try {
            if (value < 2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponseDto(400, "Distance must be >= 2"));
            }
            long deletedCount = routeService.deleteByDistance(value);
            return ResponseEntity.ok(new DeletedCountResponseDto(deletedCount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDto(400, "Bad Request: " + e.getMessage()));
        }
    }

    @GetMapping("/distancelt/{value}")
    public ResponseEntity<?> countByDistanceLessThan(@PathVariable Long value) {
        try {
            if (value < 2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponseDto(400, "Distance must be >= 2"));
            }
            long count = routeService.countByDistanceLessThan(value);
            return ResponseEntity.ok(new CountResponseDto(count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDto(400, "Bad Request: " + e.getMessage()));
        }
    }

    @GetMapping("/distancedistinct")
    public ResponseEntity<?> getDistinctDistances() {
        try {
            List<Long> distances = routeService.getDistinctDistances();
            return ResponseEntity.ok(distances);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(500, "Server Error: " + e.getMessage()));
        }
    }
}
