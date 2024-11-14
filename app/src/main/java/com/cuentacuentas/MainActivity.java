package com.cuentacuentas;

import static com.cuentacuentas.RandomString.randomString;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Button B_CrearCuenta = findViewById(R.id.CrearCuenta);
        Button B_UnirseCuenta = findViewById(R.id.UnirseCuenta);


        B_CrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.comparir_codigo);
            }
        });
        B_UnirseCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.unirse_mesa);
                Button Desp_codmesa=findViewById(R.id.Desp_codmesa);
                Desp_codmesa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setContentView(R.layout.solicitar_nombre);
                    }
                });

            }
    });





        Map<String, Object> USUARIO = new HashMap<>();
        USUARIO.put("Nombre","VARIABLE_NOMBRE_AQUI");
        USUARIO.put("Producto","VALOR_PRODUCTO_AQUI");
        USUARIO.put("Valor","VALOR_PRODUCTO_AQUI");
        
        //VARIABLE ALEATORIO;
        String aleatorio = randomString();

        db.collection("*VARIABLE VALOR ALEATORIO AQUI*")
                .add(USUARIO)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Error adding document", e);
                    }
                });
    }
}