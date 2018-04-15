package com.example.csar.kbc_news.actividades;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.csar.kbc_news.R;
import com.example.csar.kbc_news.modelos.clima.Clima;
import com.example.csar.kbc_news.modelos.clima.Principal;
import com.example.csar.kbc_news.modelos.clima.RespuestaClima;
import com.example.csar.kbc_news.modelos.clima.Sistema;
import com.example.csar.kbc_news.modelos.noticias.RespuestaNoticias;
import com.example.csar.kbc_news.utils.Constantes;
import com.example.csar.kbc_news.utils.HttpUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadClima extends ActividadBase {
    ArrayList<Clima> clima;
    Principal principal;
    Sistema sistema;
    String pueblo;
    HttpUtils httpUtils = new HttpUtils();
    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    private String localizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_clima, contentFrameLayout);
        getSupportActionBar().setTitle("Clima");
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        this.httpUtils.confiarTodosCertificados();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mensajeNoTieneGPS();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            localizacion = obtenerUbicacionActual();
        }

        //List<String> d = Arrays.asList(localizacion.split(","));
        getClimaLatLon("9.97535", "-84.1399");
    }

    public void MensajeOK(String msg) {
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

    public void getClimaCiudad(String q) {
        Call<RespuestaClima> call = this.httpUtils.callClimaActualCiudad(q);
        call.enqueue(new Callback<RespuestaClima>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaClima> call, @NonNull Response<RespuestaClima> response) {
                // Aqui se pone la acción que se ejcutaría una vez que el servidor retorna los datos
                if (response.isSuccessful() && response.body().getWeather() != null && response.body() != null) {
                    clima = response.body().getWeather();
                    principal = response.body().getMain();
                    sistema = response.body().getSys();
                    pueblo = response.body().getName();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaClima> call, @NonNull Throwable t) {
                System.out.println("Error");
            }
        });
    }

    public void getClimaLatLon(String lat, String lon) {
        Call<RespuestaClima> call = this.httpUtils.callClimaActualLatLon(lat, lon);
        call.enqueue(new Callback<RespuestaClima>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaClima> call, @NonNull Response<RespuestaClima> response) {
                // Aqui se pone la acción que se ejcutaría una vez que el servidor retorna los datos
                if (response.isSuccessful() && response.body().getWeather() != null && response.body() != null) {
                    clima = response.body().getWeather();
                    principal = response.body().getMain();
                    sistema = response.body().getSys();
                    pueblo = response.body().getName();
                    mostrarDatos();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaClima> call, @NonNull Throwable t) {
                MensajeOK(t.toString());
            }
        });
    }

    private String obtenerUbicacionActual() {
        if (ActivityCompat.checkSelfPermission(ActividadClima.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (ActividadClima.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ActividadClima.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            double lat, lon;


            if (location != null) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                return String.valueOf(lat) + "," + String.valueOf(lon);

            } else if (location1 != null) {
                lat = location1.getLatitude();
                lon = location1.getLongitude();
                return String.valueOf(lat) + "," + String.valueOf(lon);

            } else if (location2 != null) {
                lat = location2.getLatitude();
                lon = location2.getLongitude();
                return String.valueOf(lat) + "," + String.valueOf(lon);
            } else {
                return Constantes.ERROR_UBICACION;
            }
        }
        return Constantes.ERROR_UBICACION;
    }

    protected void mensajeNoTieneGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

     public void mostrarDatos(){
         TextView ciudad = (TextView) findViewById(R.id.city_field);
         TextView fecha = (TextView) findViewById(R.id.updated_field);
         Date currentTime = Calendar.getInstance().getTime();
         ImageView imagen = (ImageView) findViewById(R.id.weather_icon);
         TextView temp = (TextView) findViewById(R.id.current_temperature_field);
         TextView descripcion = (TextView) findViewById(R.id.details_field);
         TextView medida = (TextView) findViewById(R.id.grados);

         ciudad.setText(pueblo+", "+sistema.getCountry());
         fecha.setText(currentTime.toString());

         if (clima.get(0).getIcon() != null)
             Picasso.with(ActividadClima.this).load("http://openweathermap.org/img/w/"+clima.get(0).getIcon()+".png").into(imagen);

         temp.setText(String.valueOf(principal.getTemp()));
         medida.setText("°C");

         descripcion.setText((CharSequence) clima.get(0).getDescription());
     }
}

