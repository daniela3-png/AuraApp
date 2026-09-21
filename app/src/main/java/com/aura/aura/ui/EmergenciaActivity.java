package com.aura.aura.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.aura.aura.R;
import com.aura.aura.factory.PerfilFactory;
import com.aura.aura.models.PerfilUsuario;

public class EmergenciaActivity extends AppCompatActivity {

    private Button btnFonoMujer, btnFonoNinos, btnFonoSuicidio;
    private TextView tvContactosDinamicos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencia);

        btnFonoMujer = findViewById(R.id.btnFonoMujer);
        btnFonoNinos = findViewById(R.id.btnFonoNinos);
        btnFonoSuicidio = findViewById(R.id.btnFonoSuicidio);
        tvContactosDinamicos = findViewById(R.id.tvContactosDinamicos);

        // Llamadas oficiales
        btnFonoMujer.setOnClickListener(v -> llamarLineaOficial("1455"));
        btnFonoNinos.setOnClickListener(v -> llamarLineaOficial("147"));
        btnFonoSuicidio.setOnClickListener(v -> llamarLineaOficial("*4141"));

        // Recibir la edad desde el Dashboard (asegúrate de enviarla en el Intent cuando abras esta pantalla)
        int edad = getIntent().getIntExtra("EDAD_USUARIA", 22);

        // Instanciar el perfil usando el Factory Pattern
        PerfilUsuario perfil = PerfilFactory.crearPerfil(edad);

        // Inyectar los contactos hardcodeados según el perfil
        tvContactosDinamicos.setText(perfil.getContactosEmergencia());
    }

    private void llamarLineaOficial(String numero) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + numero));
        startActivity(intent);
    }
}