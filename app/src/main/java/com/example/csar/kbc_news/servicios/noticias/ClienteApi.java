package com.example.csar.kbc_news.servicios.noticias;

import com.example.csar.kbc_news.utils.Constantes;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*Esta clase modela el cliente para enviar la peticion
* Basado en la implementación de MyTimes por Debajyoti Basak
* */

public class ClienteApi {

    private static Retrofit retrofit = null;

    public static Retrofit obtenerCliente(OkHttpClient.Builder httpClient) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constantes.NEWS_API_BASE_URL)                                  //<-- URL base del api
                    .addConverterFactory(GsonConverterFactory.create()) //<-- GSON para convertir los objetos json en objetos java
                    .client(httpClient.build())                         //<-- Contruye la petición con OkHttp
                    .build();
        }
        return retrofit;
    }
}