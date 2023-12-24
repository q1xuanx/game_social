package com.example.nhom06_socialgamenetwork.models;

public class Discuss {
    private String namePost;
    private String title;
    private String idPic;
    private String details;
    private int like;
    private int dislike;


    public Discuss(){

    }
    public Discuss(String namePost,String title, String idPic, String details) {
        this.namePost=namePost;
        this.title = title;
        this.idPic = idPic;
        this.details = details;
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

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }
}
