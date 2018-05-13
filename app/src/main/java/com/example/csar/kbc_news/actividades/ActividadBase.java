package com.example.csar.kbc_news.actividades;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csar.kbc_news.R;
import com.example.csar.kbc_news.modelos.cuenta.Usuario;
import com.example.csar.kbc_news.utils.VariablesGlobales;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;

public class ActividadBase extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    protected FirebaseAuth mAuth = VariablesGlobales.getInstance().getmAuth();
    protected DatabaseReference ref = VariablesGlobales.getInstance().getRef();
    protected StorageReference storageRef = FirebaseStorage.getInstance().getReference();

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
        final TextView navUsername = (TextView) headerView.findViewById(R.id.nombreUsuario);
        final ImageView imagenPrincipal = headerView.findViewById(R.id.imagenPrincipal);

        if(mAuth.getInstance().getCurrentUser() != null){
            try{
                final File archivoTemporal = File.createTempFile("images", "jpg");
                StorageReference sRef = storageRef.child(mAuth.getInstance().getCurrentUser().getUid());

                if(sRef != null){
                    sRef.getFile(archivoTemporal)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    imagenPrincipal.setImageBitmap(BitmapFactory.decodeFile(archivoTemporal.getAbsolutePath()));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
                }

            }catch(Exception e){
            }

            ref.child(mAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    navUsername.setText(snapshot.getValue(Usuario.class).getNombre());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
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
                            Intent cuenta = new Intent(getApplicationContext(), ActividadLogin.class);
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
                        Intent configuracion = new Intent(getApplicationContext(), ActividadPreferencias.class);
                        startActivity(configuracion);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.video:
                        Uri uri = Uri.parse( "https://www.youtube.com/watch?v=c0xDkPsqfEM" );
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        break;
                    case R.id.autores:
                            startActivity(new Intent(getApplicationContext(), ActividadAutores.class));
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

    public void mensaje(String mensaje) {
        View v1 = getWindow().getDecorView().getRootView();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(v1.getContext());
        builder1.setMessage(mensaje);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void mensajeToast(String mensaje){
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
}