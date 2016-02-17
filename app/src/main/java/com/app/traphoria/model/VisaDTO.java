package com.app.traphoria.model;


import java.io.Serializable;

public class VisaDTO implements Serializable {
    public String visa_id;
    public String country;
    public String country_id;
    public String visa_type;
    public String visa_type_id;
    public String entry_type;
    public String expire_date;
    public String country_image;

    public String getVisa_id() {
        return visa_id;
    }

    public void setVisa_id(String visa_id) {
        this.visa_id = visa_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String getEntry_type() {
        return entry_type;
    }

    public void setEntry_type(String entry_type) {
        this.entry_type = entry_type;
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

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getVisa_type() {
        return visa_type;
    }

    public void setVisa_type(String visa_type) {
        this.visa_type = visa_type;
    }

    public String getVisa_type_id() {
        return visa_type_id;
    }

    public void setVisa_type_id(String visa_type_id) {
        this.visa_type_id = visa_type_id;
    }
}
