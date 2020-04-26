package com.solar.pinterest.solarmobile.network;

import com.solar.pinterest.solarmobile.network.models.LoginData;

import okhttp3.Callback;

public interface NetworkInterface {
    void login(LoginData loginData, Callback callbackFunc);

}
