package com.solar.pinterest.solarmobile.storage;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import com.solar.pinterest.solarmobile.R;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

public class AuthRepo {
    private static final String TAG = "Solar.AuthRepo";
    private static AuthRepo instance;
    private Application mContext;
    private SolarDatabase mDatabase;

    private static final String USER_ID_KEY = "KEY_USERID";

    private CookieStore mCookieStore;
    private SharedPreferences mSharedPreferences;

    private String mCsrfToken;

    public static AuthRepo get(Application app) {
        if (instance == null) {
            instance = new AuthRepo(app);
        }
        return instance;

    }

    private AuthRepo(Application app) {
        mDatabase = SolarDatabase.get(app);
        mContext = app;

        if (CookieHandler.getDefault() == null) {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        }
        mSharedPreferences = app.getSharedPreferences(
                app.getString(R.string.preferences_file_key), Context.MODE_PRIVATE
        );
        mCookieStore = ((CookieManager) CookieHandler.getDefault()).getCookieStore();
        loadCookies();
    }

    private void loadCookies() {
        String storedCookie = mSharedPreferences.getString(mContext.getString(R.string.cookies_key), "");
        URI uri = URI.create(mContext.getString(R.string.cookies_key));
        if (storedCookie == "") {
            Log.e(TAG, "No cookie stored");
            return;
        }
        setSessionCookie(new HttpCookie(mContext.getString(R.string.session_cookie), storedCookie));
        Log.i(TAG, "Loaded " + 1 + " cookies to CookieStore");
    }

    @Nullable
    public HttpCookie getSessionCookie() {
        List<HttpCookie> cookieList = mCookieStore.get(
                URI.create(mContext.getString(R.string.cookie_uri)));
        for(HttpCookie cookie : cookieList) {
            if (cookie.getName().equals(mContext.getString(R.string.session_cookie))) {
                return cookie;
            }
        }
        return null;
    }

    public void setSessionCookie(HttpCookie cookie) {
        mCookieStore.add(URI.create(mContext.getString(R.string.cookie_uri)), cookie);

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(mContext.getString(R.string.cookies_key), cookie.getValue());
        editor.commit();
    }

    public void setCsrfToken(String token) {
        mCsrfToken = token;
    }

    public String getCsrfToken() {
        return mCsrfToken;
    }

    public int getUserId() {
        return mSharedPreferences.getInt(mContext.getString(R.string.userid_key), -1);
    }
}
