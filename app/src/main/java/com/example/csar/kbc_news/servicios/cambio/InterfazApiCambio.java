package com.example.csar.kbc_news.servicios.cambio;

import com.example.csar.kbc_news.modelos.cambio.RespuestaCambio;
import com.example.csar.kbc_news.modelos.noticias.RespuestaNoticias;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by braya on 4/12/2018.
 */

public interface InterfazApiCambio {

    @GET("quotes/{source}/{target}/{format}")
    Call<RespuestaCambio> obtenerCambio(@Path("source") String source,
                                        @Path("target") String target,
                                        @Path("format") String format,
                                        @Query("quantity") String quantity,
                                        @Query("key") String key);




}