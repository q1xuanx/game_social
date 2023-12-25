package com.example.nhom06_socialgamenetwork.models;

public class User {
    private String email;
    private String pass;
    private String fullname;
    private int isAdmin;
    private int reputation;

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public User(){

    }

    public User(String email, String pass, String fullname) {
        this.email = email;
        this.pass = pass;
        this.fullname = fullname;
        this.isAdmin = 0;
        this.reputation = 0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}
