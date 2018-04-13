package com.example.csar.kbc_news.actividades;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csar.kbc_news.R;
import com.example.csar.kbc_news.modelos.noticias.Noticia;
import com.example.csar.kbc_news.modelos.noticias.RespuestaNoticias;
import com.example.csar.kbc_news.utils.HttpUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.csar.kbc_news.R.drawable;
import static com.example.csar.kbc_news.R.id;
import static com.example.csar.kbc_news.R.id.compartir;
import static com.example.csar.kbc_news.R.id.content_frame;
import static com.example.csar.kbc_news.R.id.favorito;
import static com.example.csar.kbc_news.R.id.nav_view;
import static com.example.csar.kbc_news.R.id.newsContainer;
import static com.example.csar.kbc_news.R.id.opcionFiltro;
import static com.example.csar.kbc_news.R.id.swipe_container;
import static com.example.csar.kbc_news.R.layout;

public class ActividadPrincipal extends ActividadBase {
    HttpUtils httpUtils = new HttpUtils();
    List<Noticia> lNoticias;
    String TituloNoticia = "";
    String UrlNoticia = "";
    ListView todasNoticias;
    Dialog ventana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(content_frame);
        getLayoutInflater().inflate(layout.activity_main, contentFrameLayout);
        getSupportActionBar().setTitle("Explorador");

        //Comentar esto si se ejecuta desde un dispositivo real
        this.httpUtils.confiarTodosCertificados();
        todasNoticias = (ListView) findViewById(newsContainer);

        loadRecentNews();

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecentNews();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        ventana = new Dialog(ActividadPrincipal.this);
    }

    public void loadRecentNews() {
        // Callback para obtener noticias por pais
        Call<RespuestaNoticias> call = this.httpUtils.callNoticiasPorPais("us");
        call.enqueue(new Callback<RespuestaNoticias>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaNoticias> call, @NonNull Response<RespuestaNoticias> response) {
                // Aqui se pone la acción que se ejcutaría una vez que el servidor retorna los datos
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    lNoticias = response.body().getArticles();
                    CustomAdapter cA = new CustomAdapter();
                    todasNoticias.setAdapter(cA);
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
            case favorito:
                Mensaje("Op. 1");
                break;
            case compartir:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing News");
                i.putExtra(Intent.EXTRA_TEXT, UrlNoticia);
                startActivity(Intent.createChooser(i, "Compartir Noticia"));
                break;
            default:
                break;
        }
        return true;
    }

    public void Mensaje(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.opcionFiltro:
                ventana.setContentView(R.layout.filtros);
                Button cerrar = (Button) ventana.findViewById(R.id.btnLogin);
                cerrar.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        ventana.dismiss();
                    }
                });

                ventana.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void mostrarPopUp(){

    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return lNoticias.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int index, View view, ViewGroup viewGroup) {
            final int i = index;
            view = getLayoutInflater().inflate(layout.fila_noticia, null);
            ImageView im = (ImageView) view.findViewById(id.portada);
            TextView tt = (TextView) view.findViewById(id.titulo);
            TextView tc = (TextView) view.findViewById(id.cuerpo);
            final ImageView io = (ImageView) view.findViewById(id.opciones);

            if (lNoticias.get(i).getUrlToImage() != null)
                Picasso.with(ActividadPrincipal.this).load(lNoticias.get(i).getUrlToImage())
                        .resize(getWindowManager().getDefaultDisplay().getWidth(),
                                (int) (84 / getWindowManager().getDefaultDisplay().getWidth())).into(im);

            tt.setText(lNoticias.get(i).getTitle());
            tc.setText(lNoticias.get(i).getDescription());
            io.setImageResource(drawable.ic_action_overflow);

            registerForContextMenu(io);

            io.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TituloNoticia = lNoticias.get(i).getTitle();
                    UrlNoticia = lNoticias.get(i).getUrl();
                    openContextMenu(io);
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(lNoticias.get(i).getUrl()));
                    startActivity(in);
                }
            });

            return view;
        }
    }
}

