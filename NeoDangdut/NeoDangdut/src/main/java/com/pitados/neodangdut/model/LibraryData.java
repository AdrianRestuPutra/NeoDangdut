package com.pitados.neodangdut.model;

/**
 * Created by adrianrestuputranto on 6/8/16.
 */
public class LibraryData {
    public String ID;
    public String songTitle;
    public String category;
    public String fileURL;
    public String coverURL;
    public String duration;
    public String price;
    public String dateCreated;
    public String trackNumber;
    public String albumID;
    public String albumName;
    public String albumCover;
    public String singerAlbumID;
    public String singerAlbumName;
    public String singerID;
    public String singerName;
    public String size;

    public LibraryData(String ID, String songTitle, String category, String fileURl, String coverURL,
                            String duration, String price, String dateCreated, String trackNumber,
                            String albumID, String albumName, String albumCover,
                            String singerAlbumID, String singerAlbumName,
                            String singerID, String singerName, String size) {

        this.ID = ID;
        this.songTitle = songTitle;
        this.category = category;
        this.fileURL = fileURl;
        this.coverURL = coverURL;
        this.duration = duration;
        this.price = price;
        this.dateCreated = dateCreated;
        this.trackNumber = trackNumber;
        this.albumID = albumID;
        this.albumName = albumName;
        this.albumCover = albumCover;
        this.singerAlbumID = singerAlbumID;
        this.singerAlbumName = singerAlbumName;
        this.singerID = singerID;
        this.singerName = singerName;
        this.size = size;

    }
}
