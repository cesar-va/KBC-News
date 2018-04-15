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

import com.example.csar.kbc_news.R;
import com.example.csar.kbc_news.modelos.clima.Clima;
import com.example.csar.kbc_news.modelos.clima.RespuestaClima;
import com.example.csar.kbc_news.modelos.noticias.RespuestaNoticias;
import com.example.csar.kbc_news.utils.Constantes;
import com.example.csar.kbc_news.utils.HttpUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadClima extends ActividadBase {
    ArrayList<Clima> weather;
    HttpUtils httpUtils = new HttpUtils();
    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    private String localizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_clima, contentFrameLayout);
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.getMenu().getItem(0).setChecked(true);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        this.httpUtils.confiarTodosCertificados();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mensajeNoTieneGPS();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            localizacion = obtenerUbicacionActual();
        }

        List<String> d = Arrays.asList(localizacion.split(","));
        getClimaLatLon(d.get(0), d.get(1));
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

    public void getClimaCiudad(String q){
        Call<RespuestaClima> call = this.httpUtils.callClimaActualCiudad(q);
        call.enqueue(new Callback<RespuestaClima>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaClima> call, @NonNull Response<RespuestaClima> response) {
                // Aqui se pone la acción que se ejcutaría una vez que el servidor retorna los datos
                if (response.isSuccessful() && response.body().getWeather() != null)
                    weather = response.body().getWeather();
                    MensajeOK(weather.toString());
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaClima> call, @NonNull Throwable t) {
                System.out.println("Error");
            }
        });
    }

    public void getClimaLatLon(String lat, String lon){
        Call<RespuestaClima> call = this.httpUtils.callClimaActualLatLon(lat,lon);
        call.enqueue(new Callback<RespuestaClima>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaClima> call, @NonNull Response<RespuestaClima> response) {
                // Aqui se pone la acción que se ejcutaría una vez que el servidor retorna los datos
                if (response.isSuccessful() && response.body().getWeather() != null)
                    weather = response.body().getWeather();
                MensajeOK(weather.toString());
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
                return String.valueOf(lat) + "," +  String.valueOf(lon);

            } else if (location1 != null) {
                lat = location1.getLatitude();
                lon = location1.getLongitude();
                return String.valueOf(lat) + "," +  String.valueOf(lon);

            } else if (location2 != null) {
                lat = location2.getLatitude();
                lon = location2.getLongitude();
                return String.valueOf(lat) + "," +  String.valueOf(lon);
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
}

