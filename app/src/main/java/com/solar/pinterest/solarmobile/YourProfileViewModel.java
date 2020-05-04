package com.solar.pinterest.solarmobile;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.solar.pinterest.solarmobile.network.models.User;
import com.solar.pinterest.solarmobile.storage.AuthRepo;
import com.solar.pinterest.solarmobile.storage.StatusEntity;
import com.solar.pinterest.solarmobile.storage.UserRepo;

import java.net.HttpCookie;

public class YourProfileViewModel extends AndroidViewModel {
    private UserRepo mUserRepo;

    public YourProfileViewModel(@NonNull Application application) {
        super(application);
        mUserRepo = new UserRepo(getApplication());
    }

    public LiveData<Pair<User, StatusEntity>> getMasterUser() {
        HttpCookie cookie = AuthRepo.get(getApplication()).getSessionCookie();
        if (cookie == null) {
            return new MutableLiveData<>(new Pair<>(null, new StatusEntity(StatusEntity.Status.EMPTY)));
        }
        return mUserRepo.getMasterProfile(cookie);
    }
}
