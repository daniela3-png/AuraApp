package com.aura.aura.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.aura.aura.R;

public class CalendarioActivity extends AppCompatActivity {

    private EditText etFechaInicio;
    private EditText etFechaFin;
    private Button btnGuardarCiclo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        etFechaInicio = findViewById(R.id.etFechaInicio);
        etFechaFin = findViewById(R.id.etFechaFin);
        btnGuardarCiclo = findViewById(R.id.btnGuardarCiclo);

        btnGuardarCiclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inicio = etFechaInicio.getText().toString();
                String fin = etFechaFin.getText().toString();

                if (!inicio.isEmpty() && !fin.isEmpty()) {
                    // Aquí más adelante guardaremos en la base de datos Room usando un hilo secundario
                    Toast.makeText(CalendarioActivity.this, "Fechas guardadas correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CalendarioActivity.this, "Por favor, completa ambas fechas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}