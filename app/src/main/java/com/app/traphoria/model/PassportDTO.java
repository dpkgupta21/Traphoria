package com.app.traphoria.model;


import java.io.Serializable;

public class PassportDTO implements Serializable {

    public String passport_id;
    public String country;
    public String country_id;
    public String passport_type;
    public String  passport_type_id;
    public String passport_no;
    public String expire_date;
    public String country_image;
    public String type;






    public String getPassport_type_id() {
        return passport_type_id;
    }

    public void setPassport_type_id(String passport_type_id) {
        this.passport_type_id = passport_type_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }



    public String getPassport_id() {
        return passport_id;
    }

    public void setPassport_id(String passport_id) {
        this.passport_id = passport_id;
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

    public String getPassport_no() {
        return passport_no;
    }

    public void setPassport_no(String passport_no) {
        this.passport_no = passport_no;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
