package com.pitados.neodangdut.model;

import java.util.List;

/**
 * Created by adrianrestuputranto on 5/29/16.
 */
public class AlbumData {
    public String ID;
    public String albumName;
    public String coverURL;
    public String singerID;
    public String singerName;
    public String releaseDate;
    public String price;
    public String discount;
    public String totalPurchased;

    public List<AlbumItem> listAllMusic;

    public AlbumData(String ID, String albumName, String coverURL, String singerID, String singerName,
                     String releaseDate, String price, String discount, String totalPurchased) {
        this.ID = ID;
        this.albumName = albumName;
        this.coverURL = coverURL;
        this.singerID = singerID;
        this.singerName = singerName;
        this.releaseDate = releaseDate;
        this.price = price;
        this.discount = discount;
        this.totalPurchased = totalPurchased;
    }

    public AlbumData(String ID, String albumName, String coverURL, String singerID, String singerName, String price, List<AlbumItem> listAllMusic) {
        this.ID = ID;
        this.albumName = albumName;
        this.coverURL = coverURL;
        this.singerID = singerID;
        this.singerName = singerName;
        this.price = price;

        this.listAllMusic = listAllMusic;
    }

    public AlbumData() {

    }

    private void checkIsAvailable() {

    }

}
