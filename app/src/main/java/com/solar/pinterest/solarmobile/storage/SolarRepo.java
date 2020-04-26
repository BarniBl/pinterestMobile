package com.solar.pinterest.solarmobile.storage;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.solar.pinterest.solarmobile.R;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class SolarRepo implements DBInterface.Listener, RepositoryInterface {
    private static SolarRepo instance;
    private SolarDatabase mDatabase;
    private Application mContext;

    private static final String USER_ID_KEY = "KEY_USERID";

    private CookieStore mCookieStore;
    private SharedPreferences mSharedPreferences;

    private String mCsrfToken;

    public static SolarRepo get(Application app) {
        if (instance == null) {
            instance = new SolarRepo(app);
        }
        return instance;

    }

    private SolarRepo(Application app) {
        mDatabase = SolarDatabase.get(app);
        mContext = app;

        if (CookieHandler.getDefault() == null) {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        }
        mCookieStore = ((CookieManager) CookieHandler.getDefault()).getCookieStore();
        mSharedPreferences = app.getSharedPreferences(
                app.getString(R.string.preferences_file_key), Context.MODE_PRIVATE
        );
    }

    @Override
    public void getUser(int id, UserListener listener) {
        mDatabase.getUser(id, listener);
    }

    public void putUser(DBSchema.User user) {
        mDatabase.putUser(user);
    }

    @Override
    public void getPin(int id, OnReadListener<DBSchema.Pin> listener) {
        mDatabase.getPin(id, listener);
    }

    @Override
    public void putPin(DBSchema.Pin pin) {
        mDatabase.putPin(pin);
    }

    @Override
    public void getMasterUser(UserListener listener) throws NoSuchElementException {
        int id = mSharedPreferences.getInt(mContext.getString(R.string.userid_key), -1);
        if (id == -1) {
            throw new NoSuchElementException();
        }
        getUser(id, listener);
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void setMasterUser(DBSchema.User master) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(mContext.getString(R.string.userid_key), master.getId());
        editor.commit();
        putUser(master);
    }

    @Override
    @Nullable
    public HttpCookie getSessionCookie() {
        List<HttpCookie> cookieList = mCookieStore.get(
                URI.create(mContext.getString(R.string.backend_uri)));
        for(HttpCookie cookie : cookieList) {
            if (cookie.getName() == mContext.getString(R.string.session_cookie)) {
                return cookie;
            }
        }
        return null;
    }

    @Override
    public void setSessionCookie(HttpCookie cookie) {
        mCookieStore.add(URI.create(mContext.getString(R.string.backend_uri)), cookie);
    }

    @Override
    public void setCsrfToken(String token) {
        mCsrfToken = token;
    }

    @Override
    public String getCsrfToken() {
        return mCsrfToken;
    }

    @Override
    public void onReadUser(DBSchema.User user) {

    }

}
