package com.solar.pinterest.solarmobile.network.models;

public class RegistrationResponse {
    public String csrf_token;
    public LoginResponseBody body;

    public RegistrationResponse(String csrf_token, LoginResponseBody loginResponseBody) {
        this.csrf_token = csrf_token;
        this.body = loginResponseBody;
    }

    public RegistrationResponse() {
        this.csrf_token = "";
        this.body = new LoginResponseBody();
    }
}
