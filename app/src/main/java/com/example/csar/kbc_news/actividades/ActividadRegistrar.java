package com.example.csar.kbc_news.actividades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.csar.kbc_news.R;
import com.example.csar.kbc_news.utils.VariablesGlobales;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class ActividadRegistrar extends ActividadBase {
    private EditText emailEditText;
    private EditText contrasenaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_registrar);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_cuenta, contentFrameLayout);
        getSupportActionBar().setTitle("Registrar Cuenta");

        emailEditText = (EditText) findViewById(R.id.campoCorreoRegistro);
        contrasenaEditText = (EditText) findViewById(R.id.campoContrasenaRegistro);

        mAuth = FirebaseAuth.getInstance();

        Button MiButton = findViewById(R.id.botonRegistrar);
        MiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        if (validarFormulario()) {
            final ProgressDialog progressDialog  = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Cargando...");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), contrasenaEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent intento = new Intent(getApplicationContext(), ActividadPrincipal.class);
                                startActivity(intento);
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    if (e instanceof FirebaseAuthUserCollisionException)
                        emailEditText.setError("Ya existe un usuario con ese correo");
                    else if(e instanceof FirebaseAuthWeakPasswordException)
                        contrasenaEditText.setError("La contraseña es muy débil");
                    else
                        mensaje("Ocurrió un problema al registrar la cuenta, inténtelo de nuevo");
                }
            });
        }
    }

    public boolean validarFormulario() {
        boolean valido = true;

        String email = emailEditText.getText().toString();
        String contrasena = contrasenaEditText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email inválido");
            valido = false;
        } else {
            contrasenaEditText.setError(null);
        }

        if (contrasena.isEmpty() || contrasena.length() < 4 || contrasena.length() > 10) {
            contrasenaEditText.setError("La contraseña debe tener entre 4 y 10 caracteres");
            valido = false;
        } else {
            contrasenaEditText.setError(null);
        }

        return valido;
    }
}
