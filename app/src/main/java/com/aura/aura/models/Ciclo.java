package com.aura.aura.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabla_ciclos")
public class Ciclo {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String fechaInicio;
    public String fechaFin;

    // Constructor vacío requerido por Room
    public Ciclo() {
    }
}