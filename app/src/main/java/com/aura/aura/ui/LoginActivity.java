package com.aura.aura.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.aura.aura.R;
import com.aura.aura.database.AppDatabase;
import com.aura.aura.models.Usuario;

import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmailLogin, etPinLogin;
    private Button btnIngresar;
    private android.widget.TextView btnIrRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Asegúrate de que estos IDs coincidan con tu activity_login.xml
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPinLogin = findViewById(R.id.etPinLogin);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnIrRegistro = findViewById(R.id.btnIrRegistro);

        btnIngresar.setOnClickListener(v -> validarLogin());

        btnIrRegistro.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
        });
    }

    private void validarLogin() {
        String email = etEmailLogin.getText().toString().trim();
        String pin = etPinLogin.getText().toString().trim();

        if (email.isEmpty() || pin.isEmpty()) {
            Toast.makeText(this, "Ingresa tu correo y PIN", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Buscamos en SQLite usando un Hilo Secundario
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());

            // Usamos la consulta que creamos en el UsuarioDao
            Usuario usuarioDb = db.usuarioDao().obtenerUsuarioPorEmail(email);

            // 2. Volvemos al hilo principal para mostrar mensajes y cambiar de pantalla
            runOnUiThread(() -> {
                if (usuarioDb == null) {
                    Toast.makeText(this, "Usuario no encontrado. Verifica tu correo.", Toast.LENGTH_SHORT).show();
                } else if (!usuarioDb.pin_acceso.equals(pin)) {
                    Toast.makeText(this, "PIN incorrecto", Toast.LENGTH_SHORT).show();
                } else if (usuarioDb.estado_cuenta.equals("Pendiente_Aprobacion")) {
                    // Bloqueo por regla de negocio: La niña no entra hasta que el tutor la apruebe
                    Toast.makeText(this, "Tu cuenta está esperando la autorización de tu tutor.", Toast.LENGTH_LONG).show();
                } else {
                    // Login exitoso
                    Toast.makeText(this, "¡Bienvenida " + usuarioDb.nombre + "!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    // Pasamos el ID del usuario al Dashboard para saber de quién cargar los datos después
                    intent.putExtra("ID_USUARIO", usuarioDb.id_usuario);
                    intent.putExtra("TIPO_PERFIL", usuarioDb.tipo_perfil);

                    startActivity(intent);
                    finish();
                }
            });
        });
    }
}