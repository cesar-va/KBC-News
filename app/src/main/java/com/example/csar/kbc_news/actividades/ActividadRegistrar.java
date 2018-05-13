package com.example.csar.kbc_news.actividades;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.csar.kbc_news.R;
import com.example.csar.kbc_news.modelos.cuenta.Usuario;
import com.example.csar.kbc_news.utils.VariablesGlobales;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import com.example.csar.kbc_news.utils.Constantes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;

public class ActividadRegistrar extends ActividadBase {
    private EditText nombreEditText;
    private EditText emailEditText;
    private EditText contrasenaEditText;
    private Spinner paisSpinner;
    private static int RESULT_CARGAR_IMAGEN = 100;
    private String rutaImagen;


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
        paisSpinner = findViewById(R.id.campoPais);

        mAuth = FirebaseAuth.getInstance();

        Button MiButton = findViewById(R.id.botonRegistrar);
        MiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                registrarUsuario();
            }
        });

        ImageView fotoPerfil = findViewById(R.id.fotoPerfil);
        fotoPerfil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Constantes.RESULT_CARGAR_IMAGEN);
            }
        });

        Spinner textView = findViewById(R.id.campoPais);

        ArrayList<String> paises =  new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.lista_paises)));
        paises.add(0, "--- País ---");
        textView.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, paises));
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CARGAR_IMAGEN && resultCode == RESULT_OK && data != null) {
            android.net.Uri selectedImage = data.getData();
            String[] rutasArchivos = { android.provider.MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    rutasArchivos, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(rutasArchivos[0]);
            String rutaArchivo = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = findViewById(R.id.fotoPerfil);
            imageView.setImageBitmap(android.graphics.BitmapFactory.decodeFile(rutaArchivo));
            rutaImagen = rutaArchivo;
        }


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
                                final FirebaseUser usuario = mAuth.getCurrentUser();
                                final Usuario usr = new Usuario(nombreEditText.getText().toString(), paisSpinner.getSelectedItem().toString());

                                if(rutaImagen != null){
                                    Uri file = Uri.fromFile(new java.io.File(rutaImagen));
                                    StorageReference sRef = storageRef.child(usuario.getUid());

                                    sRef.putFile(file)
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    ref.child(usuario.getUid()).setValue(usr);
                                                    enviarVerificacionEmail();
                                                    mAuth.signOut();
                                                    progressDialog.dismiss();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    progressDialog.dismiss();
                                                    mensaje("Ocurrió un error con su foto de perfil");
                                                }
                                            });
                                }else{
                                    ref.child(usuario.getUid()).setValue(usr);
                                    enviarVerificacionEmail();
                                    mAuth.signOut();
                                    progressDialog.dismiss();
                                }

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

    public void enviarVerificacionEmail() {
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

        String nombre = nombreEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String contrasena = contrasenaEditText.getText().toString();
        String pais = paisSpinner.getSelectedItem().toString();


        if(!nombre.matches("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")){
            nombreEditText.setError("Nombre inválido");
            valido = false;
        }else
            nombreEditText.setError(null);

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

        if(pais.equals("--- País ---")){
            TextView errorText = (TextView) paisSpinner.getSelectedView();
            errorText.setError("Seleccione un pais");
        }


        return valido;
    }
}
