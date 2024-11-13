package com.cuentacuentas;

import static com.cuentacuentas.RandomString.randomString;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private String Nombre;
    private String Codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Button crearMesa = findViewById(R.id.CrearCuenta);
        Button unirseCuenta = findViewById(R.id.UnirseCuenta);
        String aleatorio = randomString();

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
            setContentView(R.layout.tu_codigo);
            TextView textViewCodigo = findViewById(R.id.tucodigo);
            textViewCodigo.setText(Codigo);
        });


        unirseCuenta.setOnClickListener(v -> {
            setContentView(R.layout.unirse_mesa);
        });
    }

    private void setNombre() {
        Button ingresarNombre = findViewById(R.id.IngresarNombre);
        ingresarNombre.setOnClickListener(v -> {
            EditText name = findViewById(R.id.Nombre);
            Nombre = name.getText().toString();
        });
    }

    public String getCodigo() {
        return Codigo;
    }
}
