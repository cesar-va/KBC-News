package com.example.csar.kbc_news.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.csar.kbc_news.R;

import java.util.zip.Inflater;

import static com.example.csar.kbc_news.R.id.compartir;
import static com.example.csar.kbc_news.R.id.favorito;

public class ActividadBase extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_base);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                final String appPackageName = getPackageName();

                switch (item.getItemId()) {
                    case R.id.cuenta:
                        Intent cuenta = new Intent(getApplicationContext(), ActividadCuenta.class);
                        startActivity(cuenta);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.inicio:
                        Intent inicio = new Intent(getApplicationContext(), ActividadPrincipal.class);
                        startActivity(inicio);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.categorias:
                        Intent categorias = new Intent(getApplicationContext(), ActividadCategoria.class);
                        startActivity(categorias);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.clima:
                        Intent clima = new Intent(getApplicationContext(), ActividadClima.class);
                        startActivity(clima);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.cambio:
                        Intent cambio = new Intent(getApplicationContext(), ActividadDivisas.class);
                        startActivity(cambio);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.marcadores:
                        Intent marcadores = new Intent(getApplicationContext(), ActividadMarcadores.class);
                        startActivity(marcadores);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.configuracion:
                        Intent configuracion = new Intent(getApplicationContext(), ActividadConfiguracion.class);
                        startActivity(configuracion);
                        drawerLayout.closeDrawers();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

/*    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater infla = getMenuInflater();
        infla.inflate(R.menu.menu_search, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case filtros:
                //Mensaje("Op. 1");
                break;
                break;
            default:
                break;
        }
        return true;
    }*/

    // Overrides the pending Activity transition by performing the "Enter" animation.
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    // Overrides the pending Activity transition by performing the "Exit" animation.
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}