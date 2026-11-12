package com.aura.aura.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabla_citas_medicas")
public class CitaMedica {

    @PrimaryKey(autoGenerate = true)
    public int id_cita;

    public int id_usuario; // Llave foránea para saber a quién pertenece
    public String fecha_cita;
    public String especialidad;

    public CitaMedica() {}
}