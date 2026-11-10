package com.aura.aura.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.aura.aura.R;
import com.aura.aura.database.AppDatabase;
import com.aura.aura.models.Ciclo;
import com.aura.aura.models.CitaMedica;
import com.aura.aura.models.Usuario;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class DashboardActivity extends AppCompatActivity {

    private int miIdUsuario;
    private String miTipoPerfil;

    // Elementos Generales
    private TextView tvBienvenida;
    private Button btnCerrarSesion, btnVincularMenor;
    private Button btnIrEmergencia, btnIrEducacion, btnIrRedApoyo;
    private CardView cardAgendaMedica;

    // Elementos del Ciclo Menstrual
    private EditText etFechaInicioDashboard, etFechaFinDashboard;
    private Button btnGuardarCicloDashboard;
    private LinearLayout layoutHistorialCiclos;

    // Elementos de la Cita Médica (NUEVO)
    private EditText etFechaCita, etEspecialidad;
    private Button btnAgendarCita;
    private LinearLayout layoutCitasAgendadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        miIdUsuario = getIntent().getIntExtra("ID_USUARIO", -1);
        miTipoPerfil = getIntent().getStringExtra("TIPO_PERFIL");

        // Enlazar Generales
        tvBienvenida = findViewById(R.id.tvBienvenida);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnVincularMenor = findViewById(R.id.btnVincularMenor);
        btnIrEmergencia = findViewById(R.id.btnIrEmergencia);
        btnIrEducacion = findViewById(R.id.btnIrEducacion);
        btnIrRedApoyo = findViewById(R.id.btnIrRedApoyo);
        cardAgendaMedica = findViewById(R.id.cardAgendaMedica);

        // Enlazar Ciclo
        etFechaInicioDashboard = findViewById(R.id.etFechaInicioDashboard);
        etFechaFinDashboard = findViewById(R.id.etFechaFinDashboard);
        btnGuardarCicloDashboard = findViewById(R.id.btnGuardarCicloDashboard);
        layoutHistorialCiclos = findViewById(R.id.layoutHistorialCiclos);

        // Enlazar Cita Médica (NUEVO)
        etFechaCita = findViewById(R.id.etFechaCita);
        etEspecialidad = findViewById(R.id.etEspecialidad);
        btnAgendarCita = findViewById(R.id.btnAgendarCita);
        layoutCitasAgendadas = findViewById(R.id.layoutCitasAgendadas);

        cargarDatosUsuario();
        configurarPermisosPorRol();
        cargarHistorialCiclos();
        cargarHistorialCitas(); // NUEVO: Cargamos las citas al inicio

        // Listeners Generales
        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());
        btnVincularMenor.setOnClickListener(v -> mostrarDialogoVinculacion());

        // Listeners Ciclo
        etFechaInicioDashboard.setOnClickListener(v -> mostrarCalendario(etFechaInicioDashboard));
        etFechaFinDashboard.setOnClickListener(v -> mostrarCalendario(etFechaFinDashboard));
        btnGuardarCicloDashboard.setOnClickListener(v -> guardarCicloMenstrual());

        // Listeners Cita Médica (NUEVO)
        etFechaCita.setOnClickListener(v -> mostrarCalendario(etFechaCita)); // ¡Aquí conectamos el calendario!
        btnAgendarCita.setOnClickListener(v -> guardarCitaMedica());

        // Navegación
        btnIrEmergencia.setOnClickListener(v -> startActivity(new Intent(this, EmergenciaActivity.class)));
        btnIrEducacion.setOnClickListener(v -> startActivity(new Intent(this, EducacionActivity.class)));
        btnIrRedApoyo.setOnClickListener(v -> startActivity(new Intent(this, RedApoyoActivity.class)));
    }

    // --- MÉTODOS DE CITA MÉDICA (NUEVOS) ---

    private void guardarCitaMedica() {
        String fecha = etFechaCita.getText().toString();
        String especialidad = etEspecialidad.getText().toString().trim();

        if (fecha.isEmpty() || especialidad.isEmpty()) {
            Toast.makeText(this, "Ingresa la fecha y la especialidad", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            CitaMedica nuevaCita = new CitaMedica();
            nuevaCita.id_usuario = miIdUsuario;
            nuevaCita.fecha_cita = fecha;
            nuevaCita.especialidad = especialidad;

            db.citaMedicaDao().insertarCita(nuevaCita);

            runOnUiThread(() -> {
                Toast.makeText(DashboardActivity.this, "¡Cita agendada exitosamente!", Toast.LENGTH_SHORT).show();
                etFechaCita.setText("");
                etEspecialidad.setText("");
                cargarHistorialCitas(); // Actualizamos la lista visual
            });
        });
    }

    private void cargarHistorialCitas() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<CitaMedica> listaCitas = db.citaMedicaDao().obtenerCitasPorUsuario(miIdUsuario);

            runOnUiThread(() -> {
                layoutCitasAgendadas.removeAllViews();

                if (listaCitas.isEmpty()) {
                    TextView tvVacio = new TextView(DashboardActivity.this);
                    tvVacio.setText("No tienes citas próximas agendadas.");
                    tvVacio.setTextColor(Color.GRAY);
                    layoutCitasAgendadas.addView(tvVacio);
                } else {
                    for (CitaMedica cita : listaCitas) {
                        TextView tvCita = new TextView(DashboardActivity.this);
                        tvCita.setText("📅 " + cita.fecha_cita + " - " + cita.especialidad);
                        tvCita.setPadding(0, 12, 0, 12);
                        tvCita.setTextColor(Color.parseColor("#D32F2F")); // Rojo oscuro para que combine
                        tvCita.setTextSize(14f);
                        tvCita.setTypeface(null, android.graphics.Typeface.BOLD);                        layoutCitasAgendadas.addView(tvCita);
                    }
                }
            });
        });
    }

    // --- MÉTODOS DE CICLO Y USUARIO (EXISTENTES) ---

    private void cargarDatosUsuario() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            Usuario usuaria = db.usuarioDao().obtenerUsuarioPorId(miIdUsuario);
            if (usuaria != null) {
                runOnUiThread(() -> tvBienvenida.setText("Hola, " + usuaria.nombre));
            }
        });
    }

    private void cargarHistorialCiclos() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<Ciclo> listaCiclos = db.cicloDao().obtenerCiclosPorUsuario(miIdUsuario);
            runOnUiThread(() -> {
                layoutHistorialCiclos.removeAllViews();
                if (listaCiclos.isEmpty()) {
                    TextView tvVacio = new TextView(DashboardActivity.this);
                    tvVacio.setText("Aún no tienes ciclos registrados.");
                    tvVacio.setTextColor(Color.GRAY);
                    layoutHistorialCiclos.addView(tvVacio);
                } else {
                    for (Ciclo c : listaCiclos) {
                        TextView tvCiclo = new TextView(DashboardActivity.this);
                        tvCiclo.setText("• Inicio: " + c.fecha_inicio + " | Fin: " + c.fecha_fin);
                        tvCiclo.setPadding(0, 12, 0, 12);
                        tvCiclo.setTextColor(Color.DKGRAY);
                        tvCiclo.setTextSize(14f);
                        layoutHistorialCiclos.addView(tvCiclo);
                    }
                }
            });
        });
    }

    private void guardarCicloMenstrual() {
        String fechaInicio = etFechaInicioDashboard.getText().toString();
        String fechaFin = etFechaFinDashboard.getText().toString();

        if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
            Toast.makeText(this, "Selecciona la fecha de inicio y término", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                Ciclo nuevoCiclo = new Ciclo();
                nuevoCiclo.id_usuario = miIdUsuario;
                nuevoCiclo.fecha_inicio = fechaInicio;
                nuevoCiclo.fecha_fin = fechaFin;

                db.cicloDao().insertarCiclo(nuevoCiclo);

                // Forzamos la actualización visual en el hilo principal
                runOnUiThread(() -> {
                    etFechaInicioDashboard.setText("");
                    etFechaFinDashboard.setText("");
                    Toast.makeText(DashboardActivity.this, "¡Ciclo guardado exitosamente!", Toast.LENGTH_SHORT).show();
                    cargarHistorialCiclos(); // Refresca la lista al instante
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(DashboardActivity.this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    private void configurarPermisosPorRol() {
        if (miTipoPerfil == null) return;
        if (miTipoPerfil.equals("PerfilAdulto")) {
            btnVincularMenor.setVisibility(View.VISIBLE);
        } else {
            btnVincularMenor.setVisibility(View.GONE);
        }
        if (miTipoPerfil.equals("PerfilInfantil")) {
            cardAgendaMedica.setVisibility(View.GONE);
        } else {
            cardAgendaMedica.setVisibility(View.VISIBLE);
        }
    }

    private void mostrarCalendario(EditText editTextSeleccionado) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, yearSeleccionado, monthOfYear, dayOfMonth) -> {
                    String fecha = yearSeleccionado + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    editTextSeleccionado.setText(fecha);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void mostrarDialogoVinculacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vincular Perfil Infantil");
        builder.setMessage("Ingresa el código AURA de 4 dígitos (Ej: AURA-3B90)");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Vincular", (dialog, which) -> {
            String codigoIngresado = input.getText().toString().trim();
            procesarVinculacion(codigoIngresado);
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void procesarVinculacion(String codigo) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            Usuario nina = db.usuarioDao().obtenerUsuarioPorCodigo(codigo);
            if (nina != null && nina.estado_cuenta.equals("Pendiente_Aprobacion")) {
                nina.id_tutor = miIdUsuario;
                nina.estado_cuenta = "Activa";
                db.usuarioDao().actualizarUsuario(nina);
                runOnUiThread(() -> Toast.makeText(DashboardActivity.this, "¡" + nina.nombre + " vinculada exitosamente!", Toast.LENGTH_LONG).show());
            } else {
                runOnUiThread(() -> Toast.makeText(DashboardActivity.this, "Código inválido o cuenta ya activa.", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void cerrarSesion() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}