package com.pitados.neodangdut.model;

/**
 * Created by adrianrestuputranto on 6/16/16.
 */
public class AlbumItem {
    public String category;
    public String ID;
    public String trackNum;
    public String songName;
    public String price;
    public String discount;
    public String singerID;
    public String singerName;
    public String duration;
    public String previewURL;

    public String albumName;

    public boolean purchased;
    public boolean inLibrary;

    public AlbumItem(String category, String ID, String trackNum, String albumName, String price, String discount, String singerID, String singerName,
                     String duration , String previewURL) {
        this.category = category;
        this.ID = ID;
        this.trackNum = trackNum;
        this.songName = albumName;
        this.price = price;
        this.discount = discount;
        this.singerID = singerID;
        this.singerName = singerName;
        this.duration = duration;
        this.previewURL = previewURL;
    }

}
