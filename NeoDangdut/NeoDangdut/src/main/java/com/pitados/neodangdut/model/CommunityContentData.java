package com.pitados.neodangdut.model;

/**
 * Created by adrianrestuputranto on 5/27/16.
 */
public class CommunityContentData {
    public String ID;
    public String userID;
    public String userName;
    public String firstName;
    public String lastName;
    public String photoURL;
    public int userTotalLikes;
    public String songName;
    public String description;
    public String category;
    public String fileURL;
    public String coverURL;
    public int totalLikes;
    public int totalViews;
    public String created;
    public boolean isLikeable;

    public CommunityContentData(String ID, String userID, String userName, String firstName, String lastName, String photoURL, int userTotalLikes,
                                String songName, String description, String category, String fileURL, String coverURL,
                                int totalLikes, int totalViews, String created, String isLike) {
        this.ID = ID;
        this.userID = userID;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoURL = photoURL;
        this.userTotalLikes = userTotalLikes;
        this.songName = songName;
        this.description = description;
        this.category = category;
        this.fileURL = fileURL;
        this.coverURL = coverURL;
        this.totalLikes = totalLikes;
        this.totalViews = totalViews;
        this.created = created;
        if(isLike.equalsIgnoreCase("true"))
            this.isLikeable = true;
        else
            this.isLikeable = false;

    }
}
