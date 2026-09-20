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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.aura.aura.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DashboardActivity extends AppCompatActivity {

    private Button btnIrEducacion, btnIrRedApoyo, btnIrEmergencia, btnCerrarSesion;
    private EditText etFechaInicioDashboard, etFechaFinDashboard, etFechaCita, etEspecialidad;
    private Button btnGuardarCicloDashboard, btnAgendarCita;
    private CardView cardAgendaMedica;
    private TextView tvBienvenida;
    private LinearLayout layoutHistorialCiclos, layoutCitasAgendadas;

    private int edadUsuaria = 22; // Por defecto
    private int contadorCiclos = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        vincularVistas();
        configurarPerfilHardcodeado();
        configurarDatePickers();

        // Lógica para guardar Ciclo y calcular días
        btnGuardarCicloDashboard.setOnClickListener(v -> {
            String inicio = etFechaInicioDashboard.getText().toString();
            String fin = etFechaFinDashboard.getText().toString();

            if(!inicio.isEmpty() && !fin.isEmpty()) {
                long dias = calcularDias(inicio, fin);
                if (dias >= 0) {
                    agregarRegistroCicloUi("Ciclo #" + contadorCiclos, inicio + " al " + fin, dias + " días");
                    contadorCiclos++;
                    etFechaInicioDashboard.setText("");
                    etFechaFinDashboard.setText("");
                } else {
                    Toast.makeText(this, "La fecha de término debe ser mayor a la de inicio", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Completa ambas fechas", Toast.LENGTH_SHORT).show();
            }
        });

        // Lógica para agendar cita
        btnAgendarCita.setOnClickListener(v -> {
            String fecha = etFechaCita.getText().toString();
            String especialidad = etEspecialidad.getText().toString();

            if(!fecha.isEmpty() && !especialidad.isEmpty()) {
                agregarRegistroCitaUi(fecha, especialidad);
                etFechaCita.setText("");
                etEspecialidad.setText("");
            } else {
                Toast.makeText(this, "Completa fecha y especialidad", Toast.LENGTH_SHORT).show();
            }
        });

        // Navegación
        btnIrEducacion.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, EducacionActivity.class);
            intent.putExtra("EDAD_USUARIA", edadUsuaria); // Pasamos la edad para filtrar contenido educativo
            startActivity(intent);
        });

        btnIrRedApoyo.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, RedApoyoActivity.class)));
        btnIrEmergencia.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, EmergenciaActivity.class)));
        btnCerrarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
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

    private void configurarPerfilHardcodeado() {
        String nombre = getIntent().getStringExtra("NOMBRE_USUARIA");
        if (nombre != null) {
            tvBienvenida.setText("Hola, " + nombre);
            nombre = nombre.toLowerCase().trim();

            if (nombre.equals("selena")) {
                edadUsuaria = 12; // Perfil Infantil
            } else if (nombre.equals("aurora")) {
                edadUsuaria = 16; // Perfil Adolescente
            } else if (nombre.equals("ema")) {
                edadUsuaria = 22; // Perfil Adulto
            }
        }

        // RB-01: Ocultar agenda clínica para menores de 14
        if (edadUsuaria < 14) {
            cardAgendaMedica.setVisibility(View.GONE);
        } else {
            cardAgendaMedica.setVisibility(View.VISIBLE);
        }
    }

    private void configurarDatePickers() {
        View.OnClickListener dateClickListener = v -> mostrarDatePicker((EditText) v);
        etFechaInicioDashboard.setOnClickListener(dateClickListener);
        etFechaFinDashboard.setOnClickListener(dateClickListener);
        etFechaCita.setOnClickListener(dateClickListener);
    }

    private void mostrarDatePicker(EditText editText) {
        Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String fechaSeleccionada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            editText.setText(fechaSeleccionada);
        }, anio, mes, dia);
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

    private void agregarRegistroCicloUi(String titulo, String fechas, String duracion) {
        TextView tv = new TextView(this);
        tv.setText(titulo + "  |  " + fechas + "  |  " + duracion);
        tv.setPadding(0, 16, 0, 16);
        tv.setTextColor(Color.parseColor("#4527A0")); // Morado oscuro
        layoutHistorialCiclos.addView(tv, 0); // Lo añade al inicio de la lista
    }

    private void agregarRegistroCitaUi(String fecha, String especialidad) {
        TextView tv = new TextView(this);
        tv.setText(fecha + "  |  " + especialidad);
        tv.setPadding(0, 16, 0, 16);
        tv.setTextColor(Color.parseColor("#D32F2F")); // Rojo
        layoutCitasAgendadas.addView(tv, 0);
    }
}