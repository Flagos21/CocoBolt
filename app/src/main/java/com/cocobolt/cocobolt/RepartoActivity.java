package com.cocobolt.cocobolt;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RepartoActivity extends AppCompatActivity {
    TextView tvLatitud, tvLongitud, tvDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilitar el modo EdgeToEdge
        EdgeToEdge.enable(this);

        // Establecer el diseño de la actividad
        setContentView(R.layout.activity_reparto);

        // Obtener referencias a los elementos de la interfaz de usuario
        tvLatitud = findViewById(R.id.tvLatitud);
        tvLongitud = findViewById(R.id.tvLongitud);
        tvDireccion = findViewById(R.id.tvDireccion);

        // Verificar si se tienen los permisos necesarios para acceder a la ubicación
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // Si no se tienen permisos, solicitarlos al usuario
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            // Si se tienen permisos, iniciar la obtención de la ubicación
            locationStart();
        }
    }

    // Método para iniciar la obtención de la ubicación del dispositivo
    private void locationStart() {
        // Obtener el servicio de ubicación
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Crear una instancia de Localizacion (clase interna)
        Localizacion Local = new Localizacion();
        // Establecer la actividad actual como la actividad de reparto
        Local.setRepartoActivity(this);

        // Verificar si el GPS está habilitado
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!gpsEnabled){
            // Si el GPS no está habilitado, abrir la configuración para habilitarlo
            Intent intentgps = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intentgps);
        }

        // Verificar si se tienen los permisos necesarios para acceder a la ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si no se tienen permisos, solicitarlos al usuario
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
            return;
        }

        // Solicitar actualizaciones de ubicación desde el proveedor de red y GPS
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, Local);

        // Mostrar un mensaje en los campos de texto para indicar que se está obteniendo la ubicación
        tvLatitud.setText("Localización GPS");
        tvDireccion.setText("");
    }

    // Método para establecer la dirección en función de la ubicación obtenida
    public void setLocation(Location loc) {
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                // Utilizar Geocoder para obtener la dirección a partir de las coordenadas de ubicación
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    // Obtener la primera dirección de la lista y mostrarla en el campo de dirección
                    Address DirCalle = list.get(0);
                    tvDireccion.setText(DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Clase interna para manejar las actualizaciones de ubicación
    public class Localizacion implements LocationListener{
        RepartoActivity repartoActivity;

        // Método para obtener la actividad de reparto
        public RepartoActivity getRepartoActivity(){
            return repartoActivity;
        }

        // Método para establecer la actividad de reparto
        public void setRepartoActivity(RepartoActivity repartoActivity) {
            this.repartoActivity = repartoActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Actualizar la latitud y longitud en los campos de texto
            loc.getLatitude();
            loc.getLongitude();
            tvLatitud.setText(String.valueOf(loc.getLatitude()));
            tvLongitud.setText(String.valueOf(loc.getLongitude()));
            // Llamar al método setLocation de la actividad de reparto para establecer la dirección
            this.repartoActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider){
            // Mostrar un mensaje cuando el proveedor de ubicación está desactivado
            tvLatitud.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider){
            // Mostrar un mensaje cuando el proveedor de ubicación está activado
            tvLatitud.setText("GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){
            // Manejar los cambios en el estado del proveedor de ubicación
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }

        }
    }

}