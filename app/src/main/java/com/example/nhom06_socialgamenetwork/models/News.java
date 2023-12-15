package com.example.nhom06_socialgamenetwork.models;

public class News {
    private String idPic;
    private String title;
    private String details;

    public News(){

    }
    public News(String idPic, String title) {
        this.idPic = idPic;
        this.title = title;
    }
    public News(String idPic, String title, String details) {
        this.idPic = idPic;
        this.title = title;
        this.details = details;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
