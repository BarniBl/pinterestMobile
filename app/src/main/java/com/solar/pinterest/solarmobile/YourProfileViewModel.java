package com.solar.pinterest.solarmobile;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.solar.pinterest.solarmobile.network.models.User;
import com.solar.pinterest.solarmobile.storage.SolarRepo;
import com.solar.pinterest.solarmobile.storage.StatusEntity;

public class YourProfileViewModel extends AndroidViewModel {
    private SolarRepo mRepo;

    public YourProfileViewModel(@NonNull Application application) {
        super(application);
        mRepo = SolarRepo.get(getApplication());
    }

    public LiveData<Pair<User, StatusEntity>> getMasterUser() {
        return mRepo.getMasterProfile();
    }
}
