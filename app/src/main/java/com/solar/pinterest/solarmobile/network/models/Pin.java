package com.solar.pinterest.solarmobile.network.models;

import com.google.gson.annotations.SerializedName;

public class Pin {
    public int id;
    @SerializedName("author_id")
    public int authorID;
    @SerializedName("owner_id")
    public int ownerID;
    @SerializedName("board_id")
    public int boardId;
    @SerializedName("pin_dir")
    public String pinDir;
    public String title;
    public String description;
    @SerializedName("is_deleted")
    public boolean isDeleted;

    public Pin(int id, int authorID, int ownerID, int boardId, String path, String title, String description, boolean deleted) {
        this.id = id;
        this.authorID = authorID;
        this.ownerID = ownerID;
        this.boardId = boardId;
        this.pinDir = path;
        this.title = title;
        this.description = description;
        this.isDeleted = deleted;
    }

    public Pin() {

    }
}