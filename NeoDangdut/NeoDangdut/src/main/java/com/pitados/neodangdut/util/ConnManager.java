package com.pitados.neodangdut.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.pitados.neodangdut.Consts;

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

    public void init(Context context) {
        this.context = context;
    }

    public static ConnManager getInstance() {
        if(instance == null)
            instance = new ConnManager();
        return instance;
    }

    public void downloadFile(String url, DataType type, String albumName, String filename) {
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(url));
        downloadRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        downloadRequest.setTitle(Consts.NOTIF_DOWNLOAD_TITLE);
        downloadRequest.setDescription(Consts.NOTIF_DOWNLOAD_DESCRIPTION);
        String savePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String filePath = "";
        if(type == DataType.AUDIO) {
            // TODO savePath audio
            File file = new File(Consts.APP_BASE_DIR+"/Music/" + albumName);
            if(!file.isDirectory())
                file.mkdir();
            filePath = "/Music/" + albumName + "/" + filename + ".mp3";
        } else {
            // TODO savePath video
            File file = new File(Consts.APP_BASE_DIR+"/Video/");
            if(!file.isDirectory())
                file.mkdir();
            filePath = "/Video/" + filename + ".mp4";
        }

        // TODO add download item to download list

        downloadRequest.setDestinationInExternalPublicDir(Consts.APP_BASE_DIR, filePath);
        dm.enqueue(downloadRequest);
    }

    public boolean fileExist(DataType type, String album, String song) {
        String path = "";

        if(type == DataType.AUDIO) {
            path = Consts.APP_BASE_DIR+"/Music/"+album+"/"+song+".mp3";
            File file = new File(path);

            if(file.exists())
                return  true;
        } else {
            path = Consts.APP_BASE_DIR+"/Video/"+song+".mp4";
            File file = new File(path);

            if(file.exists())
                return  true;
        }

        return false;
    }
}
