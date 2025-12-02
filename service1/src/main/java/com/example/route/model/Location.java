package com.example.route.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Location {
    @NotNull
    private Integer x;

    @NotNull
    private Long y;

    @NotNull
    private Long z;

    @NotEmpty(message = "name cannot be empty")
    private String name;
}
