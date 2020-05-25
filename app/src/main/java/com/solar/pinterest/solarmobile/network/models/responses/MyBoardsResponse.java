package com.solar.pinterest.solarmobile.network.models.responses;

import com.solar.pinterest.solarmobile.network.models.responses.bodies.MyBoardResponseBody;
import com.solar.pinterest.solarmobile.network.models.responses.bodies.ProfileResponseBody;

public class MyBoardsResponse {

    public String csrf_token;
    public MyBoardResponseBody body;

    public MyBoardsResponse(String csrf_token, MyBoardResponseBody myBoardResponseBody) {
        this.csrf_token = csrf_token;
        this.body = myBoardResponseBody;
    }

    public MyBoardsResponse() {
        this.csrf_token = "";
        this.body = new MyBoardResponseBody();
    }
}
