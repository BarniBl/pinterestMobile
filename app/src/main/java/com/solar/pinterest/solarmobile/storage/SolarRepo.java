package com.solar.pinterest.solarmobile.storage;

import android.app.Application;
import android.content.Context;

public class SolarRepo implements StorageInterface, StorageInterface.Listener {
    private static SolarRepo instance;
    private SolarDatabase mDatabase;
    private Context mContext;

    public static SolarRepo get(Application app) {
        if (instance == null) {
            instance = new SolarRepo(app);
        }
        return instance;

    }

    private SolarRepo(Application app) {
        mDatabase = SolarDatabase.get(app);
        mContext = app;
    }

    @Override
    public void getUser(int id, Listener listener) {
        mDatabase.getUser(id, listener);
    }

    public void putUser(DBSchema.User user) {
        mDatabase.putUser(user);
    }

    @Override
    public void onReadUser(DBSchema.User user) {

    }
}
