package com.solar.pinterest.solarmobile.storage;

public interface DBInterface {
    void getUser(int id,UserListener listener);
    void putUser(DBSchema.User user);

    void getPin(int id, OnReadListener<DBSchema.Pin> listener);
    void putPin(DBSchema.Pin pin);

    interface Listener extends UserListener{
    }

    interface UserListener {
        void onReadUser(DBSchema.User user);
    }

    interface OnReadListener<T> {
        void call(T t);
    }
}
