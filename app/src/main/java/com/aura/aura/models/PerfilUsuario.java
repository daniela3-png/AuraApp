package com.aura.aura.models;
import java.util.List;

public interface PerfilUsuario {
    String getNombrePerfil();
    String getEnfoque();
    List<TemaEducativo> getMenu();
    String getAcompanamiento();
    String getSoporte();
    String getCuandoConsultar();
}