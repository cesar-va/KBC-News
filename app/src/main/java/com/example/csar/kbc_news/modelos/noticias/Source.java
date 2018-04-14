package com.example.csar.kbc_news.modelos.noticias;

/*Esta clase modela el objeto Source que viene en el JSON de la peticion a news api
  Los atributos deben llamarse igual al objeto JSON de respuesta
*/

public class Source {
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
        return "Source{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
