package com.example.csar.kbc_news.actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.csar.kbc_news.R;
import com.example.csar.kbc_news.utils.VariablesGlobales;
import com.google.firebase.auth.FirebaseAuth;

public class ActividadInformacionUsuario extends ActividadBase {

    private FirebaseAuth mAuth = VariablesGlobales.getInstance().getmAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_informacion_usuario, contentFrameLayout);
        getSupportActionBar().setTitle("Información");

        Mensaje(mAuth.getCurrentUser().getEmail().toString());

        Button MiBoton = (Button) findViewById(R.id.botonCerrarSesion);

        MiBoton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                mAuth.getInstance().signOut();
                Intent intento = new Intent(getApplicationContext(), ActividadPrincipal.class);
                startActivity(intento);
            }
        });

    }

    public void Mensaje(String msg) {
        View v1 = getWindow().getDecorView().getRootView();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(v1.getContext());
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
