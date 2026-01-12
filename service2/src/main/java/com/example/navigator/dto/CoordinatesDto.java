package com.example.navigator.dto;

public class CoordinatesDto {
    private Long x;
    private Float y;

    public CoordinatesDto(Long x, Float y) {
        this.x = x;
        this.y = y;
    }

    public CoordinatesDto() {
    }

    public Long getX() {
        return this.x;
    }

    public Float getY() {
        return this.y;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CoordinatesDto)) return false;
        final CoordinatesDto other = (CoordinatesDto) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$x = this.getX();
        final Object other$x = other.getX();
        if (this$x == null ? other$x != null : !this$x.equals(other$x)) return false;
        final Object this$y = this.getY();
        final Object other$y = other.getY();
        if (this$y == null ? other$y != null : !this$y.equals(other$y)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CoordinatesDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $x = this.getX();
        result = result * PRIME + ($x == null ? 43 : $x.hashCode());
        final Object $y = this.getY();
        result = result * PRIME + ($y == null ? 43 : $y.hashCode());
        return result;
    }

    public String toString() {
        return "CoordinatesDto(x=" + this.getX() + ", y=" + this.getY() + ")";
    }
}
