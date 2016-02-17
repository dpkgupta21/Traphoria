package com.app.traphoria.model;


import java.io.Serializable;

public class PassportVisaDTO  implements Serializable{

    private String id;
    private String country;
    private String countryID;
    private String passport_visa_type;
    private String passport_visa_type_id;
    private String passportNumber;
    private String expiryDate;
    private String entry_type;
    private String country_image;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public String getPassport_visa_type() {
        return passport_visa_type;
    }

    public void setPassport_visa_type(String passport_visa_type) {
        this.passport_visa_type = passport_visa_type;
    }

    public String getPassport_visa_type_id() {
        return passport_visa_type_id;
    }

    public void setPassport_visa_type_id(String passport_visa_type_id) {
        this.passport_visa_type_id = passport_visa_type_id;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getEntry_type() {
        return entry_type;
    }

    public void setEntry_type(String entry_type) {
        this.entry_type = entry_type;
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
