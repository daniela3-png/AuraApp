package com.aura.aura.models;
import java.util.List;

public interface PerfilUsuario {
    String getEnfoquePedagogico();
    List<String> getTemasHabilitados();
    boolean isModuloCitasHabilitado();
}