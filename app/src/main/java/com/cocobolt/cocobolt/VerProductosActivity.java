package com.cocobolt.cocobolt;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cocobolt.cocobolt.adapter.ProductosAdapter;
import com.cocobolt.cocobolt.model.Productos;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class VerProductosActivity extends AppCompatActivity {

    RecyclerView mRecycler;
    ProductosAdapter mAdapter;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilitar EdgeToEdge
        EdgeToEdge.enable(this);

        // Establecer el diseño de la actividad
        setContentView(R.layout.activity_ver_productos);

        // Inicializar Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Obtener una referencia al RecyclerView
        mRecycler = findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Consulta Firestore para obtener los productos
        Query query = mFirestore.collection("productos");

        // Configurar las opciones del adaptador
        FirestoreRecyclerOptions<Productos> firebaseRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Productos>().setQuery(query, Productos.class).build();

        // Inicializar el adaptador con las opciones de Firestore
        mAdapter = new ProductosAdapter(firebaseRecyclerOptions, this);
        mAdapter.notifyDataSetChanged();

        // Establecer el adaptador en el RecyclerView
        mRecycler.setAdapter(mAdapter);

        // Aplicar los insets del sistema a la vista principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Iniciar la escucha del adaptador
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Detener la escucha del adaptador
        mAdapter.stopListening();
    }

}
