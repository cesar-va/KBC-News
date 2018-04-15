package com.example.csar.kbc_news.actividades;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csar.kbc_news.R;
import com.example.csar.kbc_news.modelos.cambio.Cambio;
import com.example.csar.kbc_news.modelos.cambio.RespuestaCambio;
import com.example.csar.kbc_news.modelos.noticias.Noticia;
import com.example.csar.kbc_news.modelos.noticias.RespuestaNoticias;
import com.example.csar.kbc_news.utils.HttpUtils;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HEAD;


public class ActividadDivisas extends ActividadBase {
    HttpUtils httpUtils = new HttpUtils();
    ArrayList<String> listaadjunta = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_divisas, contentFrameLayout);

        /*ArchivoTextoAdjuntoALista();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listaadjunta);
        Spinner spinner1 = (Spinner)findViewById(R.id.moneda1);
        Spinner spinner2 = (Spinner)findViewById(R.id.moneda2);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        Button MiButton = (Button) findViewById(R.id.convertir);

        //Programamos el evento onclick

        MiButton.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View arg0) {
                realizarConversion();
            }

        });
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.getMenu().getItem(0).setChecked(true);*/

    }

    public void realizarConversion(){
        /*String cambio = "";
        String moneda1 = "";
        String moneda2 = "";

        EditText e1 = (EditText)findViewById(R.id.cantidad);
        cambio = e1.getText().toString();

        Spinner e2 = (Spinner)findViewById(R.id.moneda1);
        moneda1 = e2.getSelectedItem().toString();
        List<String> op1 = Arrays.asList(moneda1.split(","));

        Spinner e3 = (Spinner)findViewById(R.id.moneda2);
        moneda2 = e3.getSelectedItem().toString();
        List<String> op2 = Arrays.asList(moneda2.split(","));

        //Mensaje(op1.get(0) + " " + op2.get(0));

        this.httpUtils.confiarTodosCertificados();
        Call<RespuestaCambio> call = this.httpUtils.callCambioDosVariables(op1.get(0),op2.get(0),cambio);
        call.enqueue(new Callback<RespuestaCambio>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaCambio> call, @NonNull Response<RespuestaCambio> response) {
                // Aqui se pone la acción que se ejcutaría una vez que el servidor retorna los datos
                if (response.isSuccessful() && response.body().getCambio() != null) {
                   Cambio cambio = response.body().getCambio();
                    EditText e4 = (EditText)findViewById(R.id.resultado);
                    e4.setText(String.valueOf(cambio.getAmount()));
                    Mensaje(cambio.getSource());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaCambio> call, @NonNull Throwable t) {
                System.out.println("Error");
            }
        });*/
    }
    private void ArchivoTextoAdjuntoALista() {
       /* InputStream miarchivo = getResources().openRawResource(R.raw.paises);
        listaadjunta.clear();
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(miarchivo));
            while ((line = br.readLine()) != null) {
                listaadjunta.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {br.close();}
                catch (IOException e)
                {e.printStackTrace();}
            }
        }
        return ;*/
    }

    /*public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};*/

}
