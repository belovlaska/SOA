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
@CrossOrigin(origins = "*", maxAge = 3600)
public class RouteController {

    @Autowired
    private RouteService routeService;

    /**
     * GET /routes/distancedistinct
     * Получить все уникальные значения дистанций
     */
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

    /**
     * GET /routes/distancelt/{value}
     * Получить кол-во маршрутов с distance < value
     */
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

    /**
     * DELETE /routes/distance/{value}
     * Удалить все маршруты с дистанцией == value
     */
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

    // ============ ОСНОВНЫЕ ENDPOINTS ============

    /**
     * POST /routes
     * Создать новый маршрут
     */
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

    /**
     * GET /routes
     * Получить все маршруты с фильтрацией, сортировкой и пагинацией
     *
     * Параметры:
     * - page: номер страницы (default: 0)
     * - size: размер страницы (default: 20)
     * - sort: поле сортировки (id, name, distance, creationDate, + "-" для DESC)
     * - id: точный ID маршрута
     * - name: точное имя маршрута
     * - namelike: имя содержит (LIKE)
     * - distance: точная дистанция (>= 2)
     * - distancegt: дистанция > значение (> means greater than)
     * - distancegte: дистанция >= значение (>= means greater than or equal)
     * - distancelt: дистанция < значение (< means less than)
     * - distancelte: дистанция <= значение (<= means less than or equal)
     * - creationDateBefore: дата создания ДО (ISO date format: YYYY-MM-DD)
     * - creationDateAfter: дата создания ПОСЛЕ (ISO date format: YYYY-MM-DD)
     * - fromName: точное имя точки FROM
     * - fromNamelike: имя FROM содержит (LIKE)
     * - toName: точное имя точки TO
     * - toNamelike: имя TO содержит (LIKE)
     */
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

    /**
     * GET /routes/{id}
     * Получить маршрут по ID
     */
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

    /**
     * PUT /routes/{id}
     * Обновить маршрут по ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoute(@PathVariable Long id, @Valid @RequestBody RouteUpdateDto dto) {
        try {
            Route route = routeService.updateRoute(id, dto);
            return ResponseEntity.ok(route);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponseDto(404, "Not Found: " + e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDto(400, "Bad Request: " + e.getMessage()));
        }
    }

    /**
     * DELETE /routes/{id}
     * Удалить маршрут по ID
     */
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
}
