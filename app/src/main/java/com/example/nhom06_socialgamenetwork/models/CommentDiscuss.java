package com.example.nhom06_socialgamenetwork.models;

public class CommentDiscuss {
    private String nameUser;
    private String comment;

    public CommentDiscuss(){

    }

    public CommentDiscuss(String nameUser, String comment) {
        this.nameUser = nameUser;
        this.comment = comment;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
