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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilitar el borde a borde para aprovechar el espacio de pantalla
        EdgeToEdge.enable(this);

        // Establecer el diseño de la actividad
        setContentView(R.layout.activity_main);

        // Referencias a los botones en el diseño
        final Button btnLoginCliente = findViewById(R.id.btnLoginCliente);
        final Button btnLoginAdmin = findViewById(R.id.btnLoginAdmin);

        // Configurar OnClickListener para el botón "Login Cliente"
        btnLoginCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegación a la actividad HomeClienteActivity cuando se hace clic en el botón
                Intent intent = new Intent(MainActivity.this, HomeClienteActivity.class);
                startActivity(intent);
            }
        });

        // Configurar OnClickListener para el botón "Login Admin"
        btnLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegación a la actividad HomeAdminActivity cuando se hace clic en el botón
                Intent intent = new Intent(MainActivity.this, HomeAdminActivity.class);
                startActivity(intent);
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
