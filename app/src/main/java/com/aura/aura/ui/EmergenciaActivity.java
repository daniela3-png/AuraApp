package com.aura.aura.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aura.aura.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class EmergenciaActivity extends AppCompatActivity {

    private Button btnPanico;
    private static final int CODIGO_PERMISOS = 100;

    // Cliente para obtener la ubicación de Google
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencia);

        btnPanico = findViewById(R.id.btnPanico);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnPanico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarPermisosYEjecutarAlerta();
            }
        });
    }

    private void verificarPermisosYEjecutarAlerta() {
        boolean permisoSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        boolean permisoUbicacion = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (permisoSMS && permisoUbicacion) {
            // Ya tenemos permisos, procedemos a enviar el SOS
            enviarSOS();
        } else {
            // Solicitamos los permisos
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION},
                    CODIGO_PERMISOS);
        }
    }

    @SuppressLint("MissingPermission") // Suprimimos la advertencia porque ya verificamos los permisos arriba
    private void enviarSOS() {
        Toast.makeText(this, "Obteniendo ubicación y enviando SOS...", Toast.LENGTH_SHORT).show();

        // Obtenemos la última ubicación conocida (es la forma más rápida)
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            String mensaje = "¡AYUDA! Botón de pánico Aura activado. ";

            if (location != null) {
                // Creamos un link de Google Maps con las coordenadas
                String linkMaps = "https://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();
                mensaje += "Mi ubicación es: " + linkMaps;
            } else {
                mensaje += "No se pudo obtener mi ubicación GPS exacta en este momento.";
            }

            // TODO: En el futuro esto debe leerse de los contactos configurados en la base de datos (Room)
            String numeroEmergencia = "+56912345678"; // Reemplaza con un número de prueba real para probar

            try {
                // Enviar el SMS nativo
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(numeroEmergencia, null, mensaje, null, null);

                Toast.makeText(this, "¡Alerta enviada correctamente!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error al enviar el SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODIGO_PERMISOS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si el usuario acepta al salir el cuadro de diálogo, disparamos la alerta de inmediato
                enviarSOS();
            } else {
                Toast.makeText(this, "Aura requiere permisos de GPS y SMS para poder protegerte.", Toast.LENGTH_LONG).show();
            }
        }
    }
}