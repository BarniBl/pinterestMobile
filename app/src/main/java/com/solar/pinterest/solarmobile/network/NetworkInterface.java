package com.solar.pinterest.solarmobile.network;

import com.solar.pinterest.solarmobile.network.models.LoginData;
import com.solar.pinterest.solarmobile.network.models.RegistrationData;

import okhttp3.Callback;

public interface NetworkInterface {
    void login(LoginData loginData, Callback callbackFunc);
    void registration(RegistrationData registrationData, Callback callbackFunc);
}
