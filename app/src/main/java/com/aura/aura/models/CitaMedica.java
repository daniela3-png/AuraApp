package com.aura.aura.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "CitaMedica",
        foreignKeys = @ForeignKey(entity = Usuario.class,
                parentColumns = "id_usuario",
                childColumns = "id_usuario",
                onDelete = ForeignKey.CASCADE))
public class CitaMedica {

    @PrimaryKey(autoGenerate = true)
    public int id_cita;

    public int id_usuario; // Llave foránea

    public String fecha_cita;
    public String especialidad; // Ej: Matrona, Psicólogo, etc.

    public CitaMedica() {
    }
}