package com.kat.model;

public class PositionBuilder {

    private double longitude;
    private double latitude;

    public static PositionBuilder positionBuilder() {
        return new PositionBuilder();
    }

    public PositionBuilder longitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public PositionBuilder latitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

}
