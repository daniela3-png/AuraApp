package com.aura.aura.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.aura.aura.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etNombreLogin;
    private Button btnIngresar;
    private TextView tvIrRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etNombreLogin = findViewById(R.id.etNombreLogin);
        btnIngresar = findViewById(R.id.btnIngresar);
        tvIrRegistro = findViewById(R.id.tvIrRegistro);

        // Lógica para ingresar al sistema
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombreLogin.getText().toString().trim();

                if (!nombre.isEmpty()) {
                    // TODO: Aquí validaremos el nombre contra la base de datos (y luego agregaremos Biometría)
                    Toast.makeText(LoginActivity.this, "Bienvenida de nuevo, " + nombre, Toast.LENGTH_SHORT).show();

                    // Ingresamos al Dashboard
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Por favor, ingresa tu nombre de usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Lógica para ir a la pantalla de creación de perfil si es nueva
        tvIrRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });
    }
}