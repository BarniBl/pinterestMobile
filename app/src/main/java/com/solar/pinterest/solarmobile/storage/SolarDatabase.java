package com.solar.pinterest.solarmobile.storage;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Lambda;

public class SolarDatabase implements DBInterface {
    private static SolarDatabase INSTANCE;

    private DBSchema.UserDao mUserDao;
    private DBSchema.PinDao mPinDao;
    private DBSchema.BoardDao mBoardDao;

    public static SolarDatabase get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SolarDatabase(context);
        }
        return INSTANCE;
    }

    private SolarDatabase(Context context) {
        mUserDao = AppDatabase.get(context).getUserDao();
        mPinDao = AppDatabase.get(context).getPinDao();
        mBoardDao = AppDatabase.get(context).getBoardDao();
    }

    public void getUser(int id, DBInterface.UserListener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            DBSchema.User user = mUserDao.getUser(id);
            listener.onReadUser(user);
        });
    }

    public void getUser(String nick, DBInterface.OnReadListener<DBSchema.User> listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            DBSchema.User user = mUserDao.getUser(nick);
            listener.call(user);
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
    public void getPins(int[] ids, OnReadListener<Pair<List<DBSchema.Pin>, List<Integer>>> listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<DBSchema.Pin> res = new ArrayList<>();
            List<Integer> fails = new ArrayList<>();
            for(int id : ids) {
                try {
                    res.add(mPinDao.getPin(id));
                } catch (SQLException e) {
                    fails.add(id);
                }
            }
            listener.call(Pair.create(res, fails));
        });
    }

    @Override
    public void getBoardPins(int id, OnReadListener<List<DBSchema.Pin>> listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            listener.call(mPinDao.getBoardPins(id));
        });
    }

    @Override
    public void putPin(DBSchema.Pin pin) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mPinDao.insert(pin);
        });
    }

    @Override
    public void putPins(List<DBSchema.Pin> pins) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            for(DBSchema.Pin pin: pins) {
                putPin(pin);
            }
        });
    }

    @Override
    public void getBoard(int id, OnReadListener<DBSchema.Board> listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            DBSchema.Board board = mBoardDao.getPin(id);
            listener.call(board);
        });
    }

    @Override
    public void putBoard(DBSchema.Board board) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mBoardDao.insert(board);
        });
    }

    @Override
    public void getUserBoards(int userid, OnReadListener<List<DBSchema.Board>> listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            listener.call(mBoardDao.getUserBoards(userid));
        });
    }

    @Override
    public void clear() {
        mUserDao.clear();
        mPinDao.clear();
        mBoardDao.clear();
    }

}
