package com.cocobolt.cocobolt;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.Manifest;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class HomeClienteActivity extends AppCompatActivity {

    private int MY_PERMISSION_REQUEST_READ_CONTACTS; // Solicitud de permisos
    private FusedLocationProviderClient fusedLocationClient; // Cliente para acceder a la ubicación
    DatabaseReference mDataBase; // Referencia a la base de datos Firebase
    private Button mBtnMap; // Botón para abrir el mapa local

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_cliente);

        // Referencias a los botones en el diseño
        final Button mBtnMap = findViewById(R.id.btnMapaLocal);
        final Button btnCarta = findViewById(R.id.btnCarta);

        // Configuración de OnClickListener para el botón de "Carta"
        btnCarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegación a la actividad CartaClienteActivity
                Intent intent = new Intent(HomeClienteActivity.this, CartaClienteActivity.class);
                startActivity(intent);
            }
        });

        // Configuración de OnClickListener para el botón de "Mapa Local"
        mBtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegación a la actividad MapaLocalActivity
                Intent intent = new Intent(HomeClienteActivity.this, MapaLocalActivity.class);
                startActivity(intent);
            }
        });

        // Aplicar relleno de sistema a la vista principal para evitar superposiciones con las barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar el cliente de ubicación fusionada
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtener una referencia a la base de datos Firebase
        mDataBase = FirebaseDatabase.getInstance().getReference();
        // Método para subir la latitud y longitud a Firebase
        SubirLatLongFirebase();
    }

    // Método para solicitar permisos y obtener la última ubicación conocida
    private void SubirLatLongFirebase() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(HomeClienteActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_READ_CONTACTS);

            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Verificar si la ubicación no es nula
                        if (location != null) {
                            // Imprimir la latitud y longitud en el registro
                            Log.e("Latitud: ",+location.getLatitude()+"Longitud: "+location.getLongitude());

                            // Crear un mapa para almacenar la latitud y longitud
                            Map<String, Object> latlong = new HashMap<>();
                            latlong.put("latitud",location.getLatitude());
                            latlong.put("longitud",location.getLongitude());
                            // Subir la latitud y longitud a la base de datos Firebase
                            mDataBase.child("usuarios").push().setValue(latlong);
                        }
                    }
                });
    }
}
