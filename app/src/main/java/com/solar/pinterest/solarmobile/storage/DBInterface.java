package com.solar.pinterest.solarmobile.storage;

public interface DBInterface {
    void getUser(int id,Listener listener);
    void putUser(DBSchema.User user);

//    void getSession(Listener listener);
//    void addSession(DBSchema.Session session);
//    void deleteSession();
//    void updateSession(DBSchema.Session session, Listener listener);

    interface Listener {
        void onReadUser(DBSchema.User user);
    }
}
