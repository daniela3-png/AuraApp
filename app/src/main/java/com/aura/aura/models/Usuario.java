package com.aura.aura.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabla_usuarios")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public int id_usuario;

    public String nombre;
    public String email;        // Requisito indispensable para el Login
    public int edad;
    public String tipo_perfil;
    public String pin;          // PIN de 4 dígitos

    // Campos para la Red de Apoyo / Vincular Niña
    public String nombreTutor;
    public int id_tutor;
    public String estado_cuenta; // "Activa" o "Pendiente_Aprobacion"

    public Usuario() {
    }
}