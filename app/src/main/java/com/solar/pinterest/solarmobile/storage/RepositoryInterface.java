package com.solar.pinterest.solarmobile.storage;

import java.net.HttpCookie;

public interface RepositoryInterface extends DBInterface {
    void getMasterUser(Listener listener);
    void setMasterUser(DBSchema.User master);

    HttpCookie getSessionCookie();
    void setSessionCookie(HttpCookie cookie);
}
