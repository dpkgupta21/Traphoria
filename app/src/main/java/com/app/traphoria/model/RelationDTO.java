package com.app.traphoria.model;


import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class RelationDTO implements Serializable {

    @DatabaseField(generatedId = true)
    private int localID;
    @DatabaseField
    private String id;
    @DatabaseField
    private String name;

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
