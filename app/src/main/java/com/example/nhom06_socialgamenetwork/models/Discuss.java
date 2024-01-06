package com.example.nhom06_socialgamenetwork.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Discuss implements Parcelable{
    private String namePost;
    private String title;
    private String idPic;
    private String details;
    private List<String> like;
    private List<String> dislike;
    private String datePost;

    private List<CommentDiscuss> comment;
    private int isClosed = 0;
    private int isDelete = 0;

    public Discuss(){

    }
    public Discuss(String namePost,String title, String idPic, String details) {
        this.namePost=namePost;
        this.title = title;
        this.idPic = idPic;
        this.details = details;
        this.like = new ArrayList<>();
        this.dislike = new ArrayList<>();
        this.comment = new ArrayList<>();
        this.isClosed = 0;
        this.isDelete = 0;
    }

    protected Discuss(Parcel in) {
        namePost = in.readString();
        title = in.readString();
        idPic = in.readString();
        details = in.readString();
        like = in.createStringArrayList();
        dislike = in.createStringArrayList();
        datePost = in.readString();
        isClosed = in.readInt();
        isDelete = in.readInt();
    }

    public static final Creator<Discuss> CREATOR = new Creator<Discuss>() {
        @Override
        public Discuss createFromParcel(Parcel in) {
            return new Discuss(in);
        }

        @Override
        public Discuss[] newArray(int size) {
            return new Discuss[size];
        }
    };

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


    public List<String> getLike() {
        return like;
    }

    public void setLike(List<String> like) {
        this.like = like;
    }

    public List<String> getDislike() {
        return dislike;
    }

    public void setDislike(List<String> dislike) {
        this.dislike = dislike;
    }

    public List<CommentDiscuss> getComment() {
        return comment;
    }

    public void setComment(List<CommentDiscuss> comment) {
        this.comment = comment;
    }

    public int getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(int isClosed) {
        this.isClosed = isClosed;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(namePost);
        parcel.writeString(title);
        parcel.writeString(idPic);
        parcel.writeString(details);
        parcel.writeStringList(like);
        parcel.writeStringList(dislike);
        parcel.writeString(datePost);
        parcel.writeInt(isClosed);
        parcel.writeInt(isDelete);
    }
}
