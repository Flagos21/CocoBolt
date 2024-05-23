package com.cocobolt.cocobolt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cocobolt.cocobolt.adapter.CartaAdapter;
import com.cocobolt.cocobolt.model.Productos;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartaClienteActivity extends AppCompatActivity {
    RecyclerView cRecycler;
    CartaAdapter cAdapter;
    FirebaseFirestore mFirestore;
    DatabaseReference mDatabase;
    Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta_cliente);

        // Inicialización de Firebase Firestore y Firebase Database
        mFirestore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Configuración del RecyclerView
        cRecycler = findViewById(R.id.recyclerViewSingle);
        cRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Consulta a Firestore para obtener la lista de productos
        Query query = mFirestore.collection("productos");
        FirestoreRecyclerOptions<Productos> firebaseRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Productos>()
                        .setQuery(query, Productos.class)
                        .build();

        // Inicialización y configuración del adaptador para el RecyclerView
        cAdapter = new CartaAdapter(firebaseRecyclerOptions, this);
        cRecycler.setAdapter(cAdapter);

        // Botón para agregar el pedido
        btnAgregar = findViewById(R.id.btnPedido);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegación a la actividad de Reparto
                Intent intent = new Intent(CartaClienteActivity.this, RepartoActivity.class);
                startActivity(intent);

                // Obtener los productos seleccionados
                List<Productos> selectedProducts = getSelectedProducts();

                // Verificar si se han seleccionado productos antes de enviar el pedido
                if (!selectedProducts.isEmpty()) {
                    enviarPedidoAFirebase(selectedProducts);
                } else {
                    Toast.makeText(CartaClienteActivity.this, "No se seleccionaron productos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Comienza a escuchar los cambios en el adaptador cuando la actividad está en primer plano
        cAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Detiene la escucha de cambios en el adaptador cuando la actividad está en segundo plano
        cAdapter.stopListening();
    }

    // Método para obtener los productos seleccionados en el RecyclerView
    private List<Productos> getSelectedProducts() {
        List<Productos> selectedProducts = new ArrayList<>();
        for (int i = 0; i < cAdapter.getItemCount(); i++) {
            if (cAdapter.itemStateArray.get(i, false)) {
                selectedProducts.add(cAdapter.getItem(i));
            }
        }
        return selectedProducts;
    }

    // Método para enviar el pedido a Firebase Database
    private void enviarPedidoAFirebase(List<Productos> selectedProducts) {
        String pedidoId = mDatabase.child("pedidos").push().getKey();
        Map<String, Object> pedidoMap = new HashMap<>();

        // Iterar sobre los productos seleccionados y agregarlos al mapa del pedido
        for (Productos producto : selectedProducts) {
            String productoId = mDatabase.child("pedidos").child(pedidoId).push().getKey();
            Map<String, Object> productoMap = new HashMap<>();
            productoMap.put("nombre", producto.getNombre());
            productoMap.put("precio", producto.getPrecio());
            pedidoMap.put(productoId, productoMap);
        }

        // Guardar el pedido en Firebase Database
        mDatabase.child("pedidos").child(pedidoId).setValue(pedidoMap)
                .addOnSuccessListener(aVoid -> Toast.makeText(CartaClienteActivity.this, "Pedido agregado exitosamente", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(CartaClienteActivity.this, "Error al agregar pedido", Toast.LENGTH_SHORT).show());
    }
}
