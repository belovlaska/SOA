package com.example.navigator.controller;

import com.example.navigator.dto.ErrorResponseDto;
import com.example.navigator.dto.RouteDto;
import com.example.navigator.service.NavigatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/navigator")
public class NavigatorController {

    @Autowired
    private NavigatorService navigatorService;

    @GetMapping("/routes/{id-from}/{id-to}/{order-by}")
    public ResponseEntity<?> getRoutes(@PathVariable("id-from") Long idFrom,
                                       @PathVariable("id-to") Long idTo,
                                       @PathVariable("order-by") String orderBy) {
        try {
            List<RouteDto> routes = navigatorService.getRoutes(idFrom, idTo, orderBy);
            return ResponseEntity.ok(routes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDto(400, "Bad Request: " + e.getMessage()));
        }
    }

    @PostMapping("/routeadd/{id-from}/{id-to}/{distance}")
    public ResponseEntity<?> addRoute(@PathVariable("id-from") Long idFrom,
                                      @PathVariable("id-to") Long idTo,
                                      @PathVariable("distance") Long distance) {
        try {
            RouteDto route = navigatorService.addRoute(idFrom, idTo, distance);
            return ResponseEntity.status(HttpStatus.CREATED).body(route);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponseDto(409, "Conflict: " + e.getMessage()));
        }
    }
}
