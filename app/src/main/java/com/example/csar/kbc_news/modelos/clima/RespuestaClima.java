package com.example.csar.kbc_news.modelos.clima;

import com.example.csar.kbc_news.modelos.noticias.Noticia;

import java.util.ArrayList;

/**
 * Created by braya on 4/14/2018.
 */

public class RespuestaClima {
    private Coordenadas coord;
    private ArrayList<Clima> weather;
    private String base;
    private Principal main;
    private int visibility;
    private Viento wind;
    private Nubes clouds;
    private int dt;
    private Sistema sys;
    private int id;
    private String name;
    private int cod;

    public Coordenadas getCoord() {
        return coord;
    }

    public void setCoord(Coordenadas coord) {
        this.coord = coord;
    }

    public ArrayList<Clima> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<Clima> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Principal getMain() {
        return main;
    }

    public void setMain(Principal main) {
        this.main = main;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public Viento getWind() {
        return wind;
    }

    public void setWind(Viento wind) {
        this.wind = wind;
    }

    public Nubes getClouds() {
        return clouds;
    }

    public void setClouds(Nubes clouds) {
        this.clouds = clouds;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public Sistema getSys() {
        return sys;
    }

    public void setSys(Sistema sys) {
        this.sys = sys;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    @Override
    public String toString() {
        return "RespuestaClima{" +
                "coord=" + coord +
                ", weather=" + weather +
                ", base='" + base + '\'' +
                ", main=" + main +
                ", visibility=" + visibility +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", dt=" + dt +
                ", sys=" + sys +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", cod=" + cod +
                '}';
    }
}
