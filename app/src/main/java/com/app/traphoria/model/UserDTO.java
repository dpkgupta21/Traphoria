package com.app.traphoria.model;


import java.io.Serializable;

public class UserDTO  implements Serializable{

    public String id;
    public String name;
    public String email;
    public String mobile;
    public String gender;
    public String dob;
    public boolean is_location_service;
    public boolean is_trip_tracker;
    public boolean is_push_alert;
    public boolean is_deal_expiry_alert;
    public String image;
    public String userType;
    public CountryDTO Country;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public boolean is_location_service() {
        return is_location_service;
    }

    public void setIs_location_service(boolean is_location_service) {
        this.is_location_service = is_location_service;
    }

    public boolean is_trip_tracker() {
        return is_trip_tracker;
    }

    public void setIs_trip_tracker(boolean is_trip_tracker) {
        this.is_trip_tracker = is_trip_tracker;
    }

    public boolean is_push_alert() {
        return is_push_alert;
    }

    public void setIs_push_alert(boolean is_push_alert) {
        this.is_push_alert = is_push_alert;
    }

    public boolean is_deal_expiry_alert() {
        return is_deal_expiry_alert;
    }

    public void setIs_deal_expiry_alert(boolean is_deal_expiry_alert) {
        this.is_deal_expiry_alert = is_deal_expiry_alert;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CountryDTO getCountry() {
        return Country;
    }

    public void setCountry(CountryDTO country) {
        Country = country;
    }
}
