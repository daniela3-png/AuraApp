package com.aura.aura.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.aura.aura.R;

public class RedApoyoActivity extends AppCompatActivity {

    private ListView lvCentrosAsistenciales;
    private Button btnGuiaFamiliar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_apoyo);

        lvCentrosAsistenciales = findViewById(R.id.lvCentrosAsistenciales);
        btnGuiaFamiliar = findViewById(R.id.btnGuiaFamiliar);

        cargarCentrosTarapaca();

        btnGuiaFamiliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muestra el consejo breve de la guía familiar requerida
                Toast.makeText(RedApoyoActivity.this,
                        "Escuchar sin juzgar, responder con terminología simple y buscar ayuda profesional ante señales de malestar.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarCentrosTarapaca() {
        // Datos estáticos iniciales de Iquique (escalables a JSON/Room después)
        String[] centros = {
                "CESFAM Cirujano Videla (Arturo Fernández 123)",
                "CESFAM Sur (Av. La Tirana 456)",
                "CESFAM Guzmán (Calle Los Rieles 789)",
                "Hospital Regional Dr. Ernesto Torres Galdames"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, centros);
        lvCentrosAsistenciales.setAdapter(adapter);
    }
}