package com.solar.pinterest.solarmobile.storage;

import android.util.Pair;

import java.util.List;

public interface DBInterface {
    void getUser(int id,UserListener listener);
    void getUser(String username, OnReadListener<DBSchema.User> listener);
    void putUser(DBSchema.User user);

    void getPin(int id, OnReadListener<DBSchema.Pin> listener);
    void getPins(int[] ids, OnReadListener<Pair<List<DBSchema.Pin>, List<Integer>>> listener);
    void getBoardPins(int id, OnReadListener<List<DBSchema.Pin>> listener);
    void putPin(DBSchema.Pin pin);
    void putPins(List<DBSchema.Pin> pins);

    void getBoard(int id, OnReadListener<DBSchema.Board> listener);
    void putBoard(DBSchema.Board board);
    void getUserBoards(int userid, OnReadListener<List<DBSchema.Board>> listener);

    interface Listener extends UserListener{
    }

    interface UserListener {
        void onReadUser(DBSchema.User user);
    }

    interface OnReadListener<T> {
        void call(T t);
    }
}
