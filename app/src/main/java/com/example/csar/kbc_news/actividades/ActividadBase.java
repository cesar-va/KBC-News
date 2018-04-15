package com.example.csar.kbc_news.actividades;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.csar.kbc_news.R;
import com.example.csar.kbc_news.utils.VariablesGlobales;
import com.google.firebase.auth.FirebaseAuth;

public class ActividadBase extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    private FirebaseAuth mAuth = VariablesGlobales.getInstance().getmAuth();

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

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nombreUsuario);

        if(mAuth.getInstance().getCurrentUser() != null)
            navUsername.setText(mAuth.getInstance().getCurrentUser().getEmail());
        else
            navUsername.setText("Invitado");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                final String appPackageName = getPackageName();



                switch (item.getItemId()) {
                    case R.id.cuenta:
                        if(mAuth.getInstance().getCurrentUser() != null){
                            Intent cuenta = new Intent(getApplicationContext(), ActividadInformacionUsuario.class);
                            startActivity(cuenta);
                        }else{
                            Intent cuenta = new Intent(getApplicationContext(), ActividadCuenta.class);
                            startActivity(cuenta);
                        }

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.inicio:
                        Intent inicio = new Intent(getApplicationContext(), ActividadPrincipal.class);
                        startActivity(inicio);
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

    // Overrides the pending Activity transition by performing the "Enter" animation.
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    // Overrides the pending Activity transition by performing the "Exit" animation.
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}