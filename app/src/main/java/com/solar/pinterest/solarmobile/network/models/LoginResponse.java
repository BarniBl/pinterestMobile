package com.solar.pinterest.solarmobile.network.models;

public class LoginResponse {

    public String csrf_token;
    public LoginResponseBody body;

    public LoginResponse(String csrf_token, LoginResponseBody loginResponseBody) {
        this.csrf_token = csrf_token;
        this.body = loginResponseBody;
    }

    public LoginResponse() {
        this.csrf_token = "";
        this.body = new LoginResponseBody();
    }
}
