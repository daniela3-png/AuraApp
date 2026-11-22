package com.aura.aura.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabla_ciclos")
public class Ciclo {

    @PrimaryKey(autoGenerate = true)
    public int id_ciclo;

    public int id_usuario;
    public String fecha_inicio;
    public String fecha_fin;

    public Ciclo() {
    }
}