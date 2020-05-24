package com.solar.pinterest.solarmobile.network.models.responses;

import com.solar.pinterest.solarmobile.network.models.responses.bodies.ProfileResponseBody;

public class ProfileResponse {

    public String csrf_token;
    public ProfileResponseBody body;

    public ProfileResponse(String csrf_token, ProfileResponseBody profileResponseBody) {
        this.csrf_token = csrf_token;
        this.body = profileResponseBody;
    }

    public ProfileResponse() {
        this.csrf_token = "";
        this.body = new ProfileResponseBody();
    }
}
