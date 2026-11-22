package com.aura.aura.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aura.aura.R;
import com.aura.aura.database.AppDatabase;
import com.aura.aura.models.Usuario;

import java.util.Calendar;
import java.util.concurrent.Executors;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombre, etEmail, etFechaNacimiento, etPin, etNombreTutor;
    private LinearLayout layoutTutor;
    private Button btnRegistrar;

    private int edadCalculada = 0; // Se calcula automáticamente al elegir la fecha

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Enlazamos con el diseño XML
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        etPin = findViewById(R.id.etPin);
        etNombreTutor = findViewById(R.id.etNombreTutor);
        layoutTutor = findViewById(R.id.layoutTutor);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // Hacemos que el campo de fecha no sea editable con el teclado, solo al hacer clic
        etFechaNacimiento.setFocusable(false);
        etFechaNacimiento.setClickable(true);
        etFechaNacimiento.setOnClickListener(v -> mostrarCalendario());

        btnRegistrar.setOnClickListener(v -> guardarPerfil());
    }

    private void mostrarCalendario() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, yearSeleccionado, monthOfYear, dayOfMonth) -> {
                    // Formatear y mostrar la fecha en el campo
                    String fecha = yearSeleccionado + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    etFechaNacimiento.setText(fecha);

                    // Calcular la edad actual en base a la fecha de nacimiento
                    edadCalculada = year - yearSeleccionado;
                    if (month < monthOfYear || (month == monthOfYear && day < dayOfMonth)) {
                        edadCalculada--;
                    }

                    // Mostrar u ocultar el campo del tutor según la edad (Lógica RBAC)
                    if (edadCalculada < 14) {
                        layoutTutor.setVisibility(View.VISIBLE);
                    } else {
                        layoutTutor.setVisibility(View.GONE);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void guardarPerfil() {
        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String fechaStr = etFechaNacimiento.getText().toString().trim();
        String pin = etPin.getText().toString().trim();

        // --- 1. VALIDACIONES DE LA RÚBRICA ---
        if (nombre.isEmpty() || email.isEmpty() || fechaStr.isEmpty() || pin.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pin.length() != 4) {
            Toast.makeText(this, "El PIN debe tener exactamente 4 dígitos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edadCalculada < 14 && etNombreTutor.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Debe ingresar el nombre del tutor", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- 2. INTEGRACIÓN CON SQLITE (CREATE) ---
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
                Usuario nuevoUsuario = new Usuario();

                nuevoUsuario.nombre = nombre;
                nuevoUsuario.email = email;
                nuevoUsuario.pin = pin;
                nuevoUsuario.edad = edadCalculada;

                // Asignamos perfil y estado de cuenta según la edad
                if (edadCalculada < 14) {
                    nuevoUsuario.tipo_perfil = "PerfilInfantil";
                    nuevoUsuario.nombreTutor = etNombreTutor.getText().toString().trim();
                    nuevoUsuario.estado_cuenta = "Pendiente_Aprobacion";
                } else if (edadCalculada < 19) {
                    nuevoUsuario.tipo_perfil = "PerfilAdolescente";
                    nuevoUsuario.estado_cuenta = "Activa";
                } else {
                    nuevoUsuario.tipo_perfil = "PerfilAdulto";
                    nuevoUsuario.estado_cuenta = "Activa";
                }

                // Inserción en la base de datos local
                db.usuarioDao().insertarUsuario(nuevoUsuario);

                runOnUiThread(() -> {
                    Toast.makeText(RegistroActivity.this, "¡Perfil creado exitosamente!", Toast.LENGTH_SHORT).show();
                    // Redirigimos al Login para que inicie sesión con su nuevo correo y PIN
                    startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(RegistroActivity.this, "Error al crear el perfil: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }
}