package com.example.memorieskeeper;

public class MemoryModel {
    private String userId;
    private String name;
    private String description;
    private String location;

    public MemoryModel(String userId, String name, String description, String location) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.location = location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
