package com.solar.pinterest.solarmobile.network;

import android.graphics.Bitmap;

import com.solar.pinterest.solarmobile.network.models.CreateBoardData;
import com.solar.pinterest.solarmobile.network.models.EditProfile;
import com.solar.pinterest.solarmobile.network.models.LoginData;
import com.solar.pinterest.solarmobile.network.models.CreatePinData;
import com.solar.pinterest.solarmobile.network.models.PinComment;
import com.solar.pinterest.solarmobile.network.models.RegistrationData;

import java.net.HttpCookie;

import okhttp3.Callback;

public interface NetworkInterface {
    void login(LoginData loginData, Callback callbackFunc);

    void registration(RegistrationData registrationData, Callback callbackFunc);

    void profileData(HttpCookie cookie, Callback callbackFunc);

    void addBoard(HttpCookie cookie, CreateBoardData createBoardData, String csrf, Callback callbackFunc);

    void editProfile(HttpCookie cookie, EditProfile profile, String csrf, Callback callbackFunc);

    void editProfilePicture(HttpCookie cookie, Bitmap file, String csrf, Callback callbackFunc);

    void createPin(HttpCookie cookie, Bitmap file, CreatePinData createPinData, String csrf, Callback callbackFunc);

    void getPinsList(HttpCookie cookie, int limit, int sinceID, Callback callbackFunc);

    void getPin(HttpCookie cookie, int pinID, Callback callbackFunc);

    void sendComment(HttpCookie cookie, int pinID, PinComment comment, String csrf, Callback callbackFunc);

    void getMyBoards(HttpCookie cookie, Callback callbackFunc);
}
