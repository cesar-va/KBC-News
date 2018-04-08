package com.example.csar.kbc_news.actividades;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.text.InputType;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csar.kbc_news.R;
import com.example.csar.kbc_news.modelos.noticias.Noticia;
import com.example.csar.kbc_news.modelos.noticias.RespuestaNoticias;
import com.example.csar.kbc_news.utils.HttpUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class ActividadPrincipal extends ActividadBase {
    HttpUtils httpUtils = new HttpUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_main, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        loadRecentNews();
    }

    public void loadRecentNews() {
        //Comentar esto si se ejecuta desde un dispositivo real
        this.httpUtils.confiarTodosCertificados();
        final LinearLayout todasNoticias = (LinearLayout) findViewById(R.id.newsContainer);

        // Callback para obtener noticias por pais
        Call<RespuestaNoticias> call = this.httpUtils.callNoticiasPorPais("us");
        call.enqueue(new Callback<RespuestaNoticias>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaNoticias> call, @NonNull Response<RespuestaNoticias> response) {
                // Aqui se pone la acción que se ejcutaría una vez que el servidor retorna los datos
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    List<Noticia> lNoticias = response.body().getArticles();
                    for (Noticia n : lNoticias) {
                        final Noticia not = n;
                        LinearLayout rowNews = new LinearLayout(ActividadPrincipal.this);
                        ImageView iv = new ImageView(ActividadPrincipal.this);
                        TextView et = new TextView(ActividadPrincipal.this);
                        ImageView ip = new ImageView(ActividadPrincipal.this);

                        rowNews.setGravity(Gravity.CENTER);

                        Picasso.with(ActividadPrincipal.this).load(n.getUrl()).into(iv);

                        ip.setImageResource(R.drawable.ic_action_overflow);
                        rowNews.setOrientation(LinearLayout.HORIZONTAL);

                        et.setText(n.getDescription());
                        et.setSingleLine(false);
                        et.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                        et.setGravity(Gravity.CENTER);

                        rowNews.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
                        iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        et.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        ip.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                        registerForContextMenu(ip);

                        rowNews.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(not.getUrl()));
                                startActivity(i);
                            }
                        });

                        rowNews.addView(ip);
                        rowNews.addView(iv);
                        rowNews.addView(et);

                        todasNoticias.addView(rowNews);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaNoticias> call, @NonNull Throwable t) {
                System.out.println("Error");
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater infla = getMenuInflater();
            infla.inflate(R.menu.menu_contexto_noticia, menu);
            super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorito:
                Mensaje("Op. 1");
                break;
            case R.id.compartir:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing News");
                i.putExtra(Intent.EXTRA_TEXT, "www.xnxx.com");
                startActivity(Intent.createChooser(i, "Share URL"));
                break;
            default:
                break;
        }
        return true;
    }

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

