package com.example.csar.kbc_news.actividades;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.widget.FrameLayout;

import com.example.csar.kbc_news.R;

public class ActividadDivisas extends ActividadBase {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_divisas, contentFrameLayout);
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.getMenu().getItem(0).setChecked(true);
    }
}
