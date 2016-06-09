package com.pitados.neodangdut.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by adrianrestuputranto on 6/3/16.
 */
public class SettingPref {
    private String SETTING_DATA = "SETTING_DATA";
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;
    private String NOTIF_STATE = "NOTIF";
    private String DOWNLOAD_WIFI_STATE = "DOWNLOAD";

    public SettingPref(Context context) {
        preferences = context.getSharedPreferences(SETTING_DATA, Activity.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
    }

    public boolean getNotifState() {
        return preferences.getBoolean(NOTIF_STATE, true);
    }

    public boolean getDowloadWifiOnlyState() {
        return preferences.getBoolean(DOWNLOAD_WIFI_STATE, true);
    }

    public void setNotifState(boolean state) {
        preferencesEditor.putBoolean(NOTIF_STATE, state).commit();
    }

    public void setDownloadWifiOnly(boolean state) {
        preferencesEditor.putBoolean(DOWNLOAD_WIFI_STATE, state).commit();
    }
}
