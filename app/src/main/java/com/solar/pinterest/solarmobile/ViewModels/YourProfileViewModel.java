package com.solar.pinterest.solarmobile.ViewModels;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.solar.pinterest.solarmobile.network.models.Board;
import com.solar.pinterest.solarmobile.network.models.EditProfile;
import com.solar.pinterest.solarmobile.network.models.Pin;
import com.solar.pinterest.solarmobile.network.models.User;
import com.solar.pinterest.solarmobile.storage.AuthRepo;
import com.solar.pinterest.solarmobile.storage.BoardRepo;
import com.solar.pinterest.solarmobile.storage.DBSchema;
import com.solar.pinterest.solarmobile.storage.PinRepo;
import com.solar.pinterest.solarmobile.storage.StatusEntity;
import com.solar.pinterest.solarmobile.storage.UserRepo;

import java.net.HttpCookie;
import java.util.List;

public class YourProfileViewModel extends AndroidViewModel {
    private UserRepo mUserRepo;
    private BoardRepo mBoardRepo;
    private PinRepo mPinRepo;

    public YourProfileViewModel(@NonNull Application application) {
        super(application);
        mUserRepo = UserRepo.get(getApplication());
        mBoardRepo = BoardRepo.get(getApplication());
        mPinRepo = PinRepo.get(getApplication());
    }

    public LiveData<Pair<User, StatusEntity>> getMasterUser() {
        return getMasterUser(false);
    }

    public LiveData<Pair<User, StatusEntity>> getMasterUser(boolean forseUpdate) {
        if (AuthRepo.get(getApplication()).getSessionCookie() == null) {
            return new MutableLiveData<>(new Pair<>(null, new StatusEntity(StatusEntity.Status.EMPTY)));
        }
        return mUserRepo.getMasterProfile(forseUpdate);
    }

    public LiveData<StatusEntity> editMasterUser(EditProfile user) {
        return mUserRepo.updateMasterUser(user);
    }

    public LiveData<Pair<List<DBSchema.Board>, StatusEntity>> getMyBoards() {
        return mBoardRepo.getMyBoards();
    }

    public LiveData<StatusEntity> createPin(Pin pin) {
        return mPinRepo.addPin(pin);

    public LiveData<StatusEntity> addBoard(Board board) {
        return mBoardRepo.createBoard(board.title, board.description);
//        return new MutableLiveData<>(new StatusEntity(StatusEntity.Status.EMPTY));

    }
}
