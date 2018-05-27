package com.example.csar.kbc_news.actividades;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.csar.kbc_news.R;
import com.example.csar.kbc_news.modelos.cuenta.Usuario;
import com.example.csar.kbc_news.utils.Constantes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ActividadRegistrar extends ActividadBase {
    private EditText nombreEditText;
    private EditText emailEditText;
    private EditText contrasenaEditText;
    private Spinner paisSpinner;
    private static int RESULT_CARGAR_IMAGEN = 100;
    private String rutaImagen;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_registrar);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_login, contentFrameLayout);
        getSupportActionBar().setTitle("Registrar Cuenta");

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Cargando...");

        nombreEditText = findViewById(R.id.campoNombre);
        emailEditText = findViewById(R.id.campoCorreoRegistro);
        contrasenaEditText = findViewById(R.id.campoContrasenaRegistro);
        paisSpinner = findViewById(R.id.campoPais);
        firebaseAutenticacion = FirebaseAuth.getInstance();

        Button MiButton = findViewById(R.id.botonRegistrar);
        MiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                registrarUsuario();
            }
        });

        // Listener para la foto de perfil
        ImageView fotoPerfil = findViewById(R.id.fotoPerfil);
        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // pidiendo permisos, la respuesta se maneja en onRequestPermissionsResult, en caso de que ya tenga permisos el usuario elige la imagen
                if (ActivityCompat.checkSelfPermission(ActividadRegistrar.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ActividadRegistrar.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constantes.RESULT_CARGAR_IMAGEN);
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, Constantes.RESULT_CARGAR_IMAGEN);
                }
            }
        });

        // Se llena el spinner con los paises
        Spinner textView = findViewById(R.id.campoPais);
        ArrayList<String> paises = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.lista_paises)));
        paises.add(0, "--- País ---");
        textView.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, paises));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // si el usuario permite accesar a sus archivos, entonces se hace un intent para que escoja la foto
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, Constantes.RESULT_CARGAR_IMAGEN);
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // esto se ejecuta una vez que el usuario seleccionó la imagen, se extrae la ruta de la imagen y se setea al image view (se cambia)
        if (requestCode == RESULT_CARGAR_IMAGEN && resultCode == RESULT_OK && data != null) {
            Uri imagenSeleccionada = data.getData();
            String[] rutasArchivos = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imagenSeleccionada,
                    rutasArchivos, null, null, null);
            cursor.moveToFirst();

            int indice = cursor.getColumnIndex(rutasArchivos[0]);
            String rutaArchivo = cursor.getString(indice);
            cursor.close();

            ImageView imagenPerfil = findViewById(R.id.fotoPerfil);
            imagenPerfil.setImageBitmap(BitmapFactory.decodeFile(rutaArchivo));
            rutaImagen = rutaArchivo;
        }
    }

    public void registrarUsuario() {
        if (validarFormulario()) {
            progressDialog.show();

            // 1. Se crea el usuario en firebase
            firebaseAutenticacion.createUserWithEmailAndPassword(emailEditText.getText().toString(), contrasenaEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                final FirebaseUser usuario = firebaseAutenticacion.getCurrentUser();
                                final Usuario usr = new Usuario(nombreEditText.getText().toString(), paisSpinner.getSelectedItem().toString());

                                //2. Una vez completado, se sube la imagen a FirebaseStorage
                                if (rutaImagen != null) {
                                    subirImagen(usuario.getUid(), usr);
                                } else {
                                    firebaseDatabaseReference.child(usuario.getUid()).setValue(usr);
                                    enviarVerificacionEmail();
                                    firebaseAutenticacion.signOut();
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

    public void subirImagen(final String uID, final Usuario u) {
        // 1. Se comprime la imagen y se hace un task para subir la imagen
        StorageReference sRef = firebaseStorageReference.child(uID);
        Bitmap bitmap = BitmapFactory.decodeFile(rutaImagen);
        bitmap = escalarImagen(bitmap);
        ByteArrayOutputStream bitMapStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bitMapStream);
        byte[] datosBitmap = bitMapStream.toByteArray();

        //2. Subir la imagen
        UploadTask tareaSubirImagen = sRef.putBytes(datosBitmap);
        tareaSubirImagen.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                mensaje("Ocurrió un problema al cargar su foto de perfil");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //3. Una vez terminado, se guarda la informacion del usuario usando database normal (JSON)
                firebaseDatabaseReference.child(uID).setValue(u);
                enviarVerificacionEmail();
                firebaseAutenticacion.signOut();
                progressDialog.dismiss();
            }
        });
    }

    // Le cambia el tamano a la imagen para poder subirla mas rapido
    public static Bitmap escalarImagen(Bitmap imagenOriginal) {
        float radio = Math.min(
                (float) 500 / imagenOriginal.getWidth(),
                (float) 500 / imagenOriginal.getHeight());
        int largo = Math.round((float) radio * imagenOriginal.getWidth());
        int alto = Math.round((float) radio * imagenOriginal.getHeight());

        Bitmap nuevoBitmap = Bitmap.createScaledBitmap(imagenOriginal, largo,
                alto, true);
        return nuevoBitmap;
    }

    public void enviarVerificacionEmail() {
        final FirebaseUser usuario = firebaseAutenticacion.getCurrentUser();
        usuario.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            mensajeToast("Correo de verificación enviado a: " + emailEditText.getText().toString());
                            startActivity(new Intent(getApplicationContext(), ActividadLogin.class));
                        } else {
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

        if (!nombre.matches("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")) {
            nombreEditText.setError("Nombre inválido");
            valido = false;
        } else
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

        if (pais.equals("--- País ---")) {
            TextView errorText = (TextView) paisSpinner.getSelectedView();
            errorText.setError("Seleccione un pais");
        }
        return valido;
    }
}
