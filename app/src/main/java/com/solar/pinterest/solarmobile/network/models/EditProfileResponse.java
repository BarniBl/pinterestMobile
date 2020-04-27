package com.solar.pinterest.solarmobile.network.models;

public class EditProfileResponse {
    public String csrf_token;
    public EditProfileResponseBody body;

    public EditProfileResponse(String csrf_token, EditProfileResponseBody editProfileResponseBody) {
        this.csrf_token = csrf_token;
        this.body = editProfileResponseBody;
    }

    public EditProfileResponse() {
        this.csrf_token = "";
        this.body = new EditProfileResponseBody();
    }
}
