package com.aura.aura.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.aura.aura.R;

public class EmergenciaActivity extends AppCompatActivity {

    private Button btnPanico;
    private static final int CODIGO_PERMISOS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencia);

        btnPanico = findViewById(R.id.btnPanico);

        btnPanico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarPermisosYEjecutarAlerta();
            }
        });
    }

    private void verificarPermisosYEjecutarAlerta() {
        // Verificamos si tenemos los permisos de SMS y Ubicación
        boolean permisoSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        boolean permisoUbicacion = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (permisoSMS && permisoUbicacion) {
            // Tenemos permisos, ejecutamos la lógica del SOS (Paso 8)
            Toast.makeText(this, "Permisos concedidos. Preparando envío de SOS...", Toast.LENGTH_SHORT).show();
        } else {
            // No tenemos permisos, se los pedimos a la usuaria
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION},
                    CODIGO_PERMISOS);
        }
    }

    // Este método escucha la respuesta del usuario cuando le sale el cuadro de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CODIGO_PERMISOS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permisos aceptados. Presiona SOS de nuevo.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Aura requiere estos permisos para poder protegerte.", Toast.LENGTH_LONG).show();
            }
        }
    }
}