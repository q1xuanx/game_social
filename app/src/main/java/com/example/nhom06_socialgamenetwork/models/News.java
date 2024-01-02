package com.example.nhom06_socialgamenetwork.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class News implements Parcelable {
    private String idPic;
    private List<String> picNews;
    private String title;
    private String timePost;
    private int Views;
    private int isDelete;

    public News(){

    }

    protected News(Parcel in) {
        idPic = in.readString();
        picNews = in.createStringArrayList();
        title = in.readString();
        timePost = in.readString();
        Views = in.readInt();
        isDelete = in.readInt();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
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
        this.isDelete = 0;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(idPic);
        parcel.writeStringList(picNews);
        parcel.writeString(title);
        parcel.writeString(timePost);
        parcel.writeInt(Views);
        parcel.writeInt(isDelete);
    }
}
