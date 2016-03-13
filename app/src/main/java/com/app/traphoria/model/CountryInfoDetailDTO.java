package com.app.traphoria.model;


import java.util.List;

public class CountryInfoDetailDTO {

    private List<String> country_image;
    private String country_name;
    private int valid_visa;
    private String expire_date;
    private String description;

    public List<String> getCountry_image() {
        return country_image;
    }

    public void setCountry_image(List<String> country_image) {
        this.country_image = country_image;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
