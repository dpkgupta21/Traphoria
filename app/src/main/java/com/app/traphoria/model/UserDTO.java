package com.app.traphoria.model;


import java.io.Serializable;

public class UserDTO  implements Serializable{

    public String id;
    public String name;
    public String email ="";
    public String mobile ="";
    public String gender ="";
    public String dob ="";
    public String age ="";
    public boolean is_location_service =false;
    public boolean is_trip_tracker = false;
    public String image ="";
    public String userType="";
    public String sociallogin="";

    public CountryDTO Country;

    public String family_contact="";
    public String countrycode="";
    public String notification_duration="";
    public String emergency_number="";

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSociallogin() {
        return sociallogin;
    }

    public void setSociallogin(String sociallogin) {
        this.sociallogin = sociallogin;
    }

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

    public String getFamily_contact() {
        return family_contact;
    }

    public void setFamily_contact(String family_contact) {
        this.family_contact = family_contact;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getNotification_duration() {
        return notification_duration;
    }

    public void setNotification_duration(String notification_duration) {
        this.notification_duration = notification_duration;
    }

    public String getEmergency_number() {
        return emergency_number;
    }

    public void setEmergency_number(String emergency_number) {
        this.emergency_number = emergency_number;
    }
}
