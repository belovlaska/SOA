package com.example.navigator.dto;

import java.time.LocalDate;

public class RouteDto {
    private Long id;
    private String name;
    private CoordinatesDto coordinates;
    private LocalDate creationDate;
    private LocationDto from;
    private LocationDto to;
    private Long distance;

    public RouteDto(Long id, String name, CoordinatesDto coordinates, LocalDate creationDate, LocationDto from, LocationDto to, Long distance) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public RouteDto() {
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public CoordinatesDto getCoordinates() {
        return this.coordinates;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public LocationDto getFrom() {
        return this.from;
    }

    public LocationDto getTo() {
        return this.to;
    }

    public Long getDistance() {
        return this.distance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(CoordinatesDto coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public void setFrom(LocationDto from) {
        this.from = from;
    }

    public void setTo(LocationDto to) {
        this.to = to;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof RouteDto)) return false;
        final RouteDto other = (RouteDto) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$coordinates = this.getCoordinates();
        final Object other$coordinates = other.getCoordinates();
        if (this$coordinates == null ? other$coordinates != null : !this$coordinates.equals(other$coordinates))
            return false;
        final Object this$creationDate = this.getCreationDate();
        final Object other$creationDate = other.getCreationDate();
        if (this$creationDate == null ? other$creationDate != null : !this$creationDate.equals(other$creationDate))
            return false;
        final Object this$from = this.getFrom();
        final Object other$from = other.getFrom();
        if (this$from == null ? other$from != null : !this$from.equals(other$from)) return false;
        final Object this$to = this.getTo();
        final Object other$to = other.getTo();
        if (this$to == null ? other$to != null : !this$to.equals(other$to)) return false;
        final Object this$distance = this.getDistance();
        final Object other$distance = other.getDistance();
        if (this$distance == null ? other$distance != null : !this$distance.equals(other$distance)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof RouteDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $coordinates = this.getCoordinates();
        result = result * PRIME + ($coordinates == null ? 43 : $coordinates.hashCode());
        final Object $creationDate = this.getCreationDate();
        result = result * PRIME + ($creationDate == null ? 43 : $creationDate.hashCode());
        final Object $from = this.getFrom();
        result = result * PRIME + ($from == null ? 43 : $from.hashCode());
        final Object $to = this.getTo();
        result = result * PRIME + ($to == null ? 43 : $to.hashCode());
        final Object $distance = this.getDistance();
        result = result * PRIME + ($distance == null ? 43 : $distance.hashCode());
        return result;
    }

    public String toString() {
        return "RouteDto(id=" + this.getId() + ", name=" + this.getName() + ", coordinates=" + this.getCoordinates() + ", creationDate=" + this.getCreationDate() + ", from=" + this.getFrom() + ", to=" + this.getTo() + ", distance=" + this.getDistance() + ")";
    }
}
