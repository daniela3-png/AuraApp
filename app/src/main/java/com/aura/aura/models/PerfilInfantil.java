package com.aura.aura.models;
import java.util.ArrayList;
import java.util.List;

public class PerfilInfantil implements PerfilUsuario {
    @Override
    public String getNombrePerfil() { return "Infantil (10 a 13 años)"; }

    @Override
    public String getEnfoque() {
        return "Integral: Enfocado en el conocimiento del cuerpo, límites, emociones y vínculos seguros. Gradual según desarrollo.";
    }

    @Override
    public List<TemaEducativo> getMenu() {
        List<TemaEducativo> menu = new ArrayList<>();
        menu.add(new TemaEducativo("Pubertad", "Menstruación, cambios corporales, higiene biológica y emociones."));
        menu.add(new TemaEducativo("Consentimiento Básico", "Nadie debe tocar tu cuerpo sin permiso; aprender a decir 'no' y pedir ayuda al adulto de confianza."));
        menu.add(new TemaEducativo("Internet y Autocuidado", "Privacidad digital, imágenes íntimas y cómo reaccionar ante situaciones incómodas."));
        menu.add(new TemaEducativo("Buen Trato", "Distinguir afecto, presión, manipulación y violencia."));
        return menu;
    }

    @Override
    public String getAcompanamiento() {
        return "Ruta para Cuidadores: Escuchar sin burlas, responder con lenguaje simple y sin tabúes, no culpabilizar.";
    }

    @Override
    public String getSoporte() {
        return "Espacios Amigables Tarapacá: Atención confidencial, gratuita y cercana para el cuidado de tu desarrollo (10 a 13 años).";
    }

    @Override
    public String getCuandoConsultar() {
        return "Dudas sobre tu primera menstruación (menarquia), dolores intensos en tu periodo, cambios corporales que te asusten, o si alguien te hace sentir incómoda, presionada o asustada en internet o en persona.";
    }
}