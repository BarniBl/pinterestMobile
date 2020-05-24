package com.solar.pinterest.solarmobile.ViewModels;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.solar.pinterest.solarmobile.network.models.EditProfile;
import com.solar.pinterest.solarmobile.network.models.User;
import com.solar.pinterest.solarmobile.storage.AuthRepo;
import com.solar.pinterest.solarmobile.storage.StatusEntity;
import com.solar.pinterest.solarmobile.storage.UserRepo;

import java.net.HttpCookie;

public class YourProfileViewModel extends AndroidViewModel {
    private UserRepo mUserRepo;

    public YourProfileViewModel(@NonNull Application application) {
        super(application);
        mUserRepo = UserRepo.get(getApplication());
    }

    public LiveData<Pair<User, StatusEntity>> getMasterUser() {
        if (AuthRepo.get(getApplication()).getSessionCookie() == null) {
            return new MutableLiveData<>(new Pair<>(null, new StatusEntity(StatusEntity.Status.EMPTY)));
        }
        return mUserRepo.getMasterProfile();
    }

    // TODO: редактирование юзера
    public LiveData<StatusEntity> editMasterUser(EditProfile user) {
        return mUserRepo.updateMasterUser(user);
    }
}
