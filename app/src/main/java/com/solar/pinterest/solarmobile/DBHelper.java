package com.solar.pinterest.solarmobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "solarpinterest.db";

    private static DBHelper instance;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DBHelper get(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;

    }

    public boolean setUser(DBSchema.User user) {
        ContentValues values = DBSchema.UserTable.getContentValues(user);
        long res = mDatabase.insert(DBSchema.UserTable.NAME, null, values);
        return res != -1;
    }

    public int updateUser(DBSchema.User user) {
        String key = Integer.toString(user.getId());
        ContentValues values = DBSchema.UserTable.getContentValues(user);

        int res = mDatabase.update(DBSchema.UserTable.NAME, values,
                String.format("%s = ?", DBSchema.UserTable.Cols.ID),
                new String[] { key });
        return res;
    }

    public DBSchema.User getUser(int id) {
        String key = Integer.toString(id);

        Cursor cursor = mDatabase.query(DBSchema.UserTable.NAME, null,
                String.format("%s = ?", DBSchema.UserTable.Cols.ID), new String[] { key },
                null, null, null);
        return DBSchema.UserTable.inflateFromCursor(cursor);
    }


    private DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        mContext = context.getApplicationContext();
        mDatabase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBSchema.UserTable.getCreateQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    public class User extends DBSchema.UserTable.User {};
}
