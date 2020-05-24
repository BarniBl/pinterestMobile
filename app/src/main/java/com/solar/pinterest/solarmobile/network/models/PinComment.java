package com.solar.pinterest.solarmobile.network.models;

import com.google.gson.annotations.SerializedName;

public class PinComment {
    public String text;

    public PinComment(String text) {
        this.text = text;
    }

    public PinComment() {
        this.text = "";
    }

}
