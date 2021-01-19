package com.example.sportify;

public class Member {
    public String fName, lName, email, password,number, id, profilepic;

    public Member() {}

    public Member(String fName, String lName, String email,String password, String number) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.number = number;
        this.password = password;
    }
}

