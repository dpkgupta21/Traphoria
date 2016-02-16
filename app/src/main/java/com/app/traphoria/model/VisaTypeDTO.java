package com.app.traphoria.model;

import com.j256.ormlite.field.DatabaseField;


public class VisaTypeDTO {

    @DatabaseField(generatedId = true)
    private int localID;
    @DatabaseField
    private String id;
    @DatabaseField
    private  String name;

    public int getLocalID() {
        return localID;
    }

    public void setLocalID(int localID) {
        this.localID = localID;
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
}
