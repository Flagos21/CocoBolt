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

        mFirestore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        cRecycler = findViewById(R.id.recyclerViewSingle);
        cRecycler.setLayoutManager(new LinearLayoutManager(this));

        Query query = mFirestore.collection("productos");
        FirestoreRecyclerOptions<Productos> firebaseRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Productos>()
                        .setQuery(query, Productos.class)
                        .build();

        cAdapter = new CartaAdapter(firebaseRecyclerOptions, this);
        cRecycler.setAdapter(cAdapter);

        btnAgregar = findViewById(R.id.btnPedido);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartaClienteActivity.this, RepartoActivity.class);
                startActivity(intent);
                List<Productos> selectedProducts = getSelectedProducts();
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
        cAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cAdapter.stopListening();
    }

    private List<Productos> getSelectedProducts() {
        List<Productos> selectedProducts = new ArrayList<>();
        for (int i = 0; i < cAdapter.getItemCount(); i++) {
            if (cAdapter.itemStateArray.get(i, false)) {
                selectedProducts.add(cAdapter.getItem(i));
            }
        }
        return selectedProducts;
    }

    private void enviarPedidoAFirebase(List<Productos> selectedProducts) {
        String pedidoId = mDatabase.child("pedidos").push().getKey();
        Map<String, Object> pedidoMap = new HashMap<>();

        for (Productos producto : selectedProducts) {
            String productoId = mDatabase.child("pedidos").child(pedidoId).push().getKey();
            Map<String, Object> productoMap = new HashMap<>();
            productoMap.put("nombre", producto.getNombre());
            productoMap.put("precio", producto.getPrecio());
            pedidoMap.put(productoId, productoMap);
        }

        mDatabase.child("pedidos").child(pedidoId).setValue(pedidoMap)
                .addOnSuccessListener(aVoid -> Toast.makeText(CartaClienteActivity.this, "Pedido agregado exitosamente", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(CartaClienteActivity.this, "Error al agregar pedido", Toast.LENGTH_SHORT).show());
    }
}
