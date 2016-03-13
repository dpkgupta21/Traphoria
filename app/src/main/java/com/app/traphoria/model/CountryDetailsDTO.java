package com.app.traphoria.model;

import java.io.Serializable;


public class CountryDetailsDTO implements Serializable {

    private String country_image;
    private String country_name;
    private int valid_visa;
    private String expire_date;

    public int getValid_visa() {
        return valid_visa;
    }

    public void setValid_visa(int valid_visa) {
        this.valid_visa = valid_visa;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

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

}
