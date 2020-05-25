package com.solar.pinterest.solarmobile.storage;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Pair;

import com.solar.pinterest.solarmobile.EventBus.Event;
import com.solar.pinterest.solarmobile.EventBus.EventBus;
import com.solar.pinterest.solarmobile.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
        EventBus.get().subscribe(new Event(context.getString(R.string.event_logout)), event -> {
            clear();
        });
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
//            DBSchema.Pin pin = mPinDao.getPin(id);
            DBSchema.Pin pin = new DBSchema.Pin(613, "Lynxxxx", "Lynxxxx", 116, new Date(), "/", "Title", "Desc", false);
            listener.call(pin);
        });
    }

    @Override
    public void getPins(int[] ids, OnReadListener<List<Pair<DBSchema.Pin, Boolean>>> listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Pair<DBSchema.Pin, Boolean>> res = new ArrayList<>();
            for(int id : ids) {
                DBSchema.Pin pin;
                Boolean fail = false;
                try {
                    pin = mPinDao.getPin(id);
                } catch (SQLException e) {
                    pin = new DBSchema.Pin(id);
                    fail = true;
                }
                res.add(Pair.create(pin, fail));
            }
            listener.call(res);
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
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.clear();
            mPinDao.clear();
            mBoardDao.clear();
        });
    }

}
