package com.aura.aura.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.aura.aura.R;

public class EducacionActivity extends AppCompatActivity {

    // Declaración de variables a nivel de clase
    private String tipoPerfil;
    private TextView tvEnfoque, tvAcompanamiento, tvSoporte, tvCuandoConsultar;
    private LinearLayout layoutTarjetasEducativas;
    private CardView cardAcompanamiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educacion);

        // Recuperar el tipo de perfil enviado desde el Dashboard
        tipoPerfil = getIntent().getStringExtra("TIPO_PERFIL");
        if (tipoPerfil == null) {
            tipoPerfil = "PerfilAdulto"; // Valor por defecto por seguridad
        }

        vincularVistas();
        cargarContenidoEducativoSegunPerfil();
    }

    private void vincularVistas() {
        tvEnfoque = findViewById(R.id.tvEnfoque);
        tvAcompanamiento = findViewById(R.id.tvAcompanamiento);
        tvSoporte = findViewById(R.id.tvSoporte);
        tvCuandoConsultar = findViewById(R.id.tvCuandoConsultar);
        layoutTarjetasEducativas = findViewById(R.id.layoutTarjetasEducativas);

        // Buscamos la tarjeta de acompañamiento para ocultarla si es perfil infantil
        if (tvAcompanamiento != null && tvAcompanamiento.getParent() instanceof CardView) {
            cardAcompanamiento = (CardView) tvAcompanamiento.getParent();
        }
    }

    private void cargarContenidoEducativoSegunPerfil() {
        if (layoutTarjetasEducativas != null) {
            layoutTarjetasEducativas.removeAllViews();
        }

        if (tipoPerfil.equals("PerfilInfantil")) {
            // --- CONTENIDO PARA NIÑAS ---
            if (tvEnfoque != null) tvEnfoque.setText("Enfoque: Cuidado personal, autoconocimiento, higiene y confianza con personas adultas responsables.");

            if (cardAcompanamiento != null) {
                cardAcompanamiento.setVisibility(View.GONE);
            }

            if (tvSoporte != null) tvSoporte.setText("Red de apoyo escolar y familiar en Iquique (Padres, tutores y profesores jefes de confianza).");
            if (tvCuandoConsultar != null) tvCuandoConsultar.setText("Cuando sientas confusión sobre los cambios en tu cuerpo, necesites conversar sobre tus emociones o experimentes situaciones que te hagan sentir incómoda.");

            agregarModulo("Cuidando mi Cuerpo y Aseo Personal", "Aprende los hábitos diarios de higiene, la importancia de cuidar tu espacio personal y por qué tu cuerpo es tuyo y debes respetarlo.");
            agregarModulo("Mis Primeras Emociones", "Una guía amigable para identificar qué sientes, cómo comunicarlo de forma asertiva y a quién acudir en tu hogar o colegio cuando necesites ayuda.");

        } else if (tipoPerfil.equals("PerfilAdolescente")) {
            // --- CONTENIDO PARA ADOLESCENTES ---
            if (tvEnfoque != null) tvEnfoque.setText("Enfoque: Salud menstrual, autogestión del ciclo, cambios corporales y autonomía informada.");

            if (cardAcompanamiento != null) {
                cardAcompanamiento.setVisibility(View.VISIBLE);
            }
            if (tvAcompanamiento != null) tvAcompanamiento.setText("Acompañamiento preventivo: Recuerda que puedes conversar de forma confidencial con matronas y profesionales en tu CESFAM más cercano en la Región de Tarapacá.");

            if (tvSoporte != null) tvSoporte.setText("Centros de Salud Familiar (CESFAM) de Iquique y Alto Hospicio, y departamentos de orientación estudiantil.");
            if (tvCuandoConsultar != null) tvCuandoConsultar.setText("Ante dolores menstruales intensos (dismenorrea), irregularidades prolongadas en tus ciclos o dudas sobre salud reproductiva y autocuidado.");

            agregarModulo("Comprendiendo mi Ciclo Menstrual", "Conoce las fases de tu ciclo, qué es la menstruación, cómo registrar tus fechas de inicio y fin, y qué materiales de gestión menstrual existen.");
            agregarModulo("Derechos, Límites y Autonomía", "Información clara sobre el respeto mutuo, la prevención, el consentimiento y el derecho a decidir sobre tu propio bienestar físico y emocional.");

        } else {
            // --- CONTENIDO PARA ADULTOS ---
            if (tvEnfoque != null) tvEnfoque.setText("Enfoque: Salud preventiva integral, planificación familiar, acompañamiento a menores y gestión hormonal.");

            if (cardAcompanamiento != null) {
                cardAcompanamiento.setVisibility(View.VISIBLE);
            }
            if (tvAcompanamiento != null) tvAcompanamiento.setText("Acompañamiento a Menores: Tienes el rol clave de guiar a niñas y adolescentes con empatía, facilitando un entorno de confianza y el acceso a la red de salud local.");

            if (tvSoporte != null) tvSoporte.setText("Red de Salud Primaria (CESFAM Cirujano Guzmán, CESFAM Videla, CESFAM Aguirre) y programas de salud de la mujer en Tarapacá.");
            if (tvCuandoConsultar != null) tvCuandoConsultar.setText("Para controles ginecológicos preventivos anuales (PAP, mamografías), asesoría en métodos anticonceptivos, climaterio o derivación especializada en la red pública.");

            agregarModulo("Planificación y Salud Preventiva", "Guía clínica sobre métodos anticonceptivos disponibles en el sistema público, GES y controles preventivos obligatorios.");
            agregarModulo("Acompañamiento Educativo a Menores", "Herramientas de comunicación asertiva para orientar a perfiles infantiles y adolescentes en su desarrollo socioemocional y físico.");
            agregarModulo("Bienestar Hormonal y Autocuidado", "Monitoreo avanzado del ciclo, gestión del estrés, salud metabólica y recursos de orientación territorial en Iquique y Alto Hospicio.");
        }
    }

    private void agregarModulo(String titulo, String descripcion) {
        CardView card = new CardView(this);
        card.setRadius(12f);
        card.setCardElevation(2f);
        card.setUseCompatPadding(true);
        card.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(20, 20, 20, 20);

        TextView tvTitulo = new TextView(this);
        tvTitulo.setText(titulo);
        tvTitulo.setTextSize(16f);
        tvTitulo.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTitulo.setTextColor(Color.parseColor("#6C5CE7"));
        tvTitulo.setPadding(0, 0, 0, 6);

        TextView tvDesc = new TextView(this);
        tvDesc.setText(descripcion);
        tvDesc.setTextSize(13f);
        tvDesc.setTextColor(Color.parseColor("#6B657D"));

        linearLayout.addView(tvTitulo);
        linearLayout.addView(tvDesc);
        card.addView(linearLayout);

        if (layoutTarjetasEducativas != null) {
            layoutTarjetasEducativas.addView(card);
        }
    }
}