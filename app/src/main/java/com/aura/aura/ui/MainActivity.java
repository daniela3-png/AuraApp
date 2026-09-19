package com.aura.aura.ui; // Si no lo moviste a ui, déjalo como package com.aura.aura;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.aura.aura.R;
import com.aura.aura.factory.PerfilFactory;
import com.aura.aura.models.PerfilUsuario;

public class MainActivity extends AppCompatActivity {

    private EditText etEdad;
    private Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vinculamos la vista XML con Java
        etEdad = findViewById(R.id.etEdad);
        btnContinuar = findViewById(R.id.btnContinuar);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edadStr = etEdad.getText().toString();

                if (!edadStr.isEmpty()) {
                    int edad = Integer.parseInt(edadStr);
                    // Usamos el patrón Factory que creaste en el paso 2
                    PerfilUsuario perfil = PerfilFactory.crearPerfil(edad);

                    if (perfil != null) {
                        // Si se creó el perfil, mostramos un mensaje emergente
                        Toast.makeText(MainActivity.this,
                                "Perfil asignado. Enfoque: " + perfil.getEnfoquePedagogico(),
                                Toast.LENGTH_LONG).show();

                        // TODO: Aquí agregaremos más adelante el redireccionamiento a la pantalla principal
                        // o solicitar los datos del cuidador si la edad es < 14
                    } else {
                        Toast.makeText(MainActivity.this, "Edad no válida para el sistema", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Por favor, ingresa tu edad", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}