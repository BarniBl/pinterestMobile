package com.solar.pinterest.solarmobile.network;

import com.google.gson.Gson;
import com.solar.pinterest.solarmobile.network.models.CreateBoardData;
import com.solar.pinterest.solarmobile.network.models.EditProfile;
import com.solar.pinterest.solarmobile.network.models.LoginData;
import com.solar.pinterest.solarmobile.network.models.CreatePinData;
import com.solar.pinterest.solarmobile.network.models.PinComment;
import com.solar.pinterest.solarmobile.network.models.RegistrationData;

import java.io.File;
import java.net.HttpCookie;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class Network implements NetworkInterface {
    private static final MediaType JSON_TYPE = MediaType.parse("application/json");
    private static final String BASE_URL = "https://solarsunrise.ru";
    private static Network instance;
    private static Gson gson;
    private OkHttpClient client;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");


    private Network() {
        this.client = new OkHttpClient();
        gson = new Gson();
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public static Network getInstance() {
        if (instance == null) {
            instance = new Network();
        }
        return instance;
    }

    @Override
    public void login(LoginData loginData, Callback callbackFunc) {
        String path = "/api/v1/login";

        String json = this.gson.toJson(loginData);
        RequestBody body = RequestBody.create(JSON_TYPE, json);

        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .method("POST", body)
                .build();

        client.newCall(request).enqueue(callbackFunc);
    }

    @Override
    public void registration(RegistrationData registrationData, Callback callbackFunc) {
        String path = "/api/v1/registration";

        String json = this.gson.toJson(registrationData);
        RequestBody body = RequestBody.create(JSON_TYPE, json);

        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .method("POST", body)
                .build();

        client.newCall(request).enqueue(callbackFunc);
    }

    @Override
    public void profileData(HttpCookie cookie, Callback callbackFunc) {
        String path = "/api/v1/profile/data";

        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .method("GET", null)
                .addHeader("Cookie", cookie.getName() + "=" + cookie.getValue())
                .build();

        client.newCall(request).enqueue(callbackFunc);
    }

    @Override
    public void addBoard(HttpCookie cookie, CreateBoardData createBoardData, String csrf, Callback callbackFunc) {
        String path = "/api/v1/board";

        String json = this.gson.toJson(createBoardData);
        RequestBody body = RequestBody.create(JSON_TYPE, json);

        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .method("POST", body)
                .addHeader("Cookie", cookie.getName() + "=" + cookie.getValue())
                .addHeader("csrf-token", csrf)
                .build();

        client.newCall(request).enqueue(callbackFunc);
    }

    @Override
    public void editProfile(HttpCookie cookie, EditProfile profile, String csrf, Callback callbackFunc) {
        String path = "/api/v1/profile/data";

        String json = this.gson.toJson(profile);
        RequestBody body = RequestBody.create(JSON_TYPE, json);

        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .method("POST", body)
                .addHeader("Cookie", cookie.getName() + "=" + cookie.getValue())
                .addHeader("csrf-token", csrf)
                .build();

        client.newCall(request).enqueue(callbackFunc);
    }

    @Override
    public void editProfilePicture(HttpCookie cookie, String fileName, String csrf, Callback callbackFunc) {
        String path = "/api/v1/profile/data";

        RequestBody requestBody = new okhttp3.MultipartBody.Builder().
                setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"profilePicture\""),
                        RequestBody.create(MEDIA_TYPE_PNG, new File(fileName)))
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .method("POST", requestBody)
                .addHeader("Cookie", cookie.getName() + "=" + cookie.getValue())
                .addHeader("csrf-token", csrf)
                .build();

        client.newCall(request).enqueue(callbackFunc);
    }

    @Override
    public void createPin(HttpCookie cookie, String fileName, CreatePinData createPinData, String csrf, Callback callbackFunc) {
        String path = "/api/v1/pin";

        String json = this.gson.toJson(createPinData);
        RequestBody body = RequestBody.create(JSON_TYPE, json);

        RequestBody requestBody = new okhttp3.MultipartBody.Builder().
                setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"pinPicture\""),
                        RequestBody.create(MEDIA_TYPE_PNG, new File(fileName)))
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"pin\""),
                        body)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .method("POST", requestBody)
                .addHeader("Cookie", cookie.getName() + "=" + cookie.getValue())
                .addHeader("csrf-token", csrf)
                .build();

        client.newCall(request).enqueue(callbackFunc);
    }

    @Override
    public void getPinsList(HttpCookie cookie, int limit, int sinceID, Callback callbackFunc) {
        String path = "/api/v1/pin/list/new?";
        path += "limit=" + limit + "&id=" + sinceID;

        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .method("GET", null)
                .addHeader("Cookie", cookie.getName() + "=" + cookie.getValue())
                .build();

        client.newCall(request).enqueue(callbackFunc);
    }

    @Override
    public void getPin(HttpCookie cookie, int pinID, Callback callbackFunc) {
        String path = "/api/v1/pin/";
        path += pinID;

        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .method("GET", null)
                .addHeader("Cookie", cookie.getName() + "=" + cookie.getValue())
                .build();

        client.newCall(request).enqueue(callbackFunc);
    }

    @Override
    public void sendComment(HttpCookie cookie, int pinID, PinComment comment, String csrf, Callback callbackFunc) {
        String path = "/api/v1/pin/";
        path += pinID + "/comment";

        String json = this.gson.toJson(comment);
        RequestBody body = RequestBody.create(JSON_TYPE, json);

        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .method("POST", body)
                .addHeader("Cookie", cookie.getName() + "=" + cookie.getValue())
                .addHeader("csrf-token", csrf)
                .build();
        client.newCall(request).enqueue(callbackFunc);
    }

    @Override
    public void getMyBoards(HttpCookie cookie, Callback callbackFunc) {
        String path = "/api/v1/board/list/my";

        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .method("GET", null)
                .addHeader("Cookie", cookie.getName() + "=" + cookie.getValue())
                .build();

        client.newCall(request).enqueue(callbackFunc);
    }

}

