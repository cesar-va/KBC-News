package com.example.csar.kbc_news.modelos.deportes;

/**
 * Created by braya on 4/22/2018.
 */

public class Torneo {
    private String id;
    private String name;
    private Deporte sport;
    private Categoria category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Deporte getSport() {
        return sport;
    }

    public void setSport(Deporte sport) {
        this.sport = sport;
    }

    public Categoria getCategory() {
        return category;
    }

    public void setCategory(Categoria category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Torneo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sport=" + sport +
                ", category=" + category +
                '}';
    }
}
