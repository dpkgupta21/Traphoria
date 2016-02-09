package com.app.traphoria.model;


import java.io.Serializable;

public class VisaDTO implements Serializable {
    public String visa_id;
    public String country;
    public String passport_type;
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

    public String getPassport_type() {
        return passport_type;
    }

    public void setPassport_type(String passport_type) {
        this.passport_type = passport_type;
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
}
