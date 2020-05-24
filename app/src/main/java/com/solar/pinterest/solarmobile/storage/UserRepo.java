package com.solar.pinterest.solarmobile.storage;

import android.app.Application;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solar.pinterest.solarmobile.R;
import com.solar.pinterest.solarmobile.network.Network;
import com.solar.pinterest.solarmobile.network.models.responses.ProfileResponse;
import com.solar.pinterest.solarmobile.network.models.User;
import com.solar.pinterest.solarmobile.network.tools.TimestampConverter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpCookie;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserRepo {
    private Application mContext;
    private SolarDatabase mDatabase;
    private static UserRepo instance;

    private MutableLiveData<Pair<User, StatusEntity>> mUser;

    public static UserRepo get(Application context) {
        if (instance == null) {
            instance = new UserRepo(context);
        }
        return instance;
    }

    private UserRepo(Application context) {
        mContext = context;
        mDatabase = SolarDatabase.get(context);
        mUser = new MutableLiveData<>();
    }

    public LiveData<Pair<User, StatusEntity>> getMasterProfile(HttpCookie cookie) {
        Network.getInstance().profileData(cookie, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                mUser.postValue(new Pair<>(null, new StatusEntity(
                        StatusEntity.Status.FAILED,
                        mContext.getString(R.string.network_answer_500)
                )));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                ProfileResponse profileResponse = gson.fromJson(response.body().string(), ProfileResponse.class);
                if (!profileResponse.body.info.equals(mContext.getString(R.string.network_answer_OK))) {
                    mUser.postValue(new Pair<>(null, new StatusEntity(
                            StatusEntity.Status.FAILED,
                            profileResponse.body.info
                    )));
                    return;
                }
                AuthRepo.get(mContext).setCsrfToken(profileResponse.csrf_token);
                User user = profileResponse.body.user;

                putNetworkUser(user);

                mUser.postValue(new Pair<>(user, new StatusEntity(
                        StatusEntity.Status.SUCCESS
                )));
            }
        });

        return mUser;
    }

    public void putNetworkUser(User user) {
        mDatabase.putUser(
                new DBSchema.User(user.id, user.username, user.name, user.surname,
                        user.email, user.age, user.status, user.avatarDir,
                        user.isActive, TimestampConverter.toDate(user.createdTime), false));
    }
}
