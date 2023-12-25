package com.example.nhom06_socialgamenetwork.models;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private String idPic;
    private String nameGame;
    private int totalPoint;
    private int rateTimes;
    private String gameType;
    private List<GameComment> list;

    public Game(){

    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public List<GameComment> getList() {
        return list;
    }

    public void setList(List<GameComment> list) {
        this.list = list;
    }

    public Game(String idPic, String nameGame, String typeGame) {
        this.idPic = idPic;
        this.nameGame = nameGame;
        this.gameType = typeGame;
        this.totalPoint = 0;
        this.rateTimes = 0;
        this.list = new ArrayList<>();
    }
    public Game(String idPic, String nameGame, List<GameComment> list){
        this.idPic = idPic;
        this.nameGame = nameGame;
        this.list = list;
    }
    public String getIdPic() {
        return idPic;
    }

    public void setIdPic(String idPic) {
        this.idPic = idPic;
    }

    public String getNameGame() {
        return nameGame;
    }

    public void setNameGame(String nameGame) {
        this.nameGame = nameGame;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public int getRateTimes() {
        return rateTimes;
    }

    public void setRateTimes(int rateTimes) {
        this.rateTimes = rateTimes;
    }
}
