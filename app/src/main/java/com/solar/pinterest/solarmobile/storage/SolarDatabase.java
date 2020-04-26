package com.solar.pinterest.solarmobile.storage;

import android.content.Context;

public class SolarDatabase implements DBInterface {
    private static SolarDatabase INSTANCE;

    private DBSchema.UserDao mUserDao;

    public static SolarDatabase get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SolarDatabase(context);
        }
        return INSTANCE;
    }

    private SolarDatabase(Context context) {
        mUserDao = AppDatabase.get(context).getUserDao();
    }

    public void getUser(int id, DBInterface.Listener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            DBSchema.User user = mUserDao.getUser(id);
            listener.onReadUser(user);
        });
    }

    public void putUser(DBSchema.User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.insert(user);
        });
    }
}
