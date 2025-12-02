package com.example.route.dto;

import com.example.route.model.Coordinates;
import com.example.route.model.Location;
import lombok.Data;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
public class RouteUpdateDto {
    @NotEmpty(message = "name cannot be empty")
    @Size(min = 1, max = 434, message = "name must be between 1 and 434 characters")
    private String name;

    @Valid
    @NotNull(message = "coordinates cannot be null")
    private Coordinates coordinates;

    @Valid
    @NotNull(message = "from cannot be null")
    private Location from;

    @Valid
    @NotNull(message = "to cannot be null")
    private Location to;

    @Min(value = 2, message = "distance must be >= 2")
    private Long distance;
}
