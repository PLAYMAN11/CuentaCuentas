package com.cuentacuentas;

import static com.cuentacuentas.RandomString.randomString;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String Nombre;
    private String Codigo;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button crearMesa = findViewById(R.id.CrearCuenta);
        String aleatorio = randomString();
        Button B_UnirseCuenta = findViewById(R.id.UnirseCuenta);

        // Método para Unirse a una cuenta
        B_UnirseCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.unirse_mesa);

                Button Desp_codmesa = findViewById(R.id.Desp_codmesa);
                Desp_codmesa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText codigoIngresado = findViewById(R.id.editText_codigoMesa);
                        if (codigoIngresado != null && !codigoIngresado.getText().toString().isEmpty()) {
                            Codigo = codigoIngresado.getText().toString();
                            Paso3();
                        } else {
                            Log.d("MainActivity", "Código no válido");
                        }
                    }
                });
            }
        });

        // Método para crear mesa
        crearMesa.setOnClickListener(v -> {
            db.collection(aleatorio)
                    .add(new HashMap<>())
                    .addOnSuccessListener(documentReference ->
                            Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId())
                    )
                    .addOnFailureListener(e ->
                            Log.w("Firestore", "Error adding document", e)
                    );

            Codigo = aleatorio;
            setContentView(R.layout.comparir_codigo);
            TextView textViewCodigo = findViewById(R.id.textView_codigoMesa);
            textViewCodigo.setText(Codigo);

            Button compartir_a_ingresar = findViewById(R.id.compartir_a_ingresar);
            // Add any necessary listener code here if needed
        });
    }

    private void Paso3() {
        setContentView(R.layout.solicitar_nombre);
        Button ingresarNombre = findViewById(R.id.IngresarNombre);
        EditText name = findViewById(R.id.Nombre);

        ingresarNombre.setOnClickListener(v -> {
            if (name.getText().toString().isEmpty()) {
                name.setText("Ingrese un nombre valido");
            } else {
                Nombre = name.getText().toString();
                Paso4();
            }
        });
    }

    private void Paso4() {
        setContentView(R.layout.total_ind);
        TextView holanombre = findViewById(R.id.hola_nombre);
        holanombre.setText("Hola, " + Nombre);

        db.collection(Codigo)
                .whereEqualTo("Nombre", Nombre)  // Replaced 'nombreEspecifico' with 'Nombre'
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        double sumaPrecios = 0.0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Double precio = document.getDouble("Precio");
                            if (precio != null) {
                                sumaPrecios += precio;
                            }
                        }
                        TextView total = findViewById(R.id.Total_ind);
                        total.setText("Total: " + sumaPrecios);
                    } else {
                        Log.w("Firestore", "Error al obtener documentos: ", task.getException());
                    }
                });

        // Terminar la sesión
        Button terminar = findViewById(R.id.button3);
        terminar.setOnClickListener(view -> setContentView(R.layout.res_final_view));

        // Agregar producto
        Button Agregar = findViewById(R.id.añadir_prod);
        Agregar.setOnClickListener(view -> {
            setContentView(R.layout.ingresar_productos);
            Button agregarproducto = findViewById(R.id.button);
            agregarproducto.setOnClickListener(view1 -> {
                EditText Producto = findViewById(R.id.Producto);
                EditText Precio = findViewById(R.id.Precio);

                Map<String, Object> USUARIO = new HashMap<>();
                USUARIO.put("Nombre", Nombre);
                USUARIO.put("Producto", Producto.getText().toString());
                USUARIO.put("Precio", Precio.getText().toString());

                db.collection(Codigo)
                        .add(USUARIO)
                        .addOnSuccessListener(documentReference ->
                                Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId()))
                        .addOnFailureListener(e ->
                                Log.w("Firestore", "Error adding document", e));

                Paso4();  // After adding the product, go back to Paso4
            });
        });
    }
}
