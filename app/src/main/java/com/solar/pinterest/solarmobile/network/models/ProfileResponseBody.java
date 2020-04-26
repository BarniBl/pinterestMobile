package com.solar.pinterest.solarmobile.network.models;

public class ProfileResponseBody {

    public String info;
    public User user;

    public ProfileResponseBody(String info, User user) {
        this.info = info;
        this.user = user;
    }

    public ProfileResponseBody() {
        this.info = "";
        this.user = new User();
    }
}
