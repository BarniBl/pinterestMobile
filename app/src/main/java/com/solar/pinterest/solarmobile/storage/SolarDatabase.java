package com.solar.pinterest.solarmobile.storage;

import android.content.Context;

public class SolarDatabase implements StorageInterface {
    private static SolarDatabase INSTANCE;
    private Context mContext;

    private DBSchema.UserDao mUserDao;

    public static SolarDatabase get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SolarDatabase(context);
        }
        return INSTANCE;
    }

    private SolarDatabase(Context context) {
        mContext = context;
        mUserDao = AppDatabase.get(mContext).getUserDao();
    }

    public void getUser(int id, StorageInterface.Listener listener) {
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
