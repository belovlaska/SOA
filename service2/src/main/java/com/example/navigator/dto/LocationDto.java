package com.example.navigator.dto;

public class LocationDto {
    private Integer x;
    private Long y;
    private Long z;
    private String name;

    public LocationDto(Integer x, Long y, Long z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public LocationDto() {
    }

    public Integer getX() {
        return this.x;
    }

    public Long getY() {
        return this.y;
    }

    public Long getZ() {
        return this.z;
    }

    public String getName() {
        return this.name;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public void setZ(Long z) {
        this.z = z;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LocationDto)) return false;
        final LocationDto other = (LocationDto) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$x = this.getX();
        final Object other$x = other.getX();
        if (this$x == null ? other$x != null : !this$x.equals(other$x)) return false;
        final Object this$y = this.getY();
        final Object other$y = other.getY();
        if (this$y == null ? other$y != null : !this$y.equals(other$y)) return false;
        final Object this$z = this.getZ();
        final Object other$z = other.getZ();
        if (this$z == null ? other$z != null : !this$z.equals(other$z)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LocationDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $x = this.getX();
        result = result * PRIME + ($x == null ? 43 : $x.hashCode());
        final Object $y = this.getY();
        result = result * PRIME + ($y == null ? 43 : $y.hashCode());
        final Object $z = this.getZ();
        result = result * PRIME + ($z == null ? 43 : $z.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "LocationDto(x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ", name=" + this.getName() + ")";
    }
}
