package com.aura.aura.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.aura.aura.R;
import com.aura.aura.factory.PerfilFactory;
import com.aura.aura.models.PerfilUsuario;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombre, etEdadRegistro, etContactoEmergencia, etPin, etNombreTutor;
    private LinearLayout layoutTutor;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombre = findViewById(R.id.etNombre);
        etEdadRegistro = findViewById(R.id.etEdadRegistro);
        etContactoEmergencia = findViewById(R.id.etContactoEmergencia);
        etPin = findViewById(R.id.etPin);
        etNombreTutor = findViewById(R.id.etNombreTutor);
        layoutTutor = findViewById(R.id.layoutTutor);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // Escuchamos los cambios en el campo de la edad
        etEdadRegistro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    int edad = Integer.parseInt(s.toString());
                    // Si es menor de 14, mostramos el campo del tutor
                    if (edad < 14) {
                        layoutTutor.setVisibility(View.VISIBLE);
                    } else {
                        layoutTutor.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnRegistrar.setOnClickListener(v -> guardarPerfil());
    }

    private void guardarPerfil() {
        String nombre = etNombre.getText().toString();
        String edadStr = etEdadRegistro.getText().toString();
        String contacto = etContactoEmergencia.getText().toString();
        String pin = etPin.getText().toString();

        if (nombre.isEmpty() || edadStr.isEmpty() || contacto.isEmpty() || pin.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        int edad = Integer.parseInt(edadStr);
        PerfilUsuario perfil = PerfilFactory.crearPerfil(edad);

        if (perfil == null) {
            Toast.makeText(this, "Edad no válida", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edad < 14 && etNombreTutor.getText().toString().isEmpty()) {
            Toast.makeText(this, "Debe ingresar el nombre del tutor", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Insertar los datos en Room (AppDatabase) en un hilo secundario

        Toast.makeText(this, "Perfil creado exitosamente", Toast.LENGTH_SHORT).show();

        Toast.makeText(this, "Perfil creado exitosamente", Toast.LENGTH_SHORT).show();

        // Redirigimos al Dashboard Principal
        Intent intent = new Intent(RegistroActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish(); // Cierra la pantalla de registro para que la usuaria no pueda volver atrás usando el botón "Atrás" del celular
    }
}