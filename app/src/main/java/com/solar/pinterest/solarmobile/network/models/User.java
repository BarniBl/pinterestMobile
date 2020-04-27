package com.solar.pinterest.solarmobile.network.models;

import com.google.gson.annotations.SerializedName;

public class User {
    public int id;
    public String username;
    public String name;
    public String surname;
    public String email;
    public int age;
    public String status;
    @SerializedName("avatar_dir")
    public String avatarDir;
    @SerializedName("is_active")
    public boolean isActive;
    @SerializedName("created_time")
    public String createdTime;

    public User(int id, String username, String name, String surname, String email, int age, String status, String avatarDir, Boolean isActive, String createdTime) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.age = age;
        this.status = status;
        this.avatarDir = avatarDir;
        this.isActive = isActive;
        this.createdTime = createdTime;
    }

    public User() {
        this.id = 0;
        this.username = "";
        this.name = "";
        this.surname = "";
        this.email = "";
        this.age = 0;
        this.status = "";
        this.avatarDir = "";
        this.isActive = false;
        this.createdTime = "";
    }

}


