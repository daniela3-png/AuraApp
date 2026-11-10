package com.aura.aura.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.aura.aura.R;

public class EmergenciaActivity extends AppCompatActivity {

    private Button btnFonoMujer, btnFonoNinos, btnFonoSuicidio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencia);

        btnFonoMujer = findViewById(R.id.btnFonoMujer);
        btnFonoNinos = findViewById(R.id.btnFonoNinos);
        btnFonoSuicidio = findViewById(R.id.btnFonoSuicidio);

        // Llamadas oficiales
        btnFonoMujer.setOnClickListener(v -> llamarLineaOficial("1455"));
        btnFonoNinos.setOnClickListener(v -> llamarLineaOficial("147"));
        btnFonoSuicidio.setOnClickListener(v -> llamarLineaOficial("*4141"));
    }

    private void llamarLineaOficial(String numero) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + numero));
        startActivity(intent);
    }
}