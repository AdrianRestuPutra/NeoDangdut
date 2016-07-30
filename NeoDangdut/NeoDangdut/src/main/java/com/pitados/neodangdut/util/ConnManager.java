package com.pitados.neodangdut.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.model.SettingPref;

import java.io.File;

/**
 * Created by adrianrestuputranto on 4/25/16.
 */
public class ConnManager {
    public enum DataType {
        AUDIO,
        VIDEO
    }

    private Context context;
    private static ConnManager instance;

    private SettingPref settingData;

    public void init(Context context) {
        this.context = context;
        settingData = new SettingPref(context);
    }

    public static ConnManager getInstance() {
        if(instance == null)
            instance = new ConnManager();
        return instance;
    }

    public void downloadFile(String url, DataType type, String albumName, String filename) {
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(url));
        if(settingData.getDowloadWifiOnlyState()) {
            downloadRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        } else {
            downloadRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        }

        String ext = (type == DataType.AUDIO) ? ".mp3" : ".mp4";
        downloadRequest.setTitle(Consts.NOTIF_DOWNLOAD_TITLE + filename + ext);
        downloadRequest.setDescription(Consts.NOTIF_DOWNLOAD_DESCRIPTION);
        String filePath = "";
        if(type == DataType.AUDIO) {

            File file = new File(Consts.APP_BASE_DIR+"/Music/" + albumName);
            if(!file.isDirectory())
                file.mkdir();
            filePath = "/Music/" + albumName + "/" + filename + ".mp3";
        } else {

            File file = new File(Consts.APP_BASE_DIR+"/Video/");
            if(!file.isDirectory())
                file.mkdir();
            filePath = "/Video/" + filename + ".mp4";
        }

        downloadRequest.setDestinationInExternalPublicDir(Consts.APP_BASE_DIR, filePath);
        if(settingData.getNotifState() == true)
            downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        dm.enqueue(downloadRequest);
    }

    public boolean fileExist(DataType type, String album, String song) {
        String path = "";

        if(type == DataType.AUDIO) {
            path = "/sdcard/"+Consts.APP_BASE_DIR+"/Music/"+album+"/"+song+".mp3";
            File file = new File(path);

            Log.d("PATH", path + " | " +file.getAbsolutePath());
            if(file.exists()) {
                Log.d("PATH", "File Exists");
                return true;
            }
        } else {
            path = "/sdcard/"+Consts.APP_BASE_DIR+"/Video/"+song+".mp4";
            File file = new File(path);

            Log.d("PATH", path);
            if(file.exists())
                return true;
        }

        return false;
    }
}
