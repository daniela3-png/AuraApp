package com.aura.aura.models;
import java.util.Arrays;
import java.util.List;

public class PerfilAdulto implements PerfilUsuario {
    @Override
    public String getEnfoquePedagogico() {
        return "Autonomía reproductiva integral, bienestar a largo plazo.";
    }

    @Override
    public List<String> getTemasHabilitados() {
        return Arrays.asList("Planificación Avanzada", "Controles Clínicos Clave", "Relaciones Adultas", "Derechos Sexuales");
    }

    @Override
    public boolean isModuloCitasHabilitado() {
        return true; // Habilitado
    }
}