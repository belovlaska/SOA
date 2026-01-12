package com.example.navigator.controller;

import com.example.navigator.dto.ErrorResponseDto;
import com.example.navigator.dto.RouteDto;
import com.example.navigator.service.NavigatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/navigator")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NavigatorController {

    @Autowired
    private NavigatorService navigatorService;

    /**
     * GET /navigator
     * Информационный endpoint
     */
    @GetMapping
    public ResponseEntity<String> info() {
        return ResponseEntity.ok("HELLO");
    }

    /**
     * GET /navigator/routes/{fromId}/{toId}/{orderBy}
     * Найти маршруты между двумя точками
     *
     * @param fromId - ID точки отправления
     * @param toId - ID точки назначения
     * @param orderBy - поле сортировки (id, name, distance, creationDate)
     */
    @GetMapping("/routes/{fromId}/{toId}/{orderBy}")
    public ResponseEntity<?> getRoutes(@PathVariable Long fromId,
                                       @PathVariable Long toId,
                                       @PathVariable String orderBy) {
        try {
            // Валидация orderBy
            if (!orderBy.matches("^-?(id|name|distance|creationDate)$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponseDto(400, "Bad Request: Invalid orderBy parameter. " +
                                "Valid values: id, name, distance, creationDate (use '-' prefix for DESC)"));
            }

            List<RouteDto> routes = navigatorService.getRoutes(fromId, toId, orderBy);
            return ResponseEntity.ok(routes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDto(400, "Bad Request: " + e.getMessage()));
        }
    }

    /**
     * POST /navigator/routeadd/{fromId}/{toId}/{distance}
     * Добавить маршрут между двумя точками
     *
     * @param fromId - ID точки отправления
     * @param toId - ID точки назначения
     * @param distance - дистанция (>= 2)
     */
    @PostMapping("/routeadd/{fromId}/{toId}/{distance}")
    public ResponseEntity<?> addRoute(@PathVariable Long fromId,
                                      @PathVariable Long toId,
                                      @PathVariable Long distance,
                                      HttpServletRequest request) {
        try {
            if (distance < 2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponseDto(400, "Bad Request: Distance must be >= 2"));
            }

            RouteDto route = navigatorService.addRoute(fromId, toId, distance);
            String location = request.getRequestURL().toString() + "/" + route.getId();
            return ResponseEntity.created(URI.create(location)).body(route);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponseDto(409, "Conflict: " + e.getMessage()));
        }
    }
}
