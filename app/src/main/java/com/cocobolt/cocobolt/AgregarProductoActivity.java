package com.cocobolt.cocobolt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AgregarProductoActivity extends AppCompatActivity {

    ImageView imgAgregar;
    Button btnAgregarP, btnAgregarI ;
    EditText nombre, precio, descripcion;
    private FirebaseFirestore mfirestore;

    StorageReference storageReference;
    String storage_path = "productos/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";
    String idd;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_producto);
        mfirestore = FirebaseFirestore.getInstance();

        nombre = findViewById(R.id.inputNombreP);
        precio = findViewById(R.id.inputPrecioP);
        descripcion = findViewById(R.id.inputDescripcionP);

        btnAgregarP = findViewById(R.id.btnAgregarP);


        btnAgregarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nombre.getText().toString().trim();
                String price = precio.getText().toString().trim();
                String description = descripcion.getText().toString().trim();

                if (name.isEmpty() && price.isEmpty() && description.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Ingrese los datos", Toast.LENGTH_SHORT).show();

                }else{
                    postProducto(name, price, description);

                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void postProducto(String nombre, String price, String description) {
        Map<String,Object> map = new HashMap<>();
        map.put("nombre", nombre);
        map.put("precio", price);
        map.put("descripcion", description);

        mfirestore.collection("productos").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Creacion exitosa", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al enviar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}