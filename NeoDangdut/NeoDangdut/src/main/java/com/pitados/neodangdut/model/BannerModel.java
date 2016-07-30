package com.pitados.neodangdut.model;

import android.util.Log;

/**
 * Created by adrianrestuputranto on 5/18/16.
 */
public class BannerModel {
    public enum BannerType {
        ALBUM,
        ARTICLE,
        EVENT
    }

    public int id;
    public String imageLink;
    public String title;
    public String description;
    public String link;

    public BannerType dataType;
    public String dataID;

    public BannerModel(int id, String imageLink, String title, String description, String link) {
        this.id = id;
        this.imageLink = imageLink;
        this.title = title;
        this.description = description;
        this.link = link;

        parseUrl(link);
    }

    private void parseUrl(String url) {
        String sub = url.substring(22, url.length());
        Log.d("SUBSTRING", sub);

        if(sub.contains("album")) {
            dataType = BannerType.ALBUM;
            dataID = stripNonDigits(sub);
        } else if(sub.contains("news")) {
            dataType = BannerType.ARTICLE;
            dataID = stripNonDigits(sub);
        } else if(sub.contains("event")) {
            dataType = BannerType.EVENT;
        }
    }

    private String stripNonDigits(
            final CharSequence input){
        final StringBuilder sb = new StringBuilder(
                input.length());
        for(int i = 0; i < input.length(); i++){
            final char c = input.charAt(i);
            if(c > 47 && c < 58){
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
