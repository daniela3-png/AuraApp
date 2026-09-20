package com.aura.aura.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.aura.aura.R;
import com.aura.aura.factory.PerfilFactory;
import com.aura.aura.models.PerfilUsuario;
import com.aura.aura.models.TemaEducativo;

public class EducacionActivity extends AppCompatActivity {

    private TextView tvEnfoque, tvAcompanamiento, tvSoporte, tvCuandoConsultar;
    private LinearLayout layoutTarjetasEducativas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educacion);

        tvEnfoque = findViewById(R.id.tvEnfoque);
        tvAcompanamiento = findViewById(R.id.tvAcompanamiento);
        tvSoporte = findViewById(R.id.tvSoporte);
        tvCuandoConsultar = findViewById(R.id.tvCuandoConsultar);
        layoutTarjetasEducativas = findViewById(R.id.layoutTarjetasEducativas);

        // Recibir la edad desde el Dashboard (por defecto 22 si no llega nada)
        int edad = getIntent().getIntExtra("EDAD_USUARIA", 22);

        // Crear el perfil dinámico según la edad
        PerfilUsuario perfil = PerfilFactory.crearPerfil(edad);

        // Poblar la interfaz con los datos seguros de la clase
        tvEnfoque.setText("Perfil Activo: " + perfil.getNombrePerfil() + "\n" + perfil.getEnfoque());
        tvAcompanamiento.setText(perfil.getAcompanamiento());
        tvSoporte.setText(perfil.getSoporte());
        tvCuandoConsultar.setText(perfil.getCuandoConsultar());

        // Generar las tarjetas del menú educativo dinámicamente
        for (TemaEducativo tema : perfil.getMenu()) {
            crearTarjetaEducativa(tema.titulo, tema.detalle);
        }
    }

    private void crearTarjetaEducativa(String titulo, String detalle) {
        // Creamos la tarjeta (CardView)
        CardView card = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, 24);
        card.setLayoutParams(cardParams);
        card.setRadius(16f);
        card.setCardElevation(4f);
        card.setCardBackgroundColor(Color.WHITE);

        // Contenedor interno
        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        innerLayout.setPadding(32, 32, 32, 32);

        // Título
        TextView tvTitulo = new TextView(this);
        tvTitulo.setText(titulo);
        tvTitulo.setTextSize(16f);
        tvTitulo.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTitulo.setTextColor(Color.parseColor("#5E35B1"));
        tvTitulo.setPadding(0, 0, 0, 8);

        // Detalle
        TextView tvDetalle = new TextView(this);
        tvDetalle.setText(detalle);
        tvDetalle.setTextSize(14f);
        tvDetalle.setTextColor(Color.parseColor("#757575"));

        innerLayout.addView(tvTitulo);
        innerLayout.addView(tvDetalle);
        card.addView(innerLayout);

        // Añadir la tarjeta completa al layout principal
        layoutTarjetasEducativas.addView(card);
    }
}