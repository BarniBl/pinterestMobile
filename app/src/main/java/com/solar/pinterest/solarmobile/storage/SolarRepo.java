package com.solar.pinterest.solarmobile.storage;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;
import com.solar.pinterest.solarmobile.R;
import java.util.List;

public class SolarRepo implements RepositoryInterface {
    private static final String TAG = "Solar.SolarRepo";
    private static SolarRepo instance;
    private SolarDatabase mDatabase;

    public static SolarRepo get(Application app) {
        if (instance == null) {
            instance = new SolarRepo(app);
        }
        return instance;

    }

    private SolarRepo(Application app) {
        mDatabase = SolarDatabase.get(app);
    }

    @Override
    public void getUser(int id, UserListener listener) {
        mDatabase.getUser(id, listener);
    }

    @Override
    public void getUser(String nick, OnReadListener<DBSchema.User> listener) {
        mDatabase.getUser(nick, listener);
    }

    public void putUser(DBSchema.User user) {
        mDatabase.putUser(user);
    }

    @Override
    public void getPin(int id, OnReadListener<DBSchema.Pin> listener) {
        mDatabase.getPin(id, listener);
    }

    @Override
    public void getPins(int[] ids, OnReadListener<Pair<List<DBSchema.Pin>, List<Integer>>> listener) {
        mDatabase.getPins(ids, listener);
    }

    @Override
    public void getBoardPins(int id, OnReadListener<List<DBSchema.Pin>> listener) {
        mDatabase.getBoardPins(id, listener);
    }

    @Override
    public void putPin(DBSchema.Pin pin) {
        mDatabase.putPin(pin);
    }

    @Override
    public void putPins(List<DBSchema.Pin> pins) {
        mDatabase.putPins(pins);
    }

    @Override
    public void getBoard(int id, OnReadListener<DBSchema.Board> listener) {
        mDatabase.getBoard(id, listener);
    }

    @Override
    public void putBoard(DBSchema.Board board) {
        mDatabase.putBoard(board);
    }

    @Override
    public void getUserBoards(int userid, OnReadListener<List<DBSchema.Board>> listener) {
        mDatabase.getUserBoards(userid, listener);
    }

    @Override
    public void clear() {
        mDatabase.clear();
    }

}
