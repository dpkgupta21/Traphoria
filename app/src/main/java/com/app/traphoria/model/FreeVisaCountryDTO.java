package com.app.traphoria.model;


import java.io.Serializable;

public class FreeVisaCountryDTO implements Serializable {

    private String id;
    private String name;
    private  String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
