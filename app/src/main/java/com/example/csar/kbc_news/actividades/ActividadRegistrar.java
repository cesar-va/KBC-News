package com.example.csar.kbc_news.actividades;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.csar.kbc_news.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class ActividadRegistrar extends ActividadBase {
    private EditText nombreEditText;
    private EditText emailEditText;
    private EditText contrasenaEditText;
    private EditText paisEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_registrar);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_login, contentFrameLayout);
        getSupportActionBar().setTitle("Registrar Cuenta");

        nombreEditText = findViewById(R.id.campoNombre);
        emailEditText = findViewById(R.id.campoCorreoRegistro);
        contrasenaEditText = findViewById(R.id.campoContrasenaRegistro);
        paisEditText = findViewById(R.id.campoPais);

        mAuth = FirebaseAuth.getInstance();

        Button MiButton = findViewById(R.id.botonRegistrar);
        MiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                registrarUsuario();
            }
        });


        AutoCompleteTextView textView = findViewById(R.id.campoPais);
        String[] paises = getResources().getStringArray(R.array.lista_paises);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, paises);
        textView.setAdapter(adapter);
    }

    public void registrarUsuario() {
        if (validarFormulario()) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Cargando...");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), contrasenaEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser usuario = mAuth.getCurrentUser();
                                ref.child(usuario.getUid()).child("nombre").setValue(nombreEditText.getText().toString());
                                ref.child(usuario.getUid()).child("pais").setValue(paisEditText.getText().toString());
                                enviarVerificacionEmail();
                                mAuth.signOut();
                                progressDialog.dismiss();
                            }
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            if (e instanceof FirebaseAuthUserCollisionException)
                                emailEditText.setError("Ya existe un usuario con ese correo");
                            else if (e instanceof FirebaseAuthWeakPasswordException)
                                contrasenaEditText.setError("La contraseña es muy débil");
                            else
                                mensaje("Ocurrió un problema al registrar la cuenta, inténtelo de nuevo");
                        }
                    });
        }
    }

    public void enviarVerificacionEmail(){
        final FirebaseUser usuario = mAuth.getCurrentUser();
        usuario.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            mensajeToast("Correo de verificación enviado a: " + emailEditText.getText().toString());
                            startActivity(new Intent(getApplicationContext(), ActividadLogin.class));
                        }
                         else {
                            mensaje("Ocurrió un problema al enviar el correo de verificación");
                        }
                    }
                });
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
