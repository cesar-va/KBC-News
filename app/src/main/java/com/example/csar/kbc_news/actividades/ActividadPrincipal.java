package com.example.csar.kbc_news.actividades;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.csar.kbc_news.R;
import com.example.csar.kbc_news.modelos.noticias.Noticia;
import com.example.csar.kbc_news.modelos.noticias.RespuestaNoticias;
import com.example.csar.kbc_news.utils.HttpUtils;
import com.example.csar.kbc_news.utils.VariablesGlobales;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.csar.kbc_news.R.drawable;
import static com.example.csar.kbc_news.R.id;
import static com.example.csar.kbc_news.R.id.compartir;
import static com.example.csar.kbc_news.R.id.content_frame;
import static com.example.csar.kbc_news.R.id.favorito;
import static com.example.csar.kbc_news.R.id.newsContainer;
import static com.example.csar.kbc_news.R.layout;

//
// import static com.example.csar.kbc_news.R.id.favorito;

public class ActividadPrincipal extends ActividadBase {
    private HttpUtils httpUtils = VariablesGlobales.getInstance().getHttpUtils();
    List<Noticia> lNoticias;
    String TituloNoticia = "";
    String UrlNoticia = "";
    String urlImagenNoticia = "";
    String descripcion = "";
    ListView todasNoticias;
    Dialog ventana;
    String spinnerCategorias = "";
    String spinnerPaises = "";
    List<Noticia> noticiasFavoritos;
    boolean verFavoritos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(content_frame);
        getLayoutInflater().inflate(layout.activity_main, contentFrameLayout);
        getSupportActionBar().setTitle("Explorador");

        noticiasFavoritos = new ArrayList<>();

