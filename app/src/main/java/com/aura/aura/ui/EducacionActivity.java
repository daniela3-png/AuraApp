package com.aura.aura.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.aura.aura.R;
import com.aura.aura.factory.PerfilFactory;
import com.aura.aura.models.PerfilUsuario;
import java.util.List;

public class EducacionActivity extends AppCompatActivity {

    private TextView tvEnfoquePedagogico;
    private ListView lvTemas;
    private Button btnIrCitas;
    private PerfilUsuario perfilActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educacion);

        tvEnfoquePedagogico = findViewById(R.id.tvEnfoquePedagogico);
        lvTemas = findViewById(R.id.lvTemas);
        btnIrCitas = findViewById(R.id.btnIrCitas);

        // Simularemos que recuperamos la edad de la base de datos (por ahora pondremos 15 como ejemplo)
        // TODO: Leer la edad real desde la base de datos Room (AppDatabase)
        int edadUsuaria = 15;

        // Aquí ocurre la magia del Factory: obtenemos el perfil correcto
        perfilActual = PerfilFactory.crearPerfil(edadUsuaria);

        if (perfilActual != null) {
            cargarDatosEnPantalla();
        }
    }

    private void cargarDatosEnPantalla() {
        tvEnfoquePedagogico.setText(perfilActual.getEnfoquePedagogico());

        // Llenar la lista con los temas habilitados para esta edad
        List<String> temas = perfilActual.getTemasHabilitados();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, temas);
        lvTemas.setAdapter(adapter);

        // Ocultar o mostrar el botón de citas médicas según las reglas de negocio
        if (perfilActual.isModuloCitasHabilitado()) {
            btnIrCitas.setVisibility(View.VISIBLE);
        } else {
            btnIrCitas.setVisibility(View.GONE);
        }
    }
}