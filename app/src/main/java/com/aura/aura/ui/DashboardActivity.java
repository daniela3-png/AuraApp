package com.aura.aura.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.aura.aura.R;

public class DashboardActivity extends AppCompatActivity {

    private Button btnIrCalendario, btnIrEducacion, btnIrRedApoyo, btnIrEmergencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnIrCalendario = findViewById(R.id.btnIrCalendario);
        btnIrEducacion = findViewById(R.id.btnIrEducacion);
        btnIrRedApoyo = findViewById(R.id.btnIrRedApoyo);
        btnIrEmergencia = findViewById(R.id.btnIrEmergencia);

        // Navegación al Calendario
        btnIrCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, CalendarioActivity.class));
            }
        });

        // Navegación a Educación
        btnIrEducacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, EducacionActivity.class));
            }
        });

        // Navegación a Red de Apoyo
        btnIrRedApoyo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, RedApoyoActivity.class));
            }
        });

        // Navegación a Emergencia (Botón de Pánico)
        btnIrEmergencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, EmergenciaActivity.class));
            }
        });
    }
}