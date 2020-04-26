package com.solar.pinterest.solarmobile.storage;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

public class SolarDatabase implements DBInterface {
    private static SolarDatabase INSTANCE;

    private DBSchema.UserDao mUserDao;
    private DBSchema.PinDao mPinDao;

    public static SolarDatabase get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SolarDatabase(context);
        }
        return INSTANCE;
    }

    private SolarDatabase(Context context) {
        mUserDao = AppDatabase.get(context).getUserDao();
        mPinDao = AppDatabase.get(context).getPinDao();
    }

    public void getUser(int id, DBInterface.UserListener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            DBSchema.User user = mUserDao.getUser(id);
            listener.onReadUser(user);
        });
    }

    public void putUser(DBSchema.User user) throws SQLiteConstraintException {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.insert(user);
        });
    }

    @Override
    public void getPin(int id, OnReadListener<DBSchema.Pin> listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            DBSchema.Pin pin = mPinDao.getPin(id);
            listener.call(pin);
        });
    }

    @Override
    public void putPin(DBSchema.Pin pin) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mPinDao.insert(pin);
        });
    }
}
