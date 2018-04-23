package com.example.csar.kbc_news.modelos.deportes;

/**
 * Created by braya on 4/22/2018.
 */

public class RondaTorneo {
    private String type;
    private int number;

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
        return "RondaTorneo{" +
                "type='" + type + '\'' +
                ", number=" + number +
                '}';
    }
}
