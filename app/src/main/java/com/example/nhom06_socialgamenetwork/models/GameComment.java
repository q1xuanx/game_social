package com.example.nhom06_socialgamenetwork.models;

public class GameComment {
    private String nameComment;
    private String content;

    public GameComment(){

    }
    public GameComment(String nameComment, String content) {
        this.nameComment = nameComment;
        this.content = content;
    }
    public String getNameComment() {
        return nameComment;
    }

    public void setNameComment(String nameComment) {
        this.nameComment = nameComment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
