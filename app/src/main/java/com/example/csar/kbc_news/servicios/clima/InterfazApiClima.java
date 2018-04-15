package com.example.csar.kbc_news.servicios.clima;


import com.example.csar.kbc_news.modelos.clima.RespuestaClima;
import com.example.csar.kbc_news.modelos.noticias.RespuestaNoticias;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*Esta clase brinda el API de las noticias, realiza los request de una manera mas ordenada y sencilla
 * Basado en la implementación de MyTimes por Debajyoti Basak
 *
 * */

public interface InterfazApiClima {



    //Endpoint para obtener las noticias por busqueda avanzada.
    @GET("weather")
    Call<RespuestaClima> obtenerClimaActualCiudad(@Query("lat") String q,
                                            @Query("appid") String appid);

    @GET("weather")
    Call<RespuestaClima> obtenerClimaActualLatLon(@Query("lat") String lat,
                                            @Query("lon") String lon,
                                            @Query("appid") String appid);
}
