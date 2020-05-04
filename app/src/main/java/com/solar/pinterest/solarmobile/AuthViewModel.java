package com.solar.pinterest.solarmobile;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.solar.pinterest.solarmobile.EventBus.Event;
import com.solar.pinterest.solarmobile.EventBus.EventBus;
import com.solar.pinterest.solarmobile.EventBus.EventListener;
import com.solar.pinterest.solarmobile.storage.AuthRepo;
import com.solar.pinterest.solarmobile.storage.StatusEntity;

import java.util.Objects;

public class AuthViewModel extends AndroidViewModel {
    private static final String TAG = "Solar.AuthViewModel";

    private String mCurrentLogin;
    private String mCurrentPassword;
    private String mCurrentNickname;
    private AuthRepo mAuthRepo;

    private MutableLiveData<StatusEntity> mAuthStatus;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        mAuthRepo = AuthRepo.get(application);

        // is not used for a now
        EventBus.get().subscribe(new Event(application.getString(R.string.event_logout)), event -> {
            if (mAuthStatus != null) {
                mAuthStatus.postValue(new StatusEntity(StatusEntity.Status.NO_PERMISSION));
            }
        });
    }

    public LiveData<StatusEntity> login(@NonNull String login, @NonNull String password) {
        // awaiting previous result
        if (login.equals(mCurrentLogin) && mAuthStatus.getValue().getStatus() == StatusEntity.Status.IN_PROGRESS) {
            return mAuthStatus;
        }
        mCurrentLogin = login;
        mAuthStatus = mAuthRepo.login(login, password);
        return mAuthStatus;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.e(TAG, "onCleared()");
    }

    public LiveData<StatusEntity> register(@NonNull String login, @NonNull String nick, @NonNull String password) {
        // awaiting previous result
        if (login.equals(mCurrentLogin) && nick.equals(mCurrentNickname)
                && mAuthStatus.getValue().getStatus() == StatusEntity.Status.IN_PROGRESS) {
            return mAuthStatus;
        }
        mCurrentLogin = login;
        mCurrentNickname = nick;
        mAuthStatus = mAuthRepo.register(login, nick, password);
        return mAuthStatus;
    }

    public boolean isAuthorized() {
        return mAuthRepo.isAuthorized();
    }

}
