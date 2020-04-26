package com.solar.pinterest.solarmobile.storage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DBSchema.User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    private static final int THREAD_COUNT = 1;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(THREAD_COUNT);

    public abstract DBSchema.UserDao getUserDao();

    static AppDatabase get(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "solardb")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
