package com.solar.pinterest.solarmobile.storage;

import android.app.Application;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solar.pinterest.solarmobile.R;
import com.solar.pinterest.solarmobile.network.Network;
import com.solar.pinterest.solarmobile.network.models.Board;
import com.solar.pinterest.solarmobile.network.models.EditProfile;
import com.solar.pinterest.solarmobile.network.models.responses.EditProfileResponse;
import com.solar.pinterest.solarmobile.network.models.responses.ProfileResponse;
import com.solar.pinterest.solarmobile.network.models.User;
import com.solar.pinterest.solarmobile.network.tools.TimestampConverter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpCookie;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserRepo extends SolarRepoAbstract{
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

    public LiveData<Pair<User, StatusEntity>> getMasterProfile(boolean forseUpdate) {
        HttpCookie cookie = AuthRepo.get(mContext).getSessionCookie();

        if (forseUpdate) {
            Log.e("Solar", "Forse update");
            getMasterUserFromNetwork();
        } else {
            mDatabase.getUser(AuthRepo.get(mContext).getUserId(), (user) -> {
                if (user == null) {
                    Log.e("Solar", "No masteruser in database");
                    getMasterUserFromNetwork();
                    return;
                }
                Log.e("Solar", "Got user from db");
                mUser.postValue(Pair.create(new UserConverter().BD2Net(user),
                        new StatusEntity(StatusEntity.Status.SUCCESS)));
            });
        }

        return mUser;
    }

    public LiveData<StatusEntity> updateMasterUser(EditProfile profile) {
        MutableLiveData<StatusEntity> result = new MutableLiveData<>();
        HttpCookie cookie = AuthRepo.get(mContext).getSessionCookie();
        String csrfToken = AuthRepo.get(mContext).getCsrfToken();
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("UserRepo", "Error got editing user");
                result.postValue(new StatusEntity(
                        StatusEntity.Status.FAILED,
                        mContext.getString(R.string.network_answer_500)
                ));
//                errorSettingsTextView.setText("Сервер временно недоступен");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.e("UserRepo", "Response got editing user");
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                EditProfileResponse editProfileResponse = gson.fromJson(response.body().string(), EditProfileResponse.class);
                if (!editProfileResponse.body.info.equals("data successfully saved")) {
                    result.postValue(new StatusEntity(
                            StatusEntity.Status.FAILED,
                            editProfileResponse.body.info
                    ));
                    Log.e("UserRepo", "bad saved got editing user");
                    return;
                }

                AuthRepo.get(mContext).setCsrfToken(editProfileResponse.csrf_token);
                Log.e("UserRepo", "OKgot editing user");
                getMasterUserFromNetwork();
//                getMasterProfile();


                result.postValue(new StatusEntity(
                        StatusEntity.Status.SUCCESS
                ));
            }
        };

        Network.getInstance().editProfile(cookie, profile, csrfToken, callback);
        return result;
    }

    public void putNetworkUser(User user) {
        mDatabase.putUser(
                new UserConverter().Net2DB(user));
//                new DBSchema.User(user.id, user.username, user.name, user.surname,
//                        user.email, user.age, user.status, user.avatarDir,
//                        user.isActive, TimestampConverter.toDate(user.createdTime), false));
    }

    private void getMasterUserFromNetwork() {

        Network.getInstance().profileData(AuthRepo.get(mContext).getSessionCookie(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                mUser.postValue(new Pair<>(null, new StatusEntity(
                        StatusEntity.Status.FAILED,
                        mContext.getString(R.string.network_answer_500)
                )));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.e("UserRepo", "Response got getting user");
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
                AuthRepo.get(mContext).setUserId(user.id);
                Log.e("UserRepo", "OK got getting user");
                putNetworkUser(user);

                mUser.postValue(new Pair<>(user, new StatusEntity(
                        StatusEntity.Status.SUCCESS
                )));
            }
        });
    }

    private class UserConverter {
        public DBSchema.User Net2DB(User net) {
            return new DBSchema.User(net.id, net.username, net.name, net.surname,
                    net.email, net.age, net.status, net.avatarDir,
                    net.isActive, TimestampConverter.toDate(net.createdTime), false);
        }

        public User BD2Net(DBSchema.User db) {
            return new User(db.getId(), db.getUsername(), db.getName(), db.getSurname(),
                    db.getEmail(), db.getAge(), db.getStatus(), db.getAvatar(), db.isActive(),
                    TimestampConverter.fromDate(db.getCreated()));
        }
    }
}
