package com.solar.pinterest.solarmobile.network;

import com.solar.pinterest.solarmobile.network.models.CreateBoardData;
import com.solar.pinterest.solarmobile.network.models.EditProfile;
import com.solar.pinterest.solarmobile.network.models.LoginData;
import com.solar.pinterest.solarmobile.network.models.RegistrationData;
import com.solar.pinterest.solarmobile.network.models.User;

import java.net.HttpCookie;

import okhttp3.Callback;

public interface NetworkInterface {
    void login(LoginData loginData, Callback callbackFunc);

    void registration(RegistrationData registrationData, Callback callbackFunc);

    void profileData(HttpCookie cookie, Callback callbackFunc);

    void addBoard(HttpCookie cookie, CreateBoardData createBoardData, String csrf, Callback callbackFunc);

    void editProfile(HttpCookie cookie, EditProfile profile, String csrf, Callback callbackFunc);

    void editProfilePicture(HttpCookie cookie, String fileName, String csrf, Callback callbackFunc);
}
