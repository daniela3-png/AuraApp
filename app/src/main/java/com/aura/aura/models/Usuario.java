package com.aura.aura.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabla_usuarios")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public int edad;
    public String tipoPerfil;

    // Nuevos campos
    public String contactoEmergencia;
    public String nombreTutor; // Solo si aplica
    public String pinSeguridad; // Para un inicio de sesión básico local

    public Usuario() {
    }
}