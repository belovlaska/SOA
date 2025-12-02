package com.example.route.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Coordinates {
    @NotNull
    private Long x;

    @NotNull
    @Min(value = -985, message = "y must be > -985")
    private Float y;
}
