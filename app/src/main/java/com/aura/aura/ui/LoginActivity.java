package com.aura.aura.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import java.util.concurrent.Executor;
import com.aura.aura.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etNombreLogin;
    private Button btnIngresar;
    private TextView tvIrRegistro;

    // Variables para la biometría
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etNombreLogin = findViewById(R.id.etNombreLogin);
        btnIngresar = findViewById(R.id.btnIngresar);
        tvIrRegistro = findViewById(R.id.tvIrRegistro);

        // 1. Configurar el "Ejecutor" (el hilo donde correrá la verificación)
        executor = ContextCompat.getMainExecutor(this);

        // 2. Configurar el comportamiento del panel biométrico
        biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Error de autenticación: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Si la huella es correcta, ingresamos al Dashboard
                String nombre = etNombreLogin.getText().toString().trim();
                Toast.makeText(getApplicationContext(),
                        "¡Bienvenida de nuevo, " + nombre + "!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                intent.putExtra("NOMBRE_USUARIA", nombre);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Huella no reconocida. Intenta de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });

        // 3. Configurar los textos que verá la usuaria en el panel
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación Segura de Aura")
                .setSubtitle("Usa tu huella dactilar o rostro para ingresar")
                .setNegativeButtonText("Cancelar")
                .build();

        // Lógica del botón Ingresar
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombreLogin.getText().toString().trim();

                if (!nombre.isEmpty()) {
                    // Si ingresó su nombre, ¡Lanzamos el lector de huellas!
                    biometricPrompt.authenticate(promptInfo);
                } else {
                    Toast.makeText(LoginActivity.this, "Por favor, ingresa tu nombre de usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Lógica para ir a Registro
        tvIrRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });
    }
}