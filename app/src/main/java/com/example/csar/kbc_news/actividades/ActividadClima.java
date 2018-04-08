package com.example.csar.kbc_news.actividades;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;

import com.example.csar.kbc_news.R;
import com.example.csar.kbc_news.modelos.noticias.RespuestaNoticias;
import com.example.csar.kbc_news.utils.HttpUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadClima extends ActividadBase {

    HttpUtils httpUtils = new HttpUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_clima, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        //Comentar esto si se ejecuta desde un dispositivo real
        this.httpUtils.confiarTodosCertificados();

        // Callback para obtener noticias por pais
        Call<RespuestaNoticias> call = this.httpUtils.callNoticiasPorPais("de");
        call.enqueue(new Callback<RespuestaNoticias>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaNoticias> call, @NonNull Response<RespuestaNoticias> response) {
                // Aqui se pone la acción que se ejcutaría una vez que el servidor retorna los datos
                if (response.isSuccessful() && response.body().getArticles() != null)
                    MensajeOK(response.body().getArticles().toString());
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaNoticias> call, @NonNull Throwable t) {
                System.out.println("Error");
            }
        });
    }

    public void MensajeOK(String msg) {
        View v1 = getWindow().getDecorView().getRootView();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(v1.getContext());
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}

