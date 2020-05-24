package com.solar.pinterest.solarmobile.storage;

import android.app.Application;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PinRepo extends SolarRepoAbstract {
    private SolarDatabase mDatabase;
    private static PinRepo instance;

    public static PinRepo get(Application context) {
        if (instance == null) {
            instance = new PinRepo(context);
        }
        return instance;
    }

    private PinRepo(Application context) {
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
            List pins = new LinkedList();
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

    public LiveData<StatusEntity> putPin(DBSchema.Pin pin) {
        return put((MutableLiveData<StatusEntity> status) -> {
            //TODO: goto network
            mDatabase.putPin(pin);
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
}
