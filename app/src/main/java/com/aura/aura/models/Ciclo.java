package com.aura.aura.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

// @Entity define la tabla. ForeignKey asegura que si se borra una usuaria, se borren sus ciclos (CASCADE)
@Entity(tableName = "Ciclo",
        foreignKeys = @ForeignKey(entity = Usuario.class,
                parentColumns = "id_usuario",
                childColumns = "id_usuario",
                onDelete = ForeignKey.CASCADE))
public class Ciclo {

    @PrimaryKey(autoGenerate = true)
    public int id_ciclo;

    // Esta es la llave foránea que une esta tabla con la tabla Usuario
    public int id_usuario;

    public String fecha_inicio;
    public String fecha_fin;

    // Constructor vacío requerido por Room
    public Ciclo() {
    }
}