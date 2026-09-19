package com.aura.aura.models;
import java.util.Arrays;
import java.util.List;

public class PerfilAdolescente implements PerfilUsuario {
    @Override
    public String getEnfoquePedagogico() {
        return "Decisiones informadas, prevención de riesgos y autonomía.";
    }

    @Override
    public List<String> getTemasHabilitados() {
        return Arrays.asList("Consentimiento Pleno", "Prevención y Planificación", "ITS", "Vínculos y Salud Mental");
    }

    @Override
    public boolean isModuloCitasHabilitado() {
        return true; // Habilitado
    }
}