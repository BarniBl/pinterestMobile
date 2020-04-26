package com.solar.pinterest.solarmobile.network.models;

public class LoginResponseBody {

    public String info;
    public User user;

    public LoginResponseBody(String info, User user) {
        this.info = info;
        this.user = user;
    }

    public LoginResponseBody() {
        this.info = "";
        this.user = new User();
    }
}
