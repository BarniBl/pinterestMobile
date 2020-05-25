package com.solar.pinterest.solarmobile.network.models.responses;

import com.solar.pinterest.solarmobile.network.models.responses.bodies.CreateBoardResponseBody;
import com.solar.pinterest.solarmobile.network.models.responses.bodies.CreatePinResponseBody;

public class CreatePinResponse {
    public String csrf_token;
    public CreatePinResponseBody body;

    public CreatePinResponse(String csrf_token, CreatePinResponseBody createPinResponseBody) {
        this.csrf_token = csrf_token;
        this.body = createPinResponseBody;
    }

    public CreatePinResponse() {
        this.csrf_token = "";
        this.body = new CreatePinResponseBody();
    }
}
