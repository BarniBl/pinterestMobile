package com.solar.pinterest.solarmobile.network.models;

public class CreateBoardData {
    public String title;
    public String description;
    public String category;

    public CreateBoardData(String title, String description) {
        this.title = title;
        this.description = description;
        this.category = "cars"; //Не изменяем традициям прошлого
    }
}
