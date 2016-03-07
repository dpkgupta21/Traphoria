package com.app.traphoria.model;

import java.io.Serializable;

public class TaskDTO implements Serializable {
    private String id;
    private String user_id;
    private String title;
    private String description;
    private String created;
    private String modified;
    private String shared_with;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getShared_with() {
        return shared_with;
    }

    public void setShared_with(String shared_with) {
        this.shared_with = shared_with;
    }
}
