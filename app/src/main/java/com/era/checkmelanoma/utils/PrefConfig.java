package com.era.checkmelanoma.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PrefConfig {

    private static final String LOGIN_STATUS = "login_status";
    private final static String TOKEN = "token";
    private final static String PHOTOS = "photos";


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

    // photos
    public void setPhotos(Map<Integer, String> photos) {
        Gson gson = new Gson();
        String photosString = gson.toJson(photos);
        getEditor().putString(PHOTOS, photosString).commit();
    }
    public Map<Integer, String> getPhotos() {
        Gson gson = new Gson();
        String storedPhotos = preferences.getString(PHOTOS, "{}");
        Type type = new TypeToken<HashMap<Integer, String>>(){}.getType();
        return gson.fromJson(storedPhotos, type);
    }


}
