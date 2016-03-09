package com.app.traphoria.model;

import java.io.Serializable;
import java.util.List;

public class TripUserDTO implements Serializable {
    private String name;
    private String image;
    private String gender;
    private String age;
    private List<PassportDTO> Passport;
    private List<VisaDTO> Visa;

    public List<VisaDTO> getVisa() {
        return Visa;
    }

    public void setVisa(List<VisaDTO> visa) {
        Visa = visa;
    }

    public List<PassportDTO> getPassport() {
        return Passport;
    }

    public void setPassport(List<PassportDTO> passport) {
        Passport = passport;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
