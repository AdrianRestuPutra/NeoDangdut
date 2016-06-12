package com.pitados.neodangdut.model;

/**
 * Created by adrianrestuputranto on 6/12/16.
 */
public class RegisterModel {
    public String username;
    public String password;
    public String email;
    public String firstName;
    public String lastName;
    public String gender;

    public String birthday;
    public String city;
    public String country;

    public RegisterModel(String username, String password, String email,
                         String firstName, String lastName, String gender,
                         String birthday, String city, String country) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.gender = gender;

        this.city = city;
        this.country = country;
    }
}
