package com.pitados.neodangdut.model;

/**
 * Created by adrianrestuputranto on 5/18/16.
 */
public class BannerModel {
    public int id;
    public String imageLink;
    public String title;
    public String description;
    public String link;

    public BannerModel(int id, String imageLink, String title, String description, String link) {
        this.id = id;
        this.imageLink = imageLink;
        this.title = title;
        this.description = description;
        this.link = link;
    }
}
