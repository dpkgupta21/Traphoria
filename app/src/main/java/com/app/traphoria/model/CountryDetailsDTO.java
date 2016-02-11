package com.app.traphoria.model;

import java.io.Serializable;


public class CountryDetailsDTO implements Serializable {

    private String country_image;
    private String country_name;
    private String valid_visa;

    public String getCountry_image() {
        return country_image;
    }

    public void setCountry_image(String country_image) {
        this.country_image = country_image;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getValid_visa() {
        return valid_visa;
    }

    public void setValid_visa(String valid_visa) {
        this.valid_visa = valid_visa;
    }
}
