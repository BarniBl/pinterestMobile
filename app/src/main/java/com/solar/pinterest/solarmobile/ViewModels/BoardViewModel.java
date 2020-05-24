package com.solar.pinterest.solarmobile.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.solar.pinterest.solarmobile.network.models.Board;
import com.solar.pinterest.solarmobile.storage.DBSchema;
import com.solar.pinterest.solarmobile.storage.PinRepo;
import com.solar.pinterest.solarmobile.storage.SolarDatabase;
import com.solar.pinterest.solarmobile.storage.StatusEntity;

import java.util.List;

public class BoardViewModel extends AndroidViewModel {
    private static final String TAG = "SolarMobile.BoardViewModel";
    private PinRepo mPinRepo;

    public BoardViewModel(@NonNull Application application) {
        super(application);
        mPinRepo = PinRepo.get(application);
    }

    public LiveData<List<DBSchema.Pin>> getBoardPins(int id) {
        return mPinRepo.getBoardPins(id);
    }

    public LiveData<StatusEntity> addBoard(Board board) {
        return new MutableLiveData<>(new StatusEntity(StatusEntity.Status.EMPTY));
    }
}
