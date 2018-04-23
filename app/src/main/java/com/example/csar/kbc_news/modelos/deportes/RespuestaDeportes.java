package com.example.csar.kbc_news.modelos.deportes;

import java.util.ArrayList;

/**
 * Created by braya on 4/22/2018.
 */

public class RespuestaDeportes {
    private String generated_at;
    private String schema;
    private ArrayList<Resultados> results;

    public String getGenerated_at() {
        return generated_at;
    }

    public void setGenerated_at(String generated_at) {
        this.generated_at = generated_at;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public ArrayList<Resultados> getResults() {
        return results;
    }

    public void setResults(ArrayList<Resultados> results) {
        this.results = results;
    }

}
