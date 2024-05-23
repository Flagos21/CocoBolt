package com.cocobolt.cocobolt.adapter;

import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cocobolt.cocobolt.R;
import com.cocobolt.cocobolt.model.Productos;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CartaAdapter extends FirestoreRecyclerAdapter<Productos, CartaAdapter.ViewHolder> {

    // SparseBooleanArray para almacenar el estado de los elementos seleccionados
    public SparseBooleanArray itemStateArray = new SparseBooleanArray();

    private Activity activity;

    // Constructor
    public CartaAdapter(@NonNull FirestoreRecyclerOptions<Productos> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Productos model) {
        // Llama al método bind para vincular los datos del modelo con las vistas
        holder.bind(model, position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el diseño de los elementos de la lista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_productos, parent, false);
        return new ViewHolder(view);
    }

    // Clase interna ViewHolder para mantener las referencias de las vistas de cada elemento de la lista
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        CheckBox btnCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Asigna las vistas
            name = itemView.findViewById(R.id.nameText);
            price = itemView.findViewById(R.id.priceText);
            btnCheck = itemView.findViewById(R.id.btnCheck);

            // Listener para el cambio de estado del CheckBox
            btnCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Obtiene la posición del elemento en el adaptador y guarda su estado
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    itemStateArray.put(adapterPosition, isChecked);
                }
            });
        }

        // Método para vincular los datos del modelo con las vistas
        void bind(Productos model, int position) {
            name.setText(model.getNombre());
            price.setText(model.getPrecio());
            // Establece el estado del CheckBox según el estado almacenado en itemStateArray
            btnCheck.setChecked(itemStateArray.get(position, false));
        }
    }
}
