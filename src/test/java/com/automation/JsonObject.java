package com.automation;

import com.google.gson.Gson;

public class JsonObject {
    private String title;
    private String description;
    private boolean published;

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

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String json() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
