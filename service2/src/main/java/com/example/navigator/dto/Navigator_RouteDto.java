package com.example.navigator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto {
    private Long id;
    private String name;
    private CoordinatesDto coordinates;
    private LocalDate creationDate;
    private LocationDto from;
    private LocationDto to;
    private Long distance;
}
