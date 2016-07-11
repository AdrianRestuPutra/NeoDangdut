package com.pitados.neodangdut.model;

/**
 * Created by adrianrestuputranto on 6/28/16.
 */
public class CommentData {
    public String ID;
    public String userID;
    public String userName;
    public String photoURL;
    public String firstName;
    public String lastName;
    public String message;
    public String host;
    public String uri;
    public String category;
    public String created;
    public String updated;

    public CommentData(String ID, String userID, String userName, String photoURL, String firstName, String lastName,
                       String message, String host, String uri, String category, String created, String updated) {
        this.ID = ID;
        this.userID = userID;
        this.userName = userName;
        this.photoURL = photoURL;
        this.firstName = firstName;
        this.lastName = lastName;
        this.message = message;
        this.host = host;
        this.uri = uri;
        this.category = category;
        this.created = created;
        this.updated = updated;
    }

}
