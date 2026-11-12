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

import com.aura.aura.R;
import com.aura.aura.database.AppDatabase;
import com.aura.aura.models.Ciclo;
import com.aura.aura.models.CitaMedica;
import com.aura.aura.models.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DashboardActivity extends AppCompatActivity {

    private int miIdUsuario;
    private String miTipoPerfil;

    // Elementos de UI
    private TextView tvBienvenida;
    private Button btnIrEducacion, btnIrRedApoyo, btnIrEmergencia, btnCerrarSesion, btnVincularMenor;
    private EditText etFechaInicioDashboard, etFechaFinDashboard, etFechaCita, etEspecialidad;
    private Button btnGuardarCicloDashboard, btnAgendarCita;
    private View cardAgendaMedica;
    private LinearLayout layoutHistorialCiclos, layoutCitasAgendadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Recuperar datos de sesión desde el Login
        miIdUsuario = getIntent().getIntExtra("ID_USUARIO", -1);
        miTipoPerfil = getIntent().getStringExtra("TIPO_PERFIL");

        vincularVistas();
        configurarDatePickers();
        cargarDatosUsuario();
        configurarPermisosPorRol();

        // Cargar registros iniciales desde SQLite (READ)
        cargarHistorialCiclos();
        cargarHistorialCitas();

        // --- LISTENERS CREATE (INSERT) ---
        btnGuardarCicloDashboard.setOnClickListener(v -> guardarCicloMenstrual());
        btnAgendarCita.setOnClickListener(v -> guardarCitaMedica());

        // --- LISTENERS NAVEGACIÓN ---
        btnIrEducacion.setOnClickListener(v -> {
            Intent intent = new Intent(this, EducacionActivity.class);
            intent.putExtra("TIPO_PERFIL", miTipoPerfil);
            startActivity(intent);
        });
        btnIrRedApoyo.setOnClickListener(v -> startActivity(new Intent(this, RedApoyoActivity.class)));
        btnIrEmergencia.setOnClickListener(v -> startActivity(new Intent(this, EmergenciaActivity.class)));

        btnVincularMenor.setOnClickListener(v -> mostrarDialogoVinculacion());

        btnCerrarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void vincularVistas() {
        tvBienvenida = findViewById(R.id.tvBienvenida);
        btnIrEducacion = findViewById(R.id.btnIrEducacion);
        btnIrRedApoyo = findViewById(R.id.btnIrRedApoyo);
        btnIrEmergencia = findViewById(R.id.btnIrEmergencia);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnVincularMenor = findViewById(R.id.btnVincularMenor); // Solo visible para adultos

        etFechaInicioDashboard = findViewById(R.id.etFechaInicioDashboard);
        etFechaFinDashboard = findViewById(R.id.etFechaFinDashboard);
        btnGuardarCicloDashboard = findViewById(R.id.btnGuardarCicloDashboard);
        layoutHistorialCiclos = findViewById(R.id.layoutHistorialCiclos);

        etFechaCita = findViewById(R.id.etFechaCita);
        etEspecialidad = findViewById(R.id.etEspecialidad);
        btnAgendarCita = findViewById(R.id.btnAgendarCita);
        cardAgendaMedica = findViewById(R.id.cardAgendaMedica);
        layoutCitasAgendadas = findViewById(R.id.layoutCitasAgendadas);
    }

    private void configurarPermisosPorRol() {
        if (miTipoPerfil == null) return;

        // RBAC: Mostrar vinculación solo a adultos
        if (miTipoPerfil.equals("PerfilAdulto")) {
            btnVincularMenor.setVisibility(View.VISIBLE);
        } else {
            btnVincularMenor.setVisibility(View.GONE);
        }

        // RBAC: Ocultar agenda clínica a perfiles infantiles
        if (miTipoPerfil.equals("PerfilInfantil")) {
            cardAgendaMedica.setVisibility(View.GONE);
        } else {
            cardAgendaMedica.setVisibility(View.VISIBLE);
        }
    }

    private void cargarDatosUsuario() {
        if (miIdUsuario == -1) return;
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            Usuario usuaria = db.usuarioDao().obtenerUsuarioPorId(miIdUsuario);
            if (usuaria != null) {
                runOnUiThread(() -> tvBienvenida.setText("Hola, " + usuaria.nombre));
            }
        });
    }

    // =========================================================================
    // MÓDULO CRUD: CICLO MENSTRUAL
    // =========================================================================

    private void guardarCicloMenstrual() {
        String inicio = etFechaInicioDashboard.getText().toString();
        String fin = etFechaFinDashboard.getText().toString();

        if (inicio.isEmpty()) {
            Toast.makeText(this, "Debes ingresar al menos la fecha de inicio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!fin.isEmpty() && calcularDias(inicio, fin) < 0) {
            Toast.makeText(this, "La fecha de término no puede ser anterior al inicio", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Ciclo nuevoCiclo = new Ciclo();
                nuevoCiclo.id_usuario = miIdUsuario;
                nuevoCiclo.fecha_inicio = inicio;
                nuevoCiclo.fecha_fin = fin; // Puede ir vacío ("") si está en curso

                AppDatabase.getDatabase(getApplicationContext()).cicloDao().insertarCiclo(nuevoCiclo);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Ciclo registrado", Toast.LENGTH_SHORT).show();
                    etFechaInicioDashboard.setText("");
                    etFechaFinDashboard.setText("");
                    cargarHistorialCiclos();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void cargarHistorialCiclos() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Ciclo> listaCiclos = AppDatabase.getDatabase(getApplicationContext())
                    .cicloDao().obtenerCiclosPorUsuario(miIdUsuario);

            runOnUiThread(() -> {
                layoutHistorialCiclos.removeAllViews();
                if (listaCiclos.isEmpty()) {
                    TextView tvVacio = new TextView(this);
                    tvVacio.setText("Aún no tienes ciclos registrados.");
                    tvVacio.setTextColor(Color.parseColor("#6B657D"));
                    layoutHistorialCiclos.addView(tvVacio);
                } else {
                    for (Ciclo c : listaCiclos) {
                        LinearLayout fila = new LinearLayout(this);
                        fila.setOrientation(LinearLayout.VERTICAL);
                        fila.setPadding(0, 16, 0, 24);

                        TextView tvCiclo = new TextView(this);
                        tvCiclo.setTextColor(Color.parseColor("#6C5CE7"));
                        tvCiclo.setTextSize(15f);

                        // Lógica: Ciclo en curso vs Ciclo terminado
                        if (c.fecha_fin == null || c.fecha_fin.isEmpty()) {
                            tvCiclo.setText("🩸 Inicio: " + c.fecha_inicio + "\nEstado: En curso");

                            // Creamos un botón dinámico para terminar el ciclo
                            Button btnTerminar = new Button(this);
                            btnTerminar.setText("Confirmar Término");
                            btnTerminar.setBackgroundColor(Color.parseColor("#EFE6FD"));
                            btnTerminar.setTextColor(Color.parseColor("#6C5CE7"));

                            btnTerminar.setOnClickListener(v -> mostrarDialogoActualizarCiclo(c));

                            fila.addView(tvCiclo);
                            fila.addView(btnTerminar);
                        } else {
                            long dias = calcularDias(c.fecha_inicio, c.fecha_fin);
                            tvCiclo.setText("🩸 " + c.fecha_inicio + " al " + c.fecha_fin + "\nDuración: " + dias + " días");
                            fila.addView(tvCiclo);
                        }

                        // Eventos genéricos para UPDATE y DELETE (Mantener presionado)
                        tvCiclo.setOnClickListener(v -> mostrarDialogoActualizarCiclo(c));
                        tvCiclo.setOnLongClickListener(v -> {
                            mostrarDialogoEliminarCiclo(c);
                            return true;
                        });

                        layoutHistorialCiclos.addView(fila);
                    }
                }
            });
        });
    }

    private void mostrarDialogoActualizarCiclo(Ciclo ciclo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modificar Ciclo");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(60, 20, 60, 20);

        EditText etNuevaFechaInicio = new EditText(this);
        etNuevaFechaInicio.setText(ciclo.fecha_inicio);
        etNuevaFechaInicio.setFocusable(false);
        etNuevaFechaInicio.setClickable(true);
        etNuevaFechaInicio.setOnClickListener(v -> mostrarDatePicker(etNuevaFechaInicio, false));
        layout.addView(etNuevaFechaInicio);

        EditText etNuevaFechaFin = new EditText(this);
        etNuevaFechaFin.setText(ciclo.fecha_fin);
        etNuevaFechaFin.setFocusable(false);
        etNuevaFechaFin.setClickable(true);
        etNuevaFechaFin.setOnClickListener(v -> mostrarDatePicker(etNuevaFechaFin, false));
        layout.addView(etNuevaFechaFin);

        builder.setView(layout);
        builder.setPositiveButton("Actualizar", (dialog, which) -> {
            String fInicio = etNuevaFechaInicio.getText().toString();
            String fFin = etNuevaFechaFin.getText().toString();

            if (!fInicio.isEmpty() && !fFin.isEmpty() && calcularDias(fInicio, fFin) >= 0) {
                ciclo.fecha_inicio = fInicio;
                ciclo.fecha_fin = fFin;

                Executors.newSingleThreadExecutor().execute(() -> {
                    AppDatabase.getDatabase(getApplicationContext()).cicloDao().actualizarCiclo(ciclo);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Ciclo actualizado", Toast.LENGTH_SHORT).show();
                        cargarHistorialCiclos();
                    });
                });
            } else {
                Toast.makeText(this, "Fechas inválidas", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEliminarCiclo(Ciclo ciclo) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Registro")
                .setMessage("¿Estás segura de que deseas eliminar este ciclo?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        AppDatabase.getDatabase(getApplicationContext()).cicloDao().eliminarCiclo(ciclo);
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Ciclo eliminado", Toast.LENGTH_SHORT).show();
                            cargarHistorialCiclos();
                        });
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // =========================================================================
    // MÓDULO CRUD: CITAS MÉDICAS
    // =========================================================================

    private void guardarCitaMedica() {
        String fecha = etFechaCita.getText().toString();
        String especialidad = etEspecialidad.getText().toString().trim();

        if (fecha.isEmpty() || especialidad.isEmpty()) {
            Toast.makeText(this, "Ingresa la fecha y la especialidad", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                CitaMedica nuevaCita = new CitaMedica();
                nuevaCita.id_usuario = miIdUsuario;
                nuevaCita.fecha_cita = fecha;
                nuevaCita.especialidad = especialidad;

                AppDatabase.getDatabase(getApplicationContext()).citaMedicaDao().insertarCita(nuevaCita);

                runOnUiThread(() -> {
                    Toast.makeText(this, "¡Cita agendada!", Toast.LENGTH_SHORT).show();
                    etFechaCita.setText("");
                    etEspecialidad.setText("");
                    cargarHistorialCitas();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al guardar cita", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void cargarHistorialCitas() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<CitaMedica> listaCitas = AppDatabase.getDatabase(getApplicationContext())
                    .citaMedicaDao().obtenerCitasPorUsuario(miIdUsuario);

            runOnUiThread(() -> {
                layoutCitasAgendadas.removeAllViews();
                if (listaCitas.isEmpty()) {
                    TextView tvVacio = new TextView(this);
                    tvVacio.setText("No tienes citas próximas agendadas.");
                    tvVacio.setTextColor(Color.parseColor("#6B657D"));
                    layoutCitasAgendadas.addView(tvVacio);
                } else {
                    for (CitaMedica cita : listaCitas) {
                        TextView tvCita = new TextView(this);
                        tvCita.setText("📅 " + cita.fecha_cita + " - " + cita.especialidad);
                        tvCita.setPadding(0, 16, 0, 16);
                        tvCita.setTextColor(Color.parseColor("#E84393"));
                        tvCita.setTextSize(14f);
                        tvCita.setTypeface(null, android.graphics.Typeface.BOLD);

                        // Eventos para UPDATE y DELETE
                        tvCita.setOnClickListener(v -> mostrarDialogoActualizarCita(cita));
                        tvCita.setOnLongClickListener(v -> {
                            mostrarDialogoEliminarCita(cita);
                            return true;
                        });

                        layoutCitasAgendadas.addView(tvCita);
                    }
                }
            });
        });
    }

    private void mostrarDialogoActualizarCita(CitaMedica cita) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reprogramar Cita");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(60, 20, 60, 20);

        EditText etNuevaFecha = new EditText(this);
        etNuevaFecha.setText(cita.fecha_cita);
        etNuevaFecha.setFocusable(false);
        etNuevaFecha.setClickable(true);
        etNuevaFecha.setOnClickListener(v -> mostrarDatePicker(etNuevaFecha, true));
        layout.addView(etNuevaFecha);

        EditText etNuevaEspecialidad = new EditText(this);
        etNuevaEspecialidad.setText(cita.especialidad);
        layout.addView(etNuevaEspecialidad);

        builder.setView(layout);
        builder.setPositiveButton("Actualizar", (dialog, which) -> {
            String fecha = etNuevaFecha.getText().toString();
            String especialidad = etNuevaEspecialidad.getText().toString();

            if (!fecha.isEmpty() && !especialidad.isEmpty()) {
                cita.fecha_cita = fecha;
                cita.especialidad = especialidad;

                Executors.newSingleThreadExecutor().execute(() -> {
                    AppDatabase.getDatabase(getApplicationContext()).citaMedicaDao().actualizarCita(cita);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Cita reprogramada", Toast.LENGTH_SHORT).show();
                        cargarHistorialCitas();
                    });
                });
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEliminarCita(CitaMedica cita) {
        new AlertDialog.Builder(this)
                .setTitle("Cancelar Cita")
                .setMessage("¿Deseas cancelar tu cita para " + cita.especialidad + "?")
                .setPositiveButton("Cancelar cita", (dialog, which) -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        AppDatabase.getDatabase(getApplicationContext()).citaMedicaDao().eliminarCita(cita);
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Cita eliminada", Toast.LENGTH_SHORT).show();
                            cargarHistorialCitas();
                        });
                    });
                })
                .setNegativeButton("Conservar", null)
                .show();
    }

    // =========================================================================
    // UTILIDADES Y CUADROS DE DIÁLOGO
    // =========================================================================

    private void configurarDatePickers() {
        // false = no es cita (bloquea el futuro). true = es cita (bloquea el pasado)
        etFechaInicioDashboard.setOnClickListener(v -> mostrarDatePicker((EditText) v, false));
        etFechaFinDashboard.setOnClickListener(v -> mostrarDatePicker((EditText) v, false));
        etFechaCita.setOnClickListener(v -> mostrarDatePicker((EditText) v, true));
    }

    private void mostrarDatePicker(EditText editText, boolean esParaCita) {
        Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String fechaSeleccionada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            editText.setText(fechaSeleccionada);
        }, anio, mes, dia);

        if (esParaCita) {
            // Citas: No se puede agendar en el pasado (mínimo hoy)
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        } else {
            // Ciclos: No se puede registrar un inicio/fin en el futuro (máximo hoy)
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }

        dialog.show();
    }

    private long calcularDias(String inicioStr, String finStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date inicio = sdf.parse(inicioStr);
            Date fin = sdf.parse(finStr);
            long diff = fin.getTime() - inicio.getTime();
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void mostrarDialogoVinculacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vincular Perfil Infantil");
        builder.setMessage("Ingresa el PIN de 4 dígitos de la menor:");

        final EditText input = new EditText(this);
        input.setPadding(40, 40, 40, 40);
        builder.setView(input);

        builder.setPositiveButton("Vincular", (dialog, which) -> {
            String codigoIngresado = input.getText().toString().trim();
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
                Usuario nina = db.usuarioDao().obtenerUsuarioPorCodigo(codigoIngresado);
                if (nina != null && nina.estado_cuenta.equals("Pendiente_Aprobacion")) {
                    nina.id_tutor = miIdUsuario;
                    nina.estado_cuenta = "Activa";
                    db.usuarioDao().actualizarUsuario(nina);
                    // SOLUCIÓN: Usar DashboardActivity.this en lugar de solo this
                    runOnUiThread(() -> Toast.makeText(DashboardActivity.this, "¡" + nina.nombre + " vinculada!", Toast.LENGTH_LONG).show());
                } else {
                    // SOLUCIÓN: Usar DashboardActivity.this
                    runOnUiThread(() -> Toast.makeText(DashboardActivity.this, "PIN inválido o cuenta ya activa.", Toast.LENGTH_SHORT).show());
                }
            });
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}