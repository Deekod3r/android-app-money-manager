package com.project.hucemoney.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SessionManager {
    private static final String PREF_NAME = "HUCEMoneyPref";
    private static final String KEY_UUID = "UUID";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_LOGGED_IN = "isLoggedIn";
    private static final String KEY_SYNC_DATE = "syncDate";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setUUID(String uuid) {
        editor.putString(KEY_UUID, uuid);
        editor.commit();
    }
    public String getUUID() {
        return pref.getString(KEY_UUID, null);
    }

    public void setUsername(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public void setEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public void setLoggedIn() {
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_LOGGED_IN, false);
    }

    public void setSyncDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
        editor.putString(KEY_SYNC_DATE, LocalDateTime.now().format(formatter));
        editor.commit();
    }

    public String getSyncDate() {
        return pref.getString(KEY_SYNC_DATE, null);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }
}


