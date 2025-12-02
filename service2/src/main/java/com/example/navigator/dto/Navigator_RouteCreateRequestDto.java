package com.example.navigator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteCreateRequestDto {
    private String name;
    private CoordinatesDto coordinates;
    private LocationDto from;
    private LocationDto to;
    private Long distance;
}
