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
    public SparseBooleanArray itemStateArray = new SparseBooleanArray();
    private Activity activity;

    public CartaAdapter(@NonNull FirestoreRecyclerOptions<Productos> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Productos model) {
        holder.bind(model, position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_productos, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        CheckBox btnCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameText);
            price = itemView.findViewById(R.id.priceText);
            btnCheck = itemView.findViewById(R.id.btnCheck);

            btnCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    itemStateArray.put(adapterPosition, isChecked);
                }
            });
        }

        void bind(Productos model, int position) {
            name.setText(model.getNombre());
            price.setText(model.getPrecio());
            btnCheck.setChecked(itemStateArray.get(position, false));
        }
    }
}
