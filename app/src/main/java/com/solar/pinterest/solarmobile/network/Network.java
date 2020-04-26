package com.solar.pinterest.solarmobile.network;

import com.google.gson.Gson;
import com.solar.pinterest.solarmobile.network.models.LoginData;
import com.solar.pinterest.solarmobile.network.models.RegistrationData;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class Network implements NetworkInterface {
    private static final MediaType JSON_TYPE = MediaType.parse("application/json");
    private static final String BASE_URL = "https://solarsunrise.ru";
    private static Network instance;
    private static Gson gson;
    private OkHttpClient client;


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
}

