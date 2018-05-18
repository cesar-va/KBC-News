package com.example.csar.kbc_news.actividades;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.csar.kbc_news.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ActividadLogin extends ActividadBase {
    private EditText emailEditText;
    private EditText contrasenaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_login, contentFrameLayout);
        getSupportActionBar().setTitle("Iniciar Sesión");


        TextView textoRegistrar = (TextView) findViewById(R.id.linkRegistrar);

        textoRegistrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intento = new Intent(getApplicationContext(), ActividadRegistrar.class);
                startActivity(intento);
            }
        });

        emailEditText = (EditText) findViewById(R.id.campoCorreoLogin);
        contrasenaEditText = (EditText) findViewById(R.id.campoContrasenaLogin);

        Button botonLogin = findViewById(R.id.botonLogin);
        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                login();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intento = new Intent(getApplicationContext(), ActividadPrincipal.class);
        startActivity(intento);
    }

    public void login() {
        if(validarFormulario()) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Verificando...");
            progressDialog.show();

            firebaseAutenticacion.signInWithEmailAndPassword(emailEditText.getText().toString(), contrasenaEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if(firebaseAutenticacion.getCurrentUser().isEmailVerified()){
                                    progressDialog.dismiss();
                                    Intent intento = new Intent(getApplicationContext(), ActividadPrincipal.class);
                                    startActivity(intento);
                                }else{
                                    firebaseAutenticacion.signOut();
                                    emailEditText.setError("Email pendiente de verificación");
                                    progressDialog.dismiss();
                                }
                            }

                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        progressDialog.dismiss();
                        emailEditText.setError("No existe cuenta con este correo");
                    }else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        progressDialog.dismiss();
                        contrasenaEditText.setError("Contraseña incorrecta");
                    }else{
                        progressDialog.dismiss();
                        mensaje("Ocurrió un problema al iniciar sesión, inténtelo de nuevo");
                    }
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
            contrasenaEditText.setError("Contraseña inválida");
            valido = false;
        } else {
            contrasenaEditText.setError(null);
        }

        return valido;
    }

}
