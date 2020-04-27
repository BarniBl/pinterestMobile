package com.solar.pinterest.solarmobile.storage;

import java.net.HttpCookie;

public interface RepositoryInterface extends DBInterface {
    void getMasterUser(UserListener listener);
    void setMasterUser(DBSchema.User master);

    void onLogout();

    HttpCookie getSessionCookie();
    void setSessionCookie(HttpCookie cookie);

    void setCsrfToken(String token);
    String getCsrfToken();
}
