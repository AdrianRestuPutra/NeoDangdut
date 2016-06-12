package com.pitados.neodangdut.model;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class NewsData {
    public String ID;
    public String title;
    public String thumbnailURL;
    public String content;
    public String tags;
    public String slug;
    public int totalLikes;
    public String created;
    public boolean isLikeable;

    public NewsData(String ID, String title, String thumbnailURL, String content,
                    String tags, String slug, int totalLikes, String created, String isLikeable) {
        this.ID = ID;
        this.title = title;
        this.thumbnailURL = thumbnailURL;
        this.content = content;
        this.tags = tags;
        this.slug = slug;
        this.totalLikes = totalLikes;
        this.created = created;
        if(isLikeable.equalsIgnoreCase("true"))
            this.isLikeable = true;
        else
            this.isLikeable = false;
    }

    public NewsData() {

    }
}
