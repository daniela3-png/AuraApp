package com.aura.aura.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.aura.aura.models.Ciclo;
import java.util.List;

@Dao
public interface CicloDao {

    @Insert
    void insertarCiclo(Ciclo ciclo);

    @Update
    void actualizarCiclo(Ciclo ciclo); // <--- NUEVO (Update)

    @Delete
    void eliminarCiclo(Ciclo ciclo);   // <--- NUEVO (Delete)

    @Query("SELECT * FROM tabla_ciclos WHERE id_usuario = :idUsuario ORDER BY id_ciclo DESC")
    List<Ciclo> obtenerCiclosPorUsuario(int idUsuario);
}