package com.solar.pinterest.solarmobile.network.models.responses;

import com.solar.pinterest.solarmobile.network.models.responses.bodies.CreateBoardResponseBody;

public class CreateBoardResponse {
    public String csrf_token;
    public CreateBoardResponseBody body;

    public CreateBoardResponse(String csrf_token, CreateBoardResponseBody createBoardResponseBody) {
        this.csrf_token = csrf_token;
        this.body = createBoardResponseBody;
    }

    public CreateBoardResponse() {
        this.csrf_token = "";
        this.body = new CreateBoardResponseBody();
    }
}
