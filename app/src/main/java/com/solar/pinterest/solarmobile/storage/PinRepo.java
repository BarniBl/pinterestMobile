package com.solar.pinterest.solarmobile.storage;

import android.app.Application;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solar.pinterest.solarmobile.R;
import com.solar.pinterest.solarmobile.network.Network;
import com.solar.pinterest.solarmobile.network.models.CreateBoardData;
import com.solar.pinterest.solarmobile.network.models.CreatePinData;
import com.solar.pinterest.solarmobile.network.models.Pin;
import com.solar.pinterest.solarmobile.network.models.responses.CreateBoardResponse;
import com.solar.pinterest.solarmobile.network.models.responses.CreatePinResponse;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PinRepo extends SolarRepoAbstract {
    private SolarDatabase mDatabase;
    private static PinRepo instance;
    private Application mContext;

    public static PinRepo get(Application context) {
        if (instance == null) {
            instance = new PinRepo(context);
        }
        return instance;
    }

    private PinRepo(Application context) {
        mContext = context;
        mDatabase = SolarDatabase.get(context);
    }

    public LiveData<DBSchema.Pin> getPin(int id) {
        MutableLiveData<DBSchema.Pin> pinLD = new MutableLiveData<>();
        mDatabase.getPin(id, (pin) -> {
            pinLD.postValue(pin);
        });
        return pinLD;
    }

    // Get pins from DB by ID and from network inexistant ones
    public LiveData<List<DBSchema.Pin>> getPins(int[] ids) {
        MutableLiveData<List<DBSchema.Pin>> pinLD = new MutableLiveData<>();
        mDatabase.getPins(ids, (res) -> {
            AtomicInteger waiting = new AtomicInteger(0);
            List<DBSchema.Pin> pins = new LinkedList<>();
            for (Pair<DBSchema.Pin, Boolean> r : res) {
                if (r.second) {
                    pins.add(r.first);
                } else {
                    waiting.incrementAndGet();
                    // TODO: get from network
                    if (waiting.decrementAndGet() == 0) {
                        waiting.notifyAll();
                    }
                }
            }
            try {
                waiting.wait();
            } catch (InterruptedException e) {
            } finally {
                pinLD.postValue(pins);
            }

        });
        return pinLD;
    }

    public LiveData<List<DBSchema.Pin>> getBoardPins(int id) {
        MutableLiveData<List<DBSchema.Pin>> pinLD = new MutableLiveData<>();
        mDatabase.getBoardPins(id, (pin) -> {
            pinLD.postValue(pin);
        });
        return pinLD;
    }

    public LiveData<StatusEntity> addPin(Pin pin) {
        return put((MutableLiveData<StatusEntity> status) -> {
            CreatePinData createPinData = new CreatePinData(pin.title, pin.description, pin.boardId);
            Network.getInstance().createPin(AuthRepo.get(this.mContext).getSessionCookie(), pin.file, createPinData, AuthRepo.get(this.mContext).getCsrfToken(), createPinResponseCallback(status));

            status.postValue(new StatusEntity(StatusEntity.Status.SUCCESS));
        });
    }

    public LiveData<StatusEntity> putPins(List<DBSchema.Pin> pins) {
        return put(status -> {
            //TODO: goto network
            mDatabase.putPins(pins);
            status.postValue(new StatusEntity(StatusEntity.Status.SUCCESS));
        });
    }

    private Callback createPinResponseCallback(MutableLiveData<StatusEntity> progress) {
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
                CreatePinResponse createPinResponse = gson.fromJson(response.body().string(), CreatePinResponse.class);
                if (!createPinResponse.body.info.equals("data successfully saved")) {
                    progress.postValue(new StatusEntity(
                            StatusEntity.Status.FAILED,
                            createPinResponse.body.info
                    ));
                    return;
                }

                Date date = new Date();
                DBSchema.Pin pin = new DBSchema.Pin(createPinResponse.body.pin.id, Integer.toString(createPinResponse.body.pin.authorID), Integer.toString(createPinResponse.body.pin.ownerID), createPinResponse.body.pin.boardId, date, createPinResponse.body.pin.pinDir, createPinResponse.body.pin.title, createPinResponse.body.pin.description, createPinResponse.body.pin.isDeleted);
                mDatabase.putPin(pin);

                progress.postValue(new StatusEntity(StatusEntity.Status.SUCCESS));
            }
        };
    }
}
