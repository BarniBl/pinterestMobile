package com.solar.pinterest.solarmobile.storage;

import android.util.Pair;

import androidx.lifecycle.LiveData;

import com.solar.pinterest.solarmobile.network.models.User;

import java.net.HttpCookie;

public interface RepositoryInterface extends DBInterface {
//    void getMasterUser(UserListener listener);
    void setMasterUser(DBSchema.User master);

//    void onLogout();

//    LiveData<Pair<User, StatusEntity>> getMasterProfile();

//    HttpCookie getSessionCookie();
//    void setSessionCookie(HttpCookie cookie);
//
//    void setCsrfToken(String token);
//    String getCsrfToken();
}
