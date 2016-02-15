package com.app.traphoria.model;


public class DestinationDTO {
    private String image;
    private String title;
    private String top_destination_id;
    private String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public String getTop_destination_id() {
        return top_destination_id;
    }

    public void setTop_destination_id(String top_destination_id) {
        this.top_destination_id = top_destination_id;
    }
}
