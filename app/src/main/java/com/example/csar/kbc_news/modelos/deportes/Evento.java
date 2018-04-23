package com.example.csar.kbc_news.modelos.deportes;

import java.util.ArrayList;

/**
 * Created by braya on 4/22/2018.
 */

public class Evento {
    private String id;
    private String scheduled;
    private boolean start_time_tbd;
    private RondaTorneo tournament_round;
    private Temporada season;
    private Torneo tournament;
    private ArrayList<Competidores> competitors;
    private Estadio venue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScheduled() {
        return scheduled;
    }

    public void setScheduled(String scheduled) {
        this.scheduled = scheduled;
    }

    public boolean isStart_time_tbd() {
        return start_time_tbd;
    }

    public void setStart_time_tbd(boolean start_time_tbd) {
        this.start_time_tbd = start_time_tbd;
    }

    public RondaTorneo getTournament_round() {
        return tournament_round;
    }

    public void setTournament_round(RondaTorneo tournament_round) {
        this.tournament_round = tournament_round;
    }

    public Temporada getSeason() {
        return season;
    }

    public void setSeason(Temporada season) {
        this.season = season;
    }

    public Torneo getTournament() {
        return tournament;
    }

    public void setTournament(Torneo tournament) {
        this.tournament = tournament;
    }

    public ArrayList<Competidores> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(ArrayList<Competidores> competitors) {
        this.competitors = competitors;
    }

    public Estadio getVenue() {
        return venue;
    }

    public void setVenue(Estadio venue) {
        this.venue = venue;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id='" + id + '\'' +
                ", scheduled='" + scheduled + '\'' +
                ", start_time_tbd=" + start_time_tbd +
                ", tournament_round=" + tournament_round +
                ", season=" + season +
                ", tournament=" + tournament +
                ", competitors=" + competitors +
                ", venue=" + venue +
                '}';
    }
}
