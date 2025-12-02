package com.example.route.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 434)
    private String name;

    @Embedded
    private Coordinates coordinates;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "from_x")),
            @AttributeOverride(name = "y", column = @Column(name = "from_y")),
            @AttributeOverride(name = "z", column = @Column(name = "from_z")),
            @AttributeOverride(name = "name", column = @Column(name = "from_name"))
    })
    private Location from;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "to_x")),
            @AttributeOverride(name = "y", column = @Column(name = "to_y")),
            @AttributeOverride(name = "z", column = @Column(name = "to_z")),
            @AttributeOverride(name = "name", column = @Column(name = "to_name"))
    })
    private Location to;

    @Column
    private Long distance;
}
