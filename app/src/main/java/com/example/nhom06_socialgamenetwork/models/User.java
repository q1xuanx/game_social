package com.example.nhom06_socialgamenetwork.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class User implements Parcelable {
    private String email;
    private String pass;
    private String fullname;
    private int isBanned;
    private int isAdmin;
    private int reputation;

    private List<String> noti;

    protected User(Parcel in) {
        email = in.readString();
        pass = in.readString();
        fullname = in.readString();
        isBanned = in.readInt();
        isAdmin = in.readInt();
        reputation = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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
        this.isBanned = 0;
        this.noti = new ArrayList<>();
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


    public int getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(int isBanned) {
        this.isBanned = isBanned;
    }


    public List<String> getNoti() {
        return noti;
    }

    public void setNoti(List<String> noti) {
        this.noti = noti;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(pass);
        parcel.writeString(fullname);
        parcel.writeInt(isBanned);
        parcel.writeInt(isAdmin);
        parcel.writeInt(reputation);
    }
}
