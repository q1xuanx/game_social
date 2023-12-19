package com.example.nhom06_socialgamenetwork.models;

public class Game {
    private String idPic;
    private String nameGame;
    private String detailsGame;
    private int totalPoint;
    private int rateTimes;

    public Game(String idPic,String nameGame, String detailsGame) {
        this.idPic = idPic;
        this.nameGame = nameGame;
        this.detailsGame = detailsGame;
        this.totalPoint = 0;
        this.rateTimes = 0;
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

    public String getDetailsGame() {
        return detailsGame;
    }

    public void setDetailsGame(String detailsGame) {
        this.detailsGame = detailsGame;
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
