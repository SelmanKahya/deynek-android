package com.deynek.app.model;


public class User {

    private String username;
    private String email;
    private Gender gender;
    private String password;

    public User(String username, String password, String email, String gender){
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender == "M" ? Gender.MALE : Gender.FEMALE;

    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    public enum Gender {
        MALE, FEMALE
    }
}
