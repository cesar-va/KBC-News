package com.example.csar.kbc_news.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.widget.FrameLayout;

import com.example.csar.kbc_news.R;

public class ActividadMarcadores extends ActividadBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_marcadores, contentFrameLayout);
        getSupportActionBar().setTitle("Marcadores");
    }

    @Override
    public void onBackPressed() {
        Intent intento = new Intent(getApplicationContext(), ActividadPrincipal.class);
        startActivity(intento);
    }
}
