package com.pitados.neodangdut.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by adrianrestuputranto on 6/3/16.
 */
public class UserLoginData {
    private String USER_DATA = "USER_LOGIN_DATA";
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;
    private String USERNAME = "USERNAME";
    private String PASSWORD = "PASSWORD";
    private String REFRESH_TOKEN = "REFRESH_TOKEN";

    // USER DATA
    private String USER_ID = "ID";
    private String FULLNAME = "FULLNAME";
    private String FIRST_NAME = "First";
    private String LAST_NAME = "Last";
    private String PHOTO_URL = "URL";
    private String CREDIT = "COINS";
    // TODO all

    public UserLoginData(Context context) {
        preferences = context.getSharedPreferences(USER_DATA, Activity.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
    }

    public String getUsername() {
        return preferences.getString(USERNAME, "");
    }

    public String getPassword() {
        return preferences.getString(PASSWORD, "");
    }

    public String getRefreshToken() {
        return preferences.getString(REFRESH_TOKEN, "");
    }

    // User data
    public String getUserID() {
        return preferences.getString(USER_ID, "");
    }

    public String getFullname() {
        return preferences.getString(FULLNAME, "Full Name");
    }

    public String getFirstName() {
        return preferences.getString(FIRST_NAME, "First");
    }

    public String getLastName() {
        return preferences.getString(LAST_NAME, "Last");
    }

    public String getPhotoURL() {
        return preferences.getString(PHOTO_URL, "");
    }

    public String getCredit() {
        return preferences.getString(CREDIT, "0");
    }

    public void setUsername(String username) {
        preferencesEditor.putString(USERNAME, username).commit();
    }

    public void setPassword(String password) {
        preferencesEditor.putString(PASSWORD, password).commit();
    }

    public void setRefreshToken(String refreshToken) {
        preferencesEditor.putString(REFRESH_TOKEN, refreshToken).commit();
    }

    public void setUserID(String id) {
        preferencesEditor.putString(USER_ID, id).commit();
    }

    public void setFullName(String fullName) {
        preferencesEditor.putString(FULLNAME, fullName).commit();
    }

    public void setFirstName(String firstName) {
        preferencesEditor.putString(FIRST_NAME, firstName).commit();
    }

    public void setLastName(String lastName) {
        preferencesEditor.putString(LAST_NAME, lastName).commit();
    }

    public void setPhotoURL(String url) {
        preferencesEditor.putString(PHOTO_URL, url).commit();
    }

    public void setCredit(String coins) {
        preferencesEditor.putString(CREDIT, coins).commit();
    }

    public void signOut() {
        setUsername("");
        setPassword("");
    }
}
