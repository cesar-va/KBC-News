package com.example.csar.kbc_news.modelos.deportes;

import java.util.ArrayList;

/**
 * Created by braya on 4/22/2018.
 */

public class EventoEstatus {
    private String status;
    private String match_status;
    private int home_score;
    private int away_score;
    private String winner_id;
    private ArrayList<MarcadoresPeriodo> period_scores;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMatch_status() {
        return match_status;
    }

    public void setMatch_status(String match_status) {
        this.match_status = match_status;
    }

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

    public String getWinner_id() {
        return winner_id;
    }

    public void setWinner_id(String winner_id) {
        this.winner_id = winner_id;
    }

    public ArrayList<MarcadoresPeriodo> getPeriod_scores() {
        return period_scores;
    }

    public void setPeriod_scores(ArrayList<MarcadoresPeriodo> period_scores) {
        this.period_scores = period_scores;
    }

    @Override
    public String toString() {
        return "EventoEstatus{" +
                "status='" + status + '\'' +
                ", match_status='" + match_status + '\'' +
                ", home_score=" + home_score +
                ", away_score=" + away_score +
                ", winner_id='" + winner_id + '\'' +
                ", period_scores=" + period_scores +
                '}';
    }
}
