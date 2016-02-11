package com.app.traphoria.model;


import java.io.Serializable;

public class SerachDTO implements Serializable {

    private String country_id;
    private  String name;
    private String country_image;
    private String passportvisatext;
    private String expire_on;

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry_image() {
        return country_image;
    }

    public void setCountry_image(String country_image) {
        this.country_image = country_image;
    }

    public String getPassportvisatext() {
        return passportvisatext;
    }

    public void setPassportvisatext(String passportvisatext) {
        this.passportvisatext = passportvisatext;
    }

    public String getExpire_on() {
        return expire_on;
    }

    public void setExpire_on(String expire_on) {
        this.expire_on = expire_on;
    }
}

