package com.example.csar.kbc_news.modelos.cambio;

/**
 * Created by braya on 4/12/2018.
 */

public class RespuestaCambio {
    private Cambio result;
    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Cambio getCambio() {
        return result;
    }

    public void setCambio(Cambio c) {
        this.result = c;
    }
}