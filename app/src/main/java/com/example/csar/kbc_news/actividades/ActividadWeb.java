package com.example.csar.kbc_news.actividades;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.csar.kbc_news.R;

public class ActividadWeb extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_web);
        WebView wv = (WebView) findViewById(R.id.webview);
        wv.loadUrl(getIntent().getStringExtra("URL"));
    }


}
