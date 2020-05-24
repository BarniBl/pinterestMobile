package com.solar.pinterest.solarmobile.network.models.responses.bodies;

import com.solar.pinterest.solarmobile.network.models.Board;
import com.solar.pinterest.solarmobile.network.models.User;

public class MyBoardResponseBody {
    public String info;
    public Board[] boards;

    public MyBoardResponseBody(String info, Board[] boards) {
        this.info = info;
        this.boards = boards;
    }

    public MyBoardResponseBody() {
        this.info = "";
    }
}
