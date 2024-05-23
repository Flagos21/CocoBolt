package com.cocobolt.cocobolt;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.cocobolt.cocobolt.databinding.ActivityMapaLocalBinding;

public class MapaLocalActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapaLocalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflar el diseño de la actividad utilizando ViewBinding
        binding = ActivityMapaLocalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtener el SupportMapFragment y ser notificado cuando el mapa esté listo para ser utilizado
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipula el mapa una vez que esté disponible.
     * Este callback se activa cuando el mapa está listo para ser utilizado.
     * Aquí es donde podemos agregar marcadores o líneas, agregar escuchas o mover la cámara.
     * En este caso, simplemente agregamos un marcador cerca de Sydney, Australia.
     * Si los servicios de Google Play no están instalados en el dispositivo, se pedirá al usuario
     * que los instale dentro del SupportMapFragment. Este método solo se activará una vez que el
     * usuario haya instalado los servicios de Google Play y haya regresado a la aplicación.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Agregar un marcador en Sydney y mover la cámara
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
