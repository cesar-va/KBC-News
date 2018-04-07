package com.example.csar.kbc_news.utils;

import com.example.csar.kbc_news.modelos.noticias.RespuestaNoticias;
import com.example.csar.kbc_news.servicios.noticias.ClienteApi;
import com.example.csar.kbc_news.servicios.noticias.InterfazApi;
import com.example.csar.kbc_news.servicios.noticias.interceptadores.ResponseCacheInterceptor;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;

/*Esta clase ayuda a preparar las peticiones a los api*/

public class HttpUtils {

    private OkHttpClient.Builder builder = new OkHttpClient.Builder();

    public HttpUtils(){

    }

    public void confiarTodosCertificados() {
        try {
            // Se crea un trust manager que no valide los "certificate chains"
            final TrustManager[] confiarTodosCertificados = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext contextoSSL = SSLContext.getInstance("SSL");
            contextoSSL.init(null, confiarTodosCertificados, new java.security.SecureRandom());

            // Se crea un "factory ssl socket " con el trust manager creado
            final SSLSocketFactory sslSocketFactory = contextoSSL.getSocketFactory();


            this.builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)confiarTodosCertificados[0]);
            this.builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Call<RespuestaNoticias> callNoticiasPorPais(String pais){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        this.builder.addNetworkInterceptor(new ResponseCacheInterceptor());
        //builder.cache(new okhttp3.Cache(new File(App.obtenerInstancia().getCacheDir(), "ResponsesCache"), 10 * 1024 * 1024));
        this.builder.readTimeout(60, TimeUnit.SECONDS);
        this.builder.connectTimeout(60, TimeUnit.SECONDS);
        this.builder.addInterceptor(logging);

        InterfazApi request = ClienteApi.obtenerCliente(this.builder).create(InterfazApi.class);

        Call<RespuestaNoticias> call = request.obtenerUltimasNoticias(pais, Constantes.NEWS_API_KEY);
        return call;
    }

}
