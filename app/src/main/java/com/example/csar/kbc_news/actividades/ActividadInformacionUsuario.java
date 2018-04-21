package com.example.csar.kbc_news.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.csar.kbc_news.R;
import com.example.csar.kbc_news.modelos.cuenta.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ActividadInformacionUsuario extends ActividadBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_informacion_usuario, contentFrameLayout);
        getSupportActionBar().setTitle("Información");

        ref.child(mAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                TextView nombre = findViewById(R.id.nombreCampo);
                TextView pais = findViewById(R.id.paisCampo);

                nombre.setText(usuario.getNombre());
                pais.setText(usuario.getPais());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        TextView email = findViewById(R.id.emailCampo);
        email.setText(mAuth.getCurrentUser().getEmail().toString());

        Button MiBoton = findViewById(R.id.botonCerrarSesion);
        MiBoton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                mAuth.getInstance().signOut();
                Intent intento = new Intent(getApplicationContext(), ActividadPrincipal.class);
                startActivity(intento);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intento = new Intent(getApplicationContext(), ActividadPrincipal.class);
        startActivity(intento);
    }

}
