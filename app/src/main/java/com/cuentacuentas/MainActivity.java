package com.cuentacuentas;

import static com.cuentacuentas.RandomString.randomString;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String Nombre;
    private String Codigo;
    private RecyclerView resumen;
    private Adap_card_consumo_total adapter;
    private GridLayoutManager glm;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DatabaseReference dbReference;

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
            compartir_a_ingresar.setOnClickListener(view -> {
                Paso3();
            });
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
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        double sumaPrecios = 0.0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombreDocumento = document.getString("Nombre");
                            if (nombreDocumento != null && nombreDocumento.equals(Nombre)) {
                                Double precio = document.getDouble("Precio");
                                if (precio != null) {
                                    sumaPrecios += precio;
                                }
                            }
                        }

                        // Mostrar el total en el TextView
                        TextView total = findViewById(R.id.Total_ind);
                        total.setText("Total: " + sumaPrecios);
                    } else {
                        Log.w("Firestore", "Error al obtener documentos: ", task.getException());
                    }
                });



        // Terminar la sesión
        Button terminar = findViewById(R.id.button3);
        terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.res_final_view);
                mostrarinforme();
            }
        });

        // Agregar producto
        Button Agregar = findViewById(R.id.añadir_prod);
        Agregar.setOnClickListener(view -> AgregarProducto());
    }

    private void AgregarProducto() {
        setContentView(R.layout.ingresar_productos);
        Button agregarproducto = findViewById(R.id.button);
        agregarproducto.setOnClickListener(view1 -> {
            EditText Producto = findViewById(R.id.Producto);
            EditText Precio = findViewById(R.id.Precio);
            double precioValor = Double.parseDouble(Precio.getText().toString());
            Map<String, Object> USUARIO = new HashMap<>();
            USUARIO.put("Nombre", Nombre);
            USUARIO.put("Producto", Producto.getText().toString());
            USUARIO.put("Precio", precioValor);

            db.collection(Codigo)
                    .add(USUARIO)
                    .addOnSuccessListener(documentReference ->
                            Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId())
                    )
                    .addOnFailureListener(e ->
                            Log.w("Firestore", "Error adding document", e));
            Paso4();
        });
    }

    private void mostrarinforme() {
        setContentView(R.layout.res_final_view);

        // Usuarios y sus costos
        Map<String, Double> usuarios = new HashMap<>();

        db.collection(Codigo)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot usuarioSnapshot : task.getResult()) {
                            String nombre_ind = usuarioSnapshot.getString("Nombre");
                            Double precio_ind = usuarioSnapshot.getDouble("Precio");

                            if (nombre_ind != null && precio_ind != null) {
                                Double sumaprecio = usuarios.getOrDefault(nombre_ind, 0.0);
                                usuarios.put(nombre_ind, sumaprecio + precio_ind);
                            }
                        }

                        // Mostrar los resultados después de cargar los datos
                        resumen = findViewById(R.id.nombre_cantidad);
                        glm = new GridLayoutManager(this, 1); // 1 columna
                        resumen.setLayoutManager(glm);
                        adapter = new Adap_card_consumo_total(usuarios);
                        resumen.setAdapter(adapter);

                        // Mostrar el total
                        TextView total = findViewById(R.id.cantidadPagar);
                        Double dinero = usuarios.values().stream().mapToDouble(Double::doubleValue).sum();
                        total.setText(""+dinero);
                    } else {
                        Log.w("Firestore", "Error al obtener documentos: ", task.getException());
                    }
                });
    }
    }


