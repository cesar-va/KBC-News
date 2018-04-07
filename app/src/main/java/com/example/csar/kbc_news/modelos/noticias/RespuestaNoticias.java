package com.example.csar.kbc_news.modelos.noticias;

import java.util.ArrayList;

/*Esta clase modela los datos correspondientes a la respuesta de la peticion a news api
 Los atributos deben llamarse igual al objeto JSON de respuesta
 */

public class RespuestaNoticias {
    private String status;
    private int totalResults;
    private ArrayList<Noticia> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<Noticia> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Noticia> articles) {
        this.articles = articles;
    }
}