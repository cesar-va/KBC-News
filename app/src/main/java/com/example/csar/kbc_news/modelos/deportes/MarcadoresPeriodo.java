package com.example.csar.kbc_news.modelos.deportes;

/**
 * Created by braya on 4/22/2018.
 */

public class MarcadoresPeriodo {
    private int home_score;
    private int away_score;
    private String type;
    private int number;

    public int getHome_score() {
        return home_score;
    }

    public void setHome_score(int home_score) {
        this.home_score = home_score;
    }

    public int getAway_score() {
        return away_score;
    }

    public void setAway_score(int away_score) {
        this.away_score = away_score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "MarcadoresPeriodo{" +
                "home_score=" + home_score +
                ", away_score=" + away_score +
                ", type='" + type + '\'' +
                ", number=" + number +
                '}';
    }
}
