package com.solar.pinterest.solarmobile.storage;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solar.pinterest.solarmobile.EventBus.Event;
import com.solar.pinterest.solarmobile.EventBus.EventBus;
import com.solar.pinterest.solarmobile.MainActivity;
import com.solar.pinterest.solarmobile.R;
import com.solar.pinterest.solarmobile.YourProfileActivity;
import com.solar.pinterest.solarmobile.network.Network;
import com.solar.pinterest.solarmobile.network.models.LoginData;
import com.solar.pinterest.solarmobile.network.models.ProfileResponse;
import com.solar.pinterest.solarmobile.network.models.RegistrationData;
import com.solar.pinterest.solarmobile.network.models.User;
import com.solar.pinterest.solarmobile.network.tools.TimestampConverter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AuthRepo {
    private static final String TAG = "Solar.AuthRepo";
    private static AuthRepo instance;
    private Application mContext;
    private SolarDatabase mDatabase;

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
        mContext = app;
        EventBus.get().subscribe(new Event(mContext.getString(R.string.event_logout)), event -> logout());

        mDatabase = SolarDatabase.get(app);

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
        if (storedCookie.isEmpty()) {
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

    @SuppressLint("ApplySharedPref")
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

    public boolean isAuthorized() {
        return getSessionCookie() != null;
    }

    public MutableLiveData<StatusEntity> login(@NonNull String login, @NonNull String password) {
        MutableLiveData<StatusEntity> mAuthProgress = new MutableLiveData<>();

        LoginData loginData = new LoginData(login, password);
        Network.getInstance().login(loginData, getAuthResponseCallback(mAuthProgress));

        return mAuthProgress;
    }

    public MutableLiveData<StatusEntity> register(@NonNull String login, @NonNull String nick, @NonNull String password) {
        MutableLiveData<StatusEntity> authProgress = new MutableLiveData<>();

        RegistrationData registrationData = new RegistrationData(login, password, nick);
        Network.getInstance().registration(registrationData, getAuthResponseCallback(authProgress));

        return authProgress;
    }

    private Callback getAuthResponseCallback(MutableLiveData<StatusEntity> progress) {
        return new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
               progress.postValue(new StatusEntity(
                        StatusEntity.Status.FAILED,
                        mContext.getString(R.string.network_answer_500)
                ));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                ProfileResponse profileResponse = gson.fromJson(response.body().string(), ProfileResponse.class);
                if (!profileResponse.body.info.equals(mContext.getString(R.string.network_answer_OK))) {
                    progress.postValue(new StatusEntity(
                            StatusEntity.Status.FAILED,
                            profileResponse.body.info
                    ));
                    return;
                }
                User user = profileResponse.body.user;

                List<HttpCookie> cookies = HttpCookie.parse(response.header("Set-Cookie"));
                for (HttpCookie cookie : cookies) {
                    String cookieName = cookie.getName();
                    if (cookieName.equals(mContext.getString(R.string.session_cookie))) {
                        setSessionCookie(cookie);
                        break;
                    }
                }
                UserRepo.get(mContext).putNetworkUser(user);
                progress.postValue(new StatusEntity(StatusEntity.Status.SUCCESS));
            }
        };
    }

    @SuppressLint("ApplySharedPref")
    private void logout() {
        Log.e(TAG, "Clear AuthRepo");
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(mContext.getString(R.string.cookies_key));
        editor.remove(mContext.getString(R.string.userid_key));
        editor.commit();

        mCookieStore.removeAll();

        mDatabase.clear();
    }
}
