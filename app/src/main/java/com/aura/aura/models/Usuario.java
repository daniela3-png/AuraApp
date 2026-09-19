package com.aura.aura.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabla_usuarios")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int edad;

    public String tipoPerfil; // Guardaremos si es Infantil, Adolescente o Adulto

    // Constructor vacío requerido por Room
    public Usuario() {
    }
}