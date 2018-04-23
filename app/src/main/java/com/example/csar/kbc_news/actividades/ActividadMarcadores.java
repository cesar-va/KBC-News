package com.example.csar.kbc_news.actividades;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csar.kbc_news.R;
import com.example.csar.kbc_news.modelos.deportes.RespuestaDeportes;
import com.example.csar.kbc_news.modelos.deportes.Resultados;
import com.example.csar.kbc_news.utils.HttpUtils;
import com.example.csar.kbc_news.utils.VariablesGlobales;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadMarcadores extends ActividadBase {
    private HttpUtils httpUtils = VariablesGlobales.getInstance().getHttpUtils();
    List<Resultados> resultados;
    ListView todosResultados;
    private static final String TAG = "ActividadMarcadores";
    private EditText fecha;
    private DatePickerDialog.OnDateSetListener fechaListener;
    private String FechaBusqueda;
    String[] ligas = {"Continente Europeo", "Continente Asiático", "Continente Americano", "Internacional"};
    private int tipoLiga = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_marcadores, contentFrameLayout);
        getSupportActionBar().setTitle("Marcadores");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ligas);
        Spinner spinner1 = (Spinner)findViewById(R.id.spinnerLigas);
        spinner1.setAdapter(adapter);

        todosResultados = (ListView) findViewById(R.id.resultsContainer);

        fecha = (EditText)findViewById(R.id.liga);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        fecha.setText(dateFormat.format(date));

        fecha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialo = new DatePickerDialog(
                        ActividadMarcadores.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        fechaListener,
                        year,month,day);
                dialo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialo.show();
            }
        });

        fechaListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Date d = null;
                month = month + 1;
                FechaBusqueda = year + "-" + month + "-" + day;
                try {
                   d = new SimpleDateFormat("yyyy-MM-dd").parse(FechaBusqueda);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fecha.setText(new SimpleDateFormat("yyyy-MM-dd").format(d));
            }
        };

        Spinner ligasSpinner =(Spinner) findViewById(R.id.spinnerLigas);

        ligasSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        //System.out.println(item.toString());     //prints the text in spinner item.
                        tipoLiga = (item.toString().equals("Internacional"))?1:0;
                        cargarMarcadores(item.toString(),"en",fecha.getText().toString());
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        this.httpUtils.confiarTodosCertificados();

        cargarMarcadores("Continente Europeo","en",fecha.getText().toString());

    }

    public void cargarMarcadores(String liga, String language, String date){

        final ProgressDialog progressDialog  = new ProgressDialog(this);
       // progressDialog.setIndeterminate(true);
        //progressDialog.setMessage("Cargando marcadores...");
        //progressDialog.show();
        Call<RespuestaDeportes> call = this.httpUtils.callDeportesResultados(liga, language, date);
        call.enqueue(new Callback<RespuestaDeportes>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaDeportes> call, @NonNull Response<RespuestaDeportes> response) {
                // Aqui se pone la acción que se ejcutaría una vez que el servidor retorna los datos
                if (response.isSuccessful() && response.body().getResults() != null) {
                        resultados = response.body().getResults();
                        CustomAdapterSports cA = new CustomAdapterSports();
                        todosResultados.setAdapter(cA);
                        progressDialog.dismiss();
                        //MensajeOK("Si hay");
                }
                else{
                    progressDialog.dismiss();
                    MensajeOK("No hay resultados para esta fecha.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaDeportes> call, @NonNull Throwable t) {
                Mensaje("No hay resultados en esta fecha");
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intento = new Intent(getApplicationContext(), ActividadPrincipal.class);
        startActivity(intento);
    }

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};

    class CustomAdapterSports extends BaseAdapter {
        @Override
        public int getCount() {
            return resultados.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int index, View view, ViewGroup viewGroup) {
            final int i = index;
            view = getLayoutInflater().inflate(R.layout.fila_resultado, null);
            TextView textpais = (TextView) view.findViewById(R.id.pais);
            TextView textliga = (TextView) view.findViewById(R.id.liga);
            TextView textcasa = (TextView) view.findViewById(R.id.casa);
            TextView textvisita = (TextView) view.findViewById(R.id.visita);
            TextView textmarcador = (TextView) view.findViewById(R.id.marcador);
            TextView textestadio = (TextView) view.findViewById(R.id.estadio);

            if(tipoLiga == 0) {
                textpais.setText(resultados.get(i).getSport_event().getTournament().getCategory().getName() + ","
                        + resultados.get(i).getSport_event().getTournament().getCategory().getCountry_code());
                textestadio.setText(resultados.get(i).getSport_event().getVenue().getName() + ", "
                        + resultados.get(i).getSport_event().getVenue().getCity_name());
            }else{
                textpais.setText("");
                textestadio.setText("");
            }
            textliga.setText(resultados.get(i).getSport_event().getSeason().getName());
            textcasa.setText(resultados.get(i).getSport_event().getCompetitors().get(0).getName() + ", "
                    + resultados.get(i).getSport_event().getCompetitors().get(0).getAbbreviation());

            textvisita.setText(resultados.get(i).getSport_event().getCompetitors().get(1).getName() + ", "
                    + resultados.get(i).getSport_event().getCompetitors().get(1).getAbbreviation());

            textmarcador.setText(String.valueOf(resultados.get(i).getSport_event_status().getHome_score()) + " - "
                    + String.valueOf(resultados.get(i).getSport_event_status().getAway_score()));





//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent in = new Intent(getApplicationContext(), ActividadWeb.class);
//                    in.putExtra("URL",lNoticias.get(i).getUrl());
//                    startActivity(in);
//                }
//            });

            return view;
        }
    }

    public void MensajeOK(String msg){
        View v1 = getWindow().getDecorView().getRootView();
        AlertDialog.Builder builder1 = new AlertDialog.Builder( v1.getContext());
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {} });
        AlertDialog alert11 = builder1.create();
        alert11.show();
        ;};
}
