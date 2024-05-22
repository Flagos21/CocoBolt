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
import java.util.Objects;

import com.cocobolt.cocobolt.MapaLocalActivity;

public class HomeClienteActivity extends AppCompatActivity {

    private int MY_PERMISSION_REQUEST_READ_CONTACTS;
    private FusedLocationProviderClient fusedLocationClient;
    DatabaseReference mDataBase;
    private Button mBtnMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_cliente);

        final Button mBtnMap = findViewById(R.id.btnMapaLocal);
        final Button btnCarta = findViewById(R.id.btnCarta);

        btnCarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar HomeAdminActivity
                Intent intent = new Intent(HomeClienteActivity.this, CartaClienteActivity.class);
                startActivity(intent); // Iniciar la actividad HomeAdminActivity
            }
        });
        mBtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeClienteActivity.this, MapaLocalActivity.class);
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        SubirLatLongFirebase();
    }

    private void SubirLatLongFirebase() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
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
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.e("Latitud: ",+location.getLatitude()+"Longitud: "+location.getLongitude());

                            Map<String, Object> latlong = new HashMap<>();
                            latlong.put("latitud",location.getLatitude());
                            latlong.put("longitud",location.getLongitude());
                            mDataBase.child("usuarios").push().setValue(latlong);
                        }
                    }
                });
    }

}