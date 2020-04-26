package com.solar.pinterest.solarmobile.storage;

public interface DBInterface {
    void getUser(int id,UserListener listener);
    void putUser(DBSchema.User user);

    interface Listener extends UserListener{
    }

    interface UserListener {
        void onReadUser(DBSchema.User user);
    }
}
