package com.app.traphoria.model;


import java.io.Serializable;

public class GeometryDTO implements Serializable {
    private LocationDTO location;

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }
}
