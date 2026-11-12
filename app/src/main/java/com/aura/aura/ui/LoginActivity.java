package com.aura.aura.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.aura.aura.R;
import com.aura.aura.database.AppDatabase;
import com.aura.aura.models.Usuario;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    // 1. Cambiamos a los campos reales de tu diseño
    private EditText etEmail, etPin;
    private Button btnIngresar;
    private TextView tvIrRegistro;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    // Variable para guardar temporalmente al usuario tras consultar SQLite
    private Usuario usuarioAutenticado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Asegúrate de que estos IDs coincidan con tu activity_login.xml
        etEmail = findViewById(R.id.etCorreoLogin); // Revisa si tu ID es etEmail o etCorreoLogin
        etPin = findViewById(R.id.etPinLogin);      // Revisa si tu ID es etPin o etPinLogin
        btnIngresar = findViewById(R.id.btnIngresar);
        tvIrRegistro = findViewById(R.id.tvIrRegistro);

        executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Error de autenticación: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Toast.makeText(getApplicationContext(), "¡Bienvenida, " + usuarioAutenticado.nombre + "!", Toast.LENGTH_SHORT).show();

                // 2. Pasamos el ID real de SQLite para que el Dashboard no falle
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                intent.putExtra("ID_USUARIO", usuarioAutenticado.id_usuario);
                intent.putExtra("TIPO_PERFIL", usuarioAutenticado.tipo_perfil);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Huella no reconocida. Intenta de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación Segura de Aura")
                .setSubtitle("Usa tu huella dactilar para confirmar tu identidad")
                .setNegativeButtonText("Cancelar")
                .build();

        btnIngresar.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pin = etPin.getText().toString().trim();

            // --- VALIDACIONES DE LA RÚBRICA ---

            if (email.isEmpty() || pin.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(LoginActivity.this, "Ingresa un correo válido (ej: nombre@correo.com)", Toast.LENGTH_SHORT).show();
                return;
            }

            if (pin.length() < 4) {
                Toast.makeText(LoginActivity.this, "El PIN debe tener exactamente 4 dígitos", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- INTEGRACIÓN CON SQLITE (READ) ---

// --- INTEGRACIÓN CON SQLITE (READ) ---

            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

                // Buscamos a la usuaria en la base de datos por su correo y PIN
                Usuario usuarioDb = db.usuarioDao().loginUsuario(email, pin);

                runOnUiThread(() -> {
                    if (usuarioDb != null) {

                        // --- NUEVA VALIDACIÓN DE SEGURIDAD (RBAC / ESTADO) ---
                        if ("PerfilInfantil".equals(usuarioDb.tipo_perfil) && "Pendiente_Aprobacion".equals(usuarioDb.estado_cuenta)) {
                            Toast.makeText(LoginActivity.this, "Cuenta pendiente de aprobación por un adulto tutor.", Toast.LENGTH_LONG).show();
                            return; // Detiene el inicio de sesión y no pide huella
                        }
                        // Si las credenciales son correctas y está aprobada, guardamos la sesión y pedimos huella
                        usuarioAutenticado = usuarioDb;
                        biometricPrompt.authenticate(promptInfo);
                    } else {
                        Toast.makeText(LoginActivity.this, "Correo o PIN incorrectos", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        tvIrRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }
}