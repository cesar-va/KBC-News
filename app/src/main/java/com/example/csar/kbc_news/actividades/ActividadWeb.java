package com.example.csar.kbc_news.actividades;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.example.csar.kbc_news.R;

public class ActividadWeb extends ActividadBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_web, contentFrameLayout);
        getSupportActionBar().setTitle(getIntent().getStringExtra("TITLE"));
        WebView wv = (WebView) findViewById(R.id.webview);
        wv.loadUrl(getIntent().getStringExtra("URL"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.compartir:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing News");
                i.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra("URL"));
                startActivity(Intent.createChooser(i, "Compartir Noticia"));
                mensajeToast("sdfsfffd");
                break;
            case R.id.favoritos:
                mensajeToast("Agregado a favoritos");
                break;
            case R.id.copiar:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Enlace", getIntent().getStringExtra("URL"));
                clipboard.setPrimaryClip(clip);
                mensajeToast("Enlace copiado");
                break;
            case R.id.navegador:
                Intent in = new Intent(Intent.ACTION_VIEW);
                in.setData(Uri.parse(getIntent().getStringExtra("URL")));
                startActivity(in);
                break;
            default:
                break;
        }
        return true;
    }
}
