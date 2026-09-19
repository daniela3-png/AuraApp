package com.aura.aura.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import com.aura.aura.models.Ciclo;

@Dao
public interface CicloDao {

    @Insert
    void insertarCiclo(Ciclo ciclo);

    @Query("SELECT * FROM tabla_ciclos ORDER BY id DESC")
    List<Ciclo> obtenerTodosLosCiclos();
}