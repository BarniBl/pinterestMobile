package com.solar.pinterest.solarmobile.storage;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.solar.pinterest.solarmobile.R;
import com.solar.pinterest.solarmobile.network.models.User;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class SolarRepo implements DBInterface.Listener, RepositoryInterface {
    private static final String TAG = "Solar.SolarRepo";
    private static SolarRepo instance;
    private UserRepo mUserRepo;
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
        mUserRepo = new UserRepo(mContext);

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

    @Override
    public void getUser(int id, UserListener listener) {
        mDatabase.getUser(id, listener);
    }

    @Override
    public void getUser(String nick, OnReadListener<DBSchema.User> listener) {
        mDatabase.getUser(nick, listener);
    }

    public void putUser(DBSchema.User user) {
        mDatabase.putUser(user);
    }

    @Override
    public void getPin(int id, OnReadListener<DBSchema.Pin> listener) {
        mDatabase.getPin(id, listener);
    }

    @Override
    public void getPins(int[] ids, OnReadListener<Pair<List<DBSchema.Pin>, List<Integer>>> listener) {
        mDatabase.getPins(ids, listener);
    }

    @Override
    public void getBoardPins(int id, OnReadListener<List<DBSchema.Pin>> listener) {
        mDatabase.getBoardPins(id, listener);
    }

    @Override
    public void putPin(DBSchema.Pin pin) {
        mDatabase.putPin(pin);
    }

    @Override
    public void putPins(List<DBSchema.Pin> pins) {
        mDatabase.putPins(pins);
    }

    @Override
    public void getBoard(int id, OnReadListener<DBSchema.Board> listener) {
        mDatabase.getBoard(id, listener);
    }

    @Override
    public void putBoard(DBSchema.Board board) {
        mDatabase.putBoard(board);
    }

    @Override
    public void getUserBoards(int userid, OnReadListener<List<DBSchema.Board>> listener) {
        mDatabase.getUserBoards(userid, listener);
    }

    @Override
    public void clear() {
        mDatabase.clear();
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
    public void onLogout() {
        clear();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(mContext.getString(R.string.cookies_key));
        editor.remove(mContext.getString(R.string.userid_key));
        editor.commit();mCookieStore.removeAll();
    }

    @Override
    public LiveData<Pair<User, StatusEntity>> getMasterProfile() {
        return mUserRepo.getProfile(getSessionCookie());
    }

    @Override
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

    @Override
    public void setSessionCookie(HttpCookie cookie) {
        mCookieStore.add(URI.create(mContext.getString(R.string.cookie_uri)), cookie);

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(mContext.getString(R.string.cookies_key), cookie.getValue());
        editor.commit();
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
