package com.example.nhom06_socialgamenetwork.models;

import java.util.List;

public class News {
    private String idPic;
    private List<String> picNews;
    private String title;
    private String timePost;
    private int Views;

    public News(){

    }
    public News(String idPic, String title) {
        this.idPic = idPic;
        this.title = title;
    }
    public News(String idPic, List<String> details, String time, String title, int views){
        this.idPic = idPic;
        this.title = title;
        this.picNews = details;
        this.timePost = time;
        this.Views = views;
    }
    public News(String idPic, List<String> details, String time, String title) {
        this.idPic = idPic;
        this.title = title;
        this.picNews = details;
        this.timePost = time;
        this.Views = 0;
    }

    public String getIdPic() {
        return idPic;
    }

    public void setIdPic(String idPic) {
        this.idPic = idPic;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getPicNews() {
        return picNews;
    }

    public void setPicNews(List<String> picNews) {
        this.picNews = picNews;
    }

    public String getTimePost() {
        return timePost;
    }

    public void setTimePost(String timePost) {
        this.timePost = timePost;
    }

    public int getViews() {
        return Views;
    }

    public void setViews(int views) {
        Views = views;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
