package com.aura.aura.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.aura.aura.R;

public class DashboardActivity extends AppCompatActivity {

    private Button btnIrCalendario, btnIrEducacion, btnIrRedApoyo, btnIrEmergencia, btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnIrCalendario = findViewById(R.id.btnIrCalendario);
        btnIrEducacion = findViewById(R.id.btnIrEducacion);
        btnIrRedApoyo = findViewById(R.id.btnIrRedApoyo);
        btnIrEmergencia = findViewById(R.id.btnIrEmergencia);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion); // El nuevo botón

        btnIrCalendario.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, CalendarioActivity.class)));
        btnIrEducacion.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, EducacionActivity.class)));
        btnIrRedApoyo.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, RedApoyoActivity.class)));
        btnIrEmergencia.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, EmergenciaActivity.class)));

        // Lógica para Cerrar Sesión y volver al Login
        btnCerrarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            // Limpiamos el historial de pantallas para que no pueda volver con el botón "Atrás"
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}