        //Comentar esto si se ejecuta desde un dispositivo real
        this.httpUtils.confiarTodosCertificados();
        todasNoticias = (ListView) findViewById(newsContainer);
        cargarNoticiasRecientes();

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarNoticiasRecientes();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        ventana = new Dialog(ActividadPrincipal.this);
        ventana.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ventana.setContentView(R.layout.filtros);
    }

    public void cargarNoticiasBusquedaAvanzada(String categoria, String q, String pais){

        final ProgressDialog progressDialog  = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Cargando noticias...");
        progressDialog.show();

        // Callback para obtener noticias por pais
        Call<RespuestaNoticias> call = (q.equals("")) ?this.httpUtils.callNoticiasCategoriaPaisSinQ(categoria, pais):
                this.httpUtils.callNoticiasCategoriaPais(categoria, q, pais);

        call.enqueue(new Callback<RespuestaNoticias>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaNoticias> call, @NonNull Response<RespuestaNoticias> response) {
                // Aqui se pone la acción que se ejcutaría una vez que el servidor retorna los datos
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    lNoticias = response.body().getArticles();
                    CustomAdapter cA = new CustomAdapter(lNoticias);
                    todasNoticias.setAdapter(cA);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaNoticias> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                mensaje("Ocurrió un problema al cargar las noticias, inténtelo de nuevo");
            }
        });
    }

    public void cargarNoticiasRecientes() {
        final ProgressDialog progressDialog  = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Cargando noticias...");
        progressDialog.show();

        // Callback para obtener noticias por pais
        Call<RespuestaNoticias> call = this.httpUtils.callNoticiasPorPais("us");
        call.enqueue(new Callback<RespuestaNoticias>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaNoticias> call, @NonNull Response<RespuestaNoticias> response) {
                // Aqui se pone la acción que se ejcutaría una vez que el servidor retorna los datos
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    lNoticias = response.body().getArticles();
                    CustomAdapter cA = new CustomAdapter(lNoticias);
                    todasNoticias.setAdapter(cA);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaNoticias> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                mensaje("Ocurrió un problema al cargar las noticias, inténtelo de nuevo");
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater infla = getMenuInflater();
        infla.inflate(R.menu.menu_contexto_noticia, menu);

        if(firebaseAutenticacion.getInstance().getCurrentUser() == null)
            menu.getItem(0).setVisible(false);

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case compartir:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing News");
                i.putExtra(Intent.EXTRA_TEXT, UrlNoticia);
                startActivity(Intent.createChooser(i, "Compartir Noticia"));
                break;
            case favorito:
                FirebaseUser usuario = firebaseAutenticacion.getCurrentUser();
                String urlFormateado =  UrlNoticia.replaceAll("\\.|# |$|[|]|/|https|http", "");
                Noticia noticia = new Noticia();

                noticia.setTitle(TituloNoticia);
                noticia.setDescription(descripcion);
                noticia.setUrl(UrlNoticia);
                noticia.setUrlToImage(urlImagenNoticia);
                firebaseDatabaseReference.child(usuario.getUid()).child("Favoritos").child(urlFormateado).setValue(noticia);
                break;
            default:
                break;
        }
        return true;
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
                cargarNoticiasBusquedaAvanzada(spinnerCategorias,query,spinnerPaises);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               return true;
            }
        });

        if(firebaseAutenticacion.getInstance().getCurrentUser() == null){
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcionFiltro:
                Button cerrar = (Button) ventana.findViewById(R.id.cancelar);
                cerrar.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        spinnerCategorias="";
                        spinnerPaises="";
                        ventana.dismiss();
                    }
                });

                Button aceptar = (Button) ventana.findViewById(R.id.aceptar);
                aceptar.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Spinner sc=(Spinner) ventana.findViewById(id.categorias);
                        spinnerCategorias = obtenerCodigo(true, sc.getSelectedItem().toString());
                        Spinner sp=(Spinner) ventana.findViewById(id.paises);
                        spinnerPaises = obtenerCodigo(false, sp.getSelectedItem().toString());
                        ventana.dismiss();
                    }
                });

                ventana.show();
                break;
            case id.opcionFavoritos:
                FirebaseUser usuario = firebaseAutenticacion.getCurrentUser();
                verFavoritos = true;
                firebaseDatabaseReference.child(usuario.getUid()).child("Favoritos").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(verFavoritos){
                            noticiasFavoritos.clear();
                            for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                Noticia noticia = postSnapshot.getValue(Noticia.class);
                                noticiasFavoritos.add(noticia);
                            }
                            CustomAdapter cA = new CustomAdapter(noticiasFavoritos);
                            todasNoticias.setAdapter(cA);
                            verFavoritos = false;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case id.opcionNoticias:
                CustomAdapter cA = new CustomAdapter(lNoticias);
                todasNoticias.setAdapter(cA);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String obtenerCodigo(boolean tipo, String valor){
        int index = -1;
        String[] lista = (tipo)?getResources().getStringArray(R.array.lista_categorias)
                :getResources().getStringArray(R.array.lista_paises);
        String[] listaId = (tipo)?getResources().getStringArray(R.array.lista_codigo_categorias)
                :getResources().getStringArray(R.array.lista_codigo_paises);
        for (int i=0;i<lista.length;i++) {
            if (lista[i].equals(valor)) {
                index = i;
                break;
            }
        }
        return listaId[index];
    }

    class CustomAdapter extends BaseAdapter {
        private List<Noticia> noticias;

        public CustomAdapter(List<Noticia> noticias){
            this.noticias = noticias;
        }

        @Override
        public int getCount() {
            return this.noticias.size();
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

            if (noticias.get(i).getUrlToImage() != null)
                Picasso.with(ActividadPrincipal.this).load(noticias.get(i).getUrlToImage())
                        .resize(getWindowManager().getDefaultDisplay().getWidth(),
                                (int) (84 / getWindowManager().getDefaultDisplay().getWidth())).into(im);

            tt.setText(noticias.get(i).getTitle());
            tc.setText(noticias.get(i).getDescription());
            io.setImageResource(drawable.ic_action_overflow);

            registerForContextMenu(io);

            io.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TituloNoticia = noticias.get(i).getTitle();
                    UrlNoticia = noticias.get(i).getUrl();
                    descripcion = noticias.get(i).getDescription();

                    if(noticias.get(i).getUrlToImage() != null)
                        urlImagenNoticia = noticias.get(i).getUrlToImage();

                    openContextMenu(io);
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(getApplicationContext(), ActividadWeb.class);
                    in.putExtra("URL",noticias.get(i).getUrl());
                    in.putExtra("TITLE", noticias.get(i).getTitle());
                    startActivity(in);
                }
            });

            return view;
        }
    }
}