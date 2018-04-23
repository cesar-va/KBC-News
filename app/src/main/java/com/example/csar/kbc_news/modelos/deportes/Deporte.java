package com.example.csar.kbc_news.modelos.deportes;

/**
 * Created by braya on 4/22/2018.
 */

public class Deporte {
    private String id;
    private String name;

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

    @Override
    public String toString() {
        return "Deporte{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
