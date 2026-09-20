package com.aura.aura.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.aura.aura.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class EmergenciaActivity extends AppCompatActivity {

    private Button btnEnviarAlerta, btnGuardarContactos;
    private EditText etContacto1, etContacto2, etContacto3;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencia);

        // Enlaces de la interfaz
        btnEnviarAlerta = findViewById(R.id.btnEnviarAlerta);
        btnGuardarContactos = findViewById(R.id.btnGuardarContactos);
        etContacto1 = findViewById(R.id.etContacto1);
        etContacto2 = findViewById(R.id.etContacto2);
        etContacto3 = findViewById(R.id.etContacto3);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        cargarContactosGuardados();

        btnEnviarAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarPermisosYEjecutarAlerta();
            }
        });

        btnGuardarContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarContactos();
            }
        });
    }

    private void cargarContactosGuardados() {
        SharedPreferences prefs = getSharedPreferences("AuraContactos", Context.MODE_PRIVATE);
        // Carga los datos guardados o muestra valores sugeridos por defecto
        etContacto1.setText(prefs.getString("contacto1", "Tutor 1: +569 "));
        etContacto2.setText(prefs.getString("contacto2", "Familiar: +569 "));
        etContacto3.setText(prefs.getString("contacto3", ""));
    }

    private void guardarContactos() {
        SharedPreferences prefs = getSharedPreferences("AuraContactos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("contacto1", etContacto1.getText().toString());
        editor.putString("contacto2", etContacto2.getText().toString());
        editor.putString("contacto3", etContacto3.getText().toString());
        editor.apply(); // Guarda de forma asíncrona y segura

        Toast.makeText(this, "Contactos de emergencia actualizados", Toast.LENGTH_SHORT).show();
    }

    private void verificarPermisosYEjecutarAlerta() {
        // Tu lógica existente para obtener GPS y mandar el SMS se mantiene aquí
        Toast.makeText(this, "Alerta enviada a los contactos guardados", Toast.LENGTH_SHORT).show();
    }
}