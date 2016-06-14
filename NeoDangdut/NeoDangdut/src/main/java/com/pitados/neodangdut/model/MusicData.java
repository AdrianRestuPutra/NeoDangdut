package com.pitados.neodangdut.model;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class MusicData {
    public String ID;
    public String cover;
    public String songTitle;
    public String previewURL;
    public String duration;
    public String price;
    public String discount;
    public String singerID;
    public String singerName;
    public String albumID;
    public String albumName;
    public String albumCover;
    public String labelID;
    public String labelName;
    public String description;
    public String totalPlayed;
    public String totalPurchased;

    public int index;

    public boolean inLibrary;

    public MusicData(int index, String ID, String cover, String songTitle,
                     String previewURL, String duration,
                     String price, String discount,
                     String singerID, String singerName,
                     String albumID, String albumName, String albumCover,
                     String labelID, String labelName,
                     String description, String totalPlayed, String totalPurchased) {

        this.index = index;
        this.ID = ID;
        this.cover = cover;
        this.songTitle = songTitle;
        this.previewURL = previewURL;
        this.duration = duration;
        this.price = price;
        this.discount = discount;
        this.singerID = singerID;
        this.singerName = singerName;
        this.albumID = albumID;
        this.albumName = albumName;
        this.albumCover = albumCover;
        this.labelID = labelID;
        this.labelName = labelName;
        this.description = description;
        this.totalPlayed = totalPlayed;
        this.totalPurchased = totalPurchased;
    }

    public MusicData() {

    }

}
