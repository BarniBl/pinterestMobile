package com.solar.pinterest.solarmobile.storage;

public interface StorageInterface {
    void getUser(int id,Listener listener);
    void putUser(DBSchema.User user);

    interface Listener {
        void onReadUser(DBSchema.User user);
    }
}
