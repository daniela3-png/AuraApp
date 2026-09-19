package com.aura.aura.models;
import java.util.Arrays;
import java.util.List;

public class PerfilInfantil implements PerfilUsuario {
    @Override
    public String getEnfoquePedagogico() {
        return "Conocimiento del cuerpo, límites personales y pubertad.";
    }

    @Override
    public List<String> getTemasHabilitados() {
        return Arrays.asList("Pubertad", "Consentimiento Básico", "Autocuidado en Internet");
    }

    @Override
    public boolean isModuloCitasHabilitado() {
        return false; // Oculto para este perfil
    }
}