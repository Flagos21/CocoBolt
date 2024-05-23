package com.cocobolt.cocobolt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeAdminActivity extends AppCompatActivity {

    Button btnAgregar;
    Button btnVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilitar el diseño EdgeToEdge para extender el contenido debajo de las barras de sistema
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_home_admin);

        // Referencias a los botones en el diseño
        btnAgregar = findViewById(R.id.btnAgregarProducto);
        btnVer = findViewById(R.id.btnVerProductos);

        // Configuración de OnClickListener para el botón de "Agregar Producto"
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegación a la actividad AgregarProductoActivity
                startActivity(new Intent(HomeAdminActivity.this, AgregarProductoActivity.class));
            }
        });

        // Configuración de OnClickListener para el botón de "Ver Productos"
        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegación a la actividad VerProductosActivity
                startActivity(new Intent(HomeAdminActivity.this, VerProductosActivity.class));
            }
        });

        // Aplicar relleno de sistema a la vista principal para evitar superposiciones con las barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
