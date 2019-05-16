package com.era.checkmelanoma.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefConfig {

    private static final String LOGIN_STATUS = "login_status";
    private final static String TOKEN = "token";


    private final static String FILE_NAME = "preferences";
    private SharedPreferences preferences;
    private Context context;

    public PrefConfig(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(FILE_NAME, 0);
    }

    private SharedPreferences.Editor getEditor() {
        return preferences.edit();
    }

    //login status
    public void setLoginStatus(boolean status) {
        getEditor().putBoolean(LOGIN_STATUS, status).commit();
    }
    public boolean getLoginStatus() {
        return preferences.getBoolean(LOGIN_STATUS, false);
    }

    //token
    public void setToken(String str) {
        getEditor().putString(TOKEN, str).commit();
    }
    public String getToken() {
        return preferences.getString(TOKEN, "");
    }



}
