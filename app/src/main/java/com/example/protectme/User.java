package com.example.protectme;

public class User {
    String username;
    String firstname;
    String surname;
    String bloodgroup;
    String password;

    public User(String username, String firstname, String surname, String bloodgroup, String password) {
        this.username = username;
        this.firstname = firstname;
        this.surname = surname;
        this.bloodgroup = bloodgroup;
        this.password = password;
    }
}
