package com.app.traphoria.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class MemberDTO implements Serializable {

    @DatabaseField
    private String name;
    @DatabaseField
    private String id;
    @DatabaseField
    private String selected ="N";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
