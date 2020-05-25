package com.solar.pinterest.solarmobile.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.solar.pinterest.solarmobile.storage.DBSchema;
import com.solar.pinterest.solarmobile.storage.PinRepo;
import com.solar.pinterest.solarmobile.storage.StatusEntity;

public class PinViewModel extends AndroidViewModel {
    private static final String TAG = "SolarMobile.PinViewModel";
    private PinRepo mPinRepo;
    private int mCurrentId = 0;

//    private MutableLiveData<DBSchema.Pin> mPin = new MutableLiveData<>();

    public PinViewModel(@NonNull Application application) {
        super(application);
        mPinRepo = PinRepo.get(application);
    }

    public LiveData<DBSchema.Pin> getPin(int id) {
        mCurrentId = id;
        return mPinRepo.getPin(id);
    }

    public LiveData<DBSchema.Pin> getPin() {
        return getPin(mCurrentId);
    }

    public LiveData<StatusEntity> addPin(DBSchema.Pin pin) {
        return mPinRepo.addPin(pin);
    }
}
