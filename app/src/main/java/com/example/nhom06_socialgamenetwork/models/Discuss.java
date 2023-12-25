package com.example.nhom06_socialgamenetwork.models;

import java.util.ArrayList;
import java.util.List;

public class Discuss {
    private String namePost;
    private String title;
    private String idPic;
    private String details;
    private List<Integer> like;
    private List<Integer> dislike;


    public Discuss(){

    }
    public Discuss(String namePost,String title, String idPic, String details) {
        this.namePost=namePost;
        this.title = title;
        this.idPic = idPic;
        this.details = details;
        this.like = new ArrayList<>();
        this.dislike = new ArrayList<>();
    }

    public String getNamePost() {
        return namePost;
    }

    public void setNamePost(String namePost) {
        this.namePost = namePost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdPic() {
        return idPic;
    }

    public void setIdPic(String idPic) {
        this.idPic = idPic;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    public List<Integer> getLike() {
        return like;
    }

    public void setLike(List<Integer> like) {
        this.like = like;
    }

    public List<Integer> getDislike() {
        return dislike;
    }

    public void setDislike(List<Integer> dislike) {
        this.dislike = dislike;
    }
}
