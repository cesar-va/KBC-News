package com.example.csar.kbc_news.servicios.noticias;

import com.example.csar.kbc_news.modelos.noticias.RespuestaNoticias;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*Esta clase brinda el API de las noticias, realiza los request de una manera mas ordenada y sencilla
 * Basado en la implementación de MyTimes por Debajyoti Basak
 *
 * */

public interface InterfazApi {
    //Probable ses después
    /*@GET("articles")
    Call<NoticiasRespuesta> getCall(@Query("source") String source,
                                    @Query("sortBy") String sortBy,
                                    @Query("apiKey") String apiKey);*/

    //Enpoint para obtener las ultimas noticias "headlines".
    @GET("top-headlines")
    Call<RespuestaNoticias> obtenerUltimasNoticias(@Query("country") String country,
                                                   @Query("apiKey") String apiKey);

    //Endpoint para obtener las noticias por busqueda avanzada.
    @GET("everything")
    Call<RespuestaNoticias> obtenerNoticiasBusqueda(@Query("q") String query,
                                                    @Query("sortBy") String sortBy,
                                                    @Query("language") String language,
                                                    @Query("apiKey") String apiKey);

    @GET("top-headlines")
    Call<RespuestaNoticias> obtenerNoticiasCategoriaPais(@Query("category") String sources,
                                                         @Query("country") String country,
                                                         @Query("q") String q,
                                                         @Query("apiKey") String apiKey);



    /*
     * categoria, lenguaje, pais
     * */

}
