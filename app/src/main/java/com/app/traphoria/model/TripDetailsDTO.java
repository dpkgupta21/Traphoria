package com.app.traphoria.model;


import java.io.Serializable;
import java.util.List;

public class TripDetailsDTO  implements Serializable{
    private String country_image;
    private String country_name;
    private String trip_type;
    private String start_date;
    private String end_date;
    private String expire_date;
    private List<TripUserDTO> trip_users;

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

    public String getTrip_type() {
        return trip_type;
    }

    public void setTrip_type(String trip_type) {
        this.trip_type = trip_type;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public List<TripUserDTO> getTrip_users() {
        return trip_users;
    }

    public void setTrip_users(List<TripUserDTO> trip_users) {
        this.trip_users = trip_users;
    }
}
