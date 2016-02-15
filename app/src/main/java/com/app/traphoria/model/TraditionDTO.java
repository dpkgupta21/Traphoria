package com.app.traphoria.model;


import java.io.Serializable;
import java.util.List;

public class TraditionDTO implements Serializable {

    private String image;
    private String title;
    private String tradition_id;
    private String description;
    private List<AccordianDTO> accordian;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTradition_id() {
        return tradition_id;
    }

    public void setTradition_id(String tradition_id) {
        this.tradition_id = tradition_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AccordianDTO> getAccordian() {
        return accordian;
    }

    public void setAccordian(List<AccordianDTO> accordian) {
        this.accordian = accordian;
    }
}
