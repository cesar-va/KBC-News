package com.example.csar.kbc_news.actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.csar.kbc_news.R;

public class ActividadRegistrar extends ActividadBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_registrar);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_cuenta, contentFrameLayout);
    }
}
