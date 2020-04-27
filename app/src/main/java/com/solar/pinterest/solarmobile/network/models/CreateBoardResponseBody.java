package com.solar.pinterest.solarmobile.network.models;

public class CreateBoardResponseBody {
    public String info;
    public Board board;

    public CreateBoardResponseBody(String info, Board board) {
        this.info = info;
        this.board = board;
    }

    public CreateBoardResponseBody() {
        this.info = "";
        this.board = new Board();
    }
}
