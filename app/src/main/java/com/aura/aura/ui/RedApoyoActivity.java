package com.aura.aura.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.aura.aura.R;

public class RedApoyoActivity extends AppCompatActivity {

    private Button btnRutaVidela, btnRutaAguirre, btnRutaGuzman, btnRutaSur, btnRutaHospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_apoyo);

        // Enlazar los 5 botones del XML
        btnRutaVidela = findViewById(R.id.btnRutaVidela);
        btnRutaAguirre = findViewById(R.id.btnRutaAguirre);
        btnRutaGuzman = findViewById(R.id.btnRutaGuzman);
        btnRutaSur = findViewById(R.id.btnRutaSur);
        btnRutaHospital = findViewById(R.id.btnRutaHospital);

        // Configurar los eventos de clic para cada centro
        btnRutaVidela.setOnClickListener(v ->
                abrirNavegacionGPS("CESFAM Cirujano Videla, Iquique, Chile")
        );

        btnRutaAguirre.setOnClickListener(v ->
                abrirNavegacionGPS("CESFAM Cirujano Aguirre, Iquique, Chile")
        );

        btnRutaGuzman.setOnClickListener(v ->
                abrirNavegacionGPS("CESFAM Cirujano Guzman, Iquique, Chile")
        );

        btnRutaSur.setOnClickListener(v ->
                abrirNavegacionGPS("CESFAM Sur, Iquique, Chile")
        );

        btnRutaHospital.setOnClickListener(v ->
                abrirNavegacionGPS("Hospital Regional de Iquique, Chile")
        );
    }

    private void abrirNavegacionGPS(String destino) {
        String url = "https://www.google.com/maps/dir/?api=1&destination=" + Uri.encode(destino);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}