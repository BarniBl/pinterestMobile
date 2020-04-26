package com.solar.pinterest.solarmobile;

import android.text.Editable;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Expose;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

class Network {
    private static Network instance;
    private Retrofit retrofit;
    private static SolarSunriseApi solarSunriseApi;

    private Network() {
        String BASE_URL = "https://solarsunrise.ru";
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
        solarSunriseApi = this.retrofit.create(SolarSunriseApi.class);
    }

    static Network getInstance() {
        if (instance == null) {
            instance = new Network();
        }
        return instance;
    }

    SolarSunriseApi getSolarSunriseApi() {
        return solarSunriseApi;
    }
}


class ResponseLoginBody {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("info")
    @Expose
    private String info;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}

class User {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("age")
    @Expose
    private int age;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("avatar_dir")
    @Expose
    private String avatarDir;
    @SerializedName("is_active")
    @Expose
    private boolean isActive;
    @SerializedName("created_time")
    @Expose
    private String createdTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatarDir() {
        return avatarDir;
    }

    public void setAvatarDir(String avatarDir) {
        this.avatarDir = avatarDir;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

}

class LoginData {

    private String email;
    private String password;

    LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return email + " " + password;
    }

}

class Post {
    @SerializedName("userId")
    @Expose
    private int userId;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

interface SolarSunriseApi {

    @POST("/api/v1/login")
    Call<Response> login(@Body LoginData data);

    // Another endpoints...
}
