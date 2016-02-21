package com.app.traphoria.model;


import java.io.Serializable;

public class NearBYLocationDTO implements Serializable {

    private String vicinity;
    private String name;
    private GeometryDTO geometry;

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeometryDTO getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryDTO geometry) {
        this.geometry = geometry;
    }
}
