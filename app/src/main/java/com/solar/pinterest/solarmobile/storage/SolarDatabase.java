package com.solar.pinterest.solarmobile.storage;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

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

    public void getUser(int id, DBInterface.UserListener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            DBSchema.User user = mUserDao.getUser(id);
            listener.onReadUser(user);
        });
    }

    public void putUser(DBSchema.User user) throws SQLiteConstraintException {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try{
                mUserDao.insert(user);
            } catch (SQLiteConstraintException e) {
                mUserDao.delete(user);
                mUserDao.insert(user);
            }
        });
    }
}
