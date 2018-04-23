package com.example.csar.kbc_news.servicios.deportes;

/**
 * Created by braya on 4/22/2018.
 */
import com.example.csar.kbc_news.modelos.deportes.RespuestaDeportes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/*Esta clase brinda el API de las noticias, realiza los request de una manera mas ordenada y sencilla
 * Basado en la implementación de MyTimes por Debajyoti Basak
 *
 * */
//Futbol de Asia:
//        https://api.sportradar.us/soccer-t3/as/en/schedules/2018-04-01/results.json?api_key=5rvf3h6uxa2tb7c24t845d4x
//
public interface InterfazApiDeportes {

    @GET("{liga}/{language}/schedules/{date}/results.json")
    Call<RespuestaDeportes> obtenerResultados(@Path("liga") String liga,
                                              @Path("language") String language,
                                              @Path("date") String date,
                                              @Query("api_key") String api_key);


}
