package com.example.csar.kbc_news.actividades;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.widget.FrameLayout;

import com.example.csar.kbc_news.R;

public class ActividadConfiguracion extends ActividadBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_configuracion, contentFrameLayout);
        getSupportActionBar().setTitle("Configuración");
    }
}
