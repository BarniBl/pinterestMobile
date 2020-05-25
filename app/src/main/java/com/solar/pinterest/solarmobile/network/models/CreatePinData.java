package com.solar.pinterest.solarmobile.network.models;

import com.google.gson.annotations.SerializedName;

public class CreatePinData {
    public String title;
    public String description;
    @SerializedName("board_id")
    public int boardID;

    public CreatePinData(String title, String description, int boardID) {
        this.title = title;
        this.description = description;
        this.boardID = boardID;
    }

    public CreatePinData() {
        this.title = "";
        this.description = "";
        this.boardID = 0;
    }

}
