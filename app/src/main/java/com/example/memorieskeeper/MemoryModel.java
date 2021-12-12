package com.example.memorieskeeper;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

/*
    This class is used for creating and working with memory object,
    storing and retrieving data from the database.
 */
public class MemoryModel implements Parcelable {
    private String userName;
    private String userId;
    private String name;
    private String description;
    private String location;
    private long createdAt;
    private String imageUrl;

    public MemoryModel() { }

    public MemoryModel(String userName, String userId, String name, String description, String location) {
        this.userName = userName;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.createdAt = new Date().getTime();
    }

    public MemoryModel(Parcel parcel) {
        this.userName = parcel.readString();
        this.userId = parcel.readString();
        this.name = parcel.readString();
        this.description = parcel.readString();
        this.location = parcel.readString();
        this.createdAt = parcel.readLong();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MemoryModel> CREATOR
            = new Parcelable.Creator<MemoryModel>() {
        public MemoryModel createFromParcel(Parcel in) {
            return new MemoryModel(in);
        }

        public MemoryModel[] newArray(int size) {
            return new MemoryModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.userName);
        parcel.writeString(this.userId);
        parcel.writeString(this.name);
        parcel.writeString(this.description);
        parcel.writeString(this.location);
        parcel.writeLong(this.createdAt);
    }
}
