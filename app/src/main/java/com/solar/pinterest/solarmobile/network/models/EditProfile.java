package com.solar.pinterest.solarmobile.network.models;

public class EditProfile {
    public String name;
    public String surname;
    public String nickname;
    public String status;

    public EditProfile(String name, String surname, String nickname, String status) {
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.status = status;
    }
}
