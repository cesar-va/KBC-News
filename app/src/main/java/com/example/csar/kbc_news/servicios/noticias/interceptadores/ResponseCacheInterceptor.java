package com.example.csar.kbc_news.servicios.noticias.interceptadores;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/*Esta clase se encarga de realizar un cache para las llamadas al api*/

public class ResponseCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response respuestaOriginal = chain.proceed(chain.request());
        return respuestaOriginal.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=" + 60)
                .build();
    }
}
