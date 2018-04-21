package com.example.csar.kbc_news.modelos.cuenta;

public class Usuario {
    private String nombre;
    private String pais;

    public Usuario() {
    }

    public Usuario(String nombre, String pais) {
        this.nombre = nombre;
        this.pais = pais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
