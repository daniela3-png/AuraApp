package com.aura.aura.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.UUID;
import java.util.concurrent.Executors;

public class RegistroActivity extends AppCompatActivity {

    // Nota: Agregué etEmail. Necesitarás añadir este EditText en tu XML (activity_registro.xml)
    private EditText etNombre, etEmail, etFechaNacimiento, etContactoEmergencia, etPin, etNombreTutor;
    private LinearLayout layoutTutor;
    private Button btnRegistrar;

    private int edadCalculada = 0;
    private String fechaNacimientoStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etFechaNacimiento = findViewById(R.id.etEdadRegistro); // Reutilizamos tu ID actual para la fecha
        etPin = findViewById(R.id.etPin);
        etNombreTutor = findViewById(R.id.etNombreTutor);
        layoutTutor = findViewById(R.id.layoutTutor);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // Configuramos el campo para que abra un calendario en lugar de escribir a mano
        etFechaNacimiento.setFocusable(false);
        etFechaNacimiento.setClickable(true);
        etFechaNacimiento.setOnClickListener(v -> mostrarSelectorFecha());

        btnRegistrar.setOnClickListener(v -> guardarPerfil());
    }

    private void mostrarSelectorFecha() {
        final Calendar c = Calendar.getInstance();
        int yearActual = c.get(Calendar.YEAR);
        int monthActual = c.get(Calendar.MONTH);
        int dayActual = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, yearSeleccionado, monthOfYear, dayOfMonth) -> {
                    // Guardamos la fecha en formato YYYY-MM-DD para SQLite
                    fechaNacimientoStr = yearSeleccionado + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    etFechaNacimiento.setText(fechaNacimientoStr);

                    // Algoritmo para calcular la edad exacta
                    edadCalculada = yearActual - yearSeleccionado;
                    if (monthActual < monthOfYear || (monthActual == monthOfYear && dayActual < dayOfMonth)) {
                        edadCalculada--;
                    }

                    // Regla de Negocio: Si es menor de 14, pedimos tutor
                    if (edadCalculada < 14) {
                        layoutTutor.setVisibility(View.VISIBLE);
                    } else {
                        layoutTutor.setVisibility(View.GONE);
                    }
                }, yearActual, monthActual, dayActual);
        datePickerDialog.show();
    }

    private void guardarPerfil() {
        String nombre = etNombre.getText().toString();
        String pin = etPin.getText().toString();
        String nombreTutor = etNombreTutor.getText().toString();

        String email = etEmail.getText().toString();

        if (nombre.isEmpty() || email.isEmpty() || fechaNacimientoStr.isEmpty() || pin.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edadCalculada < 14 && nombreTutor.isEmpty()) {
            Toast.makeText(this, "Debe ingresar el nombre del tutor", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Instanciamos la Entidad de Room
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.nombre = nombre;
        nuevoUsuario.email = email;
        nuevoUsuario.pin_acceso = pin;
        nuevoUsuario.fecha_nacimiento = fechaNacimientoStr;

        // 2. Aplicamos la lógica de perfiles y vinculación del diagrama
        if (edadCalculada < 14) {
            nuevoUsuario.tipo_perfil = "PerfilInfantil";
            nuevoUsuario.estado_cuenta = "Pendiente_Aprobacion";
            // Genera un código tipo "AURA-4F8A"
            nuevoUsuario.codigo_vinculacion = "AURA-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            nuevoUsuario.id_tutor = null;
        } else if (edadCalculada < 18) {
            nuevoUsuario.tipo_perfil = "PerfilAdolescente";
            nuevoUsuario.estado_cuenta = "Activa";
            nuevoUsuario.codigo_vinculacion = null;
            nuevoUsuario.id_tutor = null;
        } else {
            nuevoUsuario.tipo_perfil = "PerfilAdulto";
            nuevoUsuario.estado_cuenta = "Activa";
            nuevoUsuario.codigo_vinculacion = null;
            nuevoUsuario.id_tutor = null;
        }

        // 3. Ejecutamos la base de datos en un Hilo Secundario (Background Thread)
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            db.usuarioDao().insertarUsuario(nuevoUsuario);

            // 4. Volvemos al Hilo Principal (Main Thread) para actualizar la pantalla
            runOnUiThread(() -> {
                Toast.makeText(RegistroActivity.this, "Perfil creado en SQLite (Local)", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistroActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            });
        });
    }
}