package com.example.csar.kbc_news.actividades;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;

import com.example.csar.kbc_news.R;
import com.example.csar.kbc_news.modelos.clima.Clima;
import com.example.csar.kbc_news.modelos.clima.RespuestaClima;
import com.example.csar.kbc_news.utils.HttpUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadClima extends ActividadBase implements LocationListener {
    private ArrayList<Clima> weather;
    private HttpUtils httpUtils = new HttpUtils();
    private LocationManager locationManager;
    private Criteria criterio;
    private String mejorProvedor;
    private double latitud;
    private double longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.actividad_clima, contentFrameLayout);

        obtenerLocalizacion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        obtenerLocalizacion();
    }

    @Override
    public void onBackPressed() {
        Intent intento = new Intent(getApplicationContext(), ActividadPrincipal.class);
        startActivity(intento);
    }

    public void obtenerLocalizacion() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        criterio = new Criteria();
        mejorProvedor = String.valueOf(locationManager.getBestProvider(criterio, true)).toString();

        // Se pide permisos para accesar a la ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else{
            // Ya tiene permisos, entonces se trata de accesar a la ultima ubicación y se actualizan la latitud y longitud
            Location location = locationManager.getLastKnownLocation(mejorProvedor);
            if (location != null) {
                latitud = location.getLatitude();
                longitud = location.getLongitude();

                getClimaLatLon(String.valueOf(latitud), String.valueOf(longitud));
            }
            // Si no había una ubicación previa, entonces se forza a hacer update de ubicación
            else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
        }
    }


    // Esta es la acción que se ejecuta cuando el usuario decide si dar permisos o no
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                if(!locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )){
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        // Se cambia la ubicación, entonces se actualizan las variables latitud y longitud
        locationManager.removeUpdates(this);
        latitud = location.getLatitude();
        longitud = location.getLongitude();
        getClimaLatLon(String.valueOf(latitud), String.valueOf(longitud));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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

}

