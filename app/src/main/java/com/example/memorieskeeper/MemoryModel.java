package com.example.memorieskeeper;

import androidx.annotation.NonNull;

import com.google.firebase.database.ServerValue;

import java.util.Date;

public class MemoryModel {
    private String userId;
    private String name;
    private String description;
    private String location;
    private long createdAt;

    public MemoryModel() { }

    public MemoryModel(String userId, String name, String description, String location) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.createdAt = new Date().getTime();
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

    public long getCreatedAt() { return this.createdAt; }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
