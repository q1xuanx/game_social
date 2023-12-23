package com.example.nhom06_socialgamenetwork.models;

import android.graphics.Point;

public class GameComment {
    private String nameComment;
    private String content;
    private int point;

    public GameComment(){

    }
    public GameComment(String nameComment, String content, int Point) {
        this.nameComment = nameComment;
        this.content = content;
        this.point = Point;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
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
