package com.solar.pinterest.solarmobile.network.models;

import com.google.gson.annotations.SerializedName;

public class Board {
    public int id;
    @SerializedName("owner_id")
    public int ownerId;
    public String title;
    public String description;
    public String category;
    @SerializedName("created_time")
    public String createdTime;
    @SerializedName("is_deleted")
    public boolean isDeleted;
    @SerializedName("view_pin")
    public String viewPin;



    public Board(int id, int ownerId, String title, String description, String category, String createdTime, boolean isDeleted, String viewPin) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.createdTime = createdTime;
        this.isDeleted = isDeleted;
        this.viewPin = viewPin;
    }

    public Board() {
        this.id = 0;
        this.ownerId = 0;
        this.title = "";
        this.description = "";
        this.category = "";
        this.createdTime = "";
        this.isDeleted = false;
        this.viewPin = "";
    }

}
