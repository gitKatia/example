package com.kat.model;

import java.time.LocalTime;

public class StopBuilder {

    private long id;
    private LocalTime pickupTime;
    private Position position;

    public static StopBuilder stopBuilder(){
        return new StopBuilder();
    }

    public StopBuilder id(long id) {
        this.id = id;
        return this;
    }

    public StopBuilder pickupTime(LocalTime pickupTime) {
        this.pickupTime = pickupTime;
        return this;
    }

    public StopBuilder position(Position position) {
        this.position = position;
        return this;
    }

    public Stop build() {
        Stop stop = new Stop();
        stop.setId(id);
        stop.setPickupTime(pickupTime);
        stop.setPosition(position);
        return stop;
    }

}
