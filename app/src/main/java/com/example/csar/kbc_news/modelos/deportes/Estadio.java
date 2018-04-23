package com.example.csar.kbc_news.modelos.deportes;

/**
 * Created by braya on 4/22/2018.
 */

public class Estadio {
    private String id;
    private String name;
    private String capacity;
    private String city_name;
    private String country_name;
    private String map_coordinates;
    private String country_code;

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

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getMap_coordinates() {
        return map_coordinates;
    }

    public void setMap_coordinates(String map_coordinates) {
        this.map_coordinates = map_coordinates;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    @Override
    public String toString() {
        return "Estadio{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", capacity='" + capacity + '\'' +
                ", city_name='" + city_name + '\'' +
                ", country_name='" + country_name + '\'' +
                ", map_coordinates='" + map_coordinates + '\'' +
                ", country_code='" + country_code + '\'' +
                '}';
    }
}
