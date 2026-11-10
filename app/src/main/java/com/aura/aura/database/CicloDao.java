package com.aura.aura.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.aura.aura.models.Ciclo;
import java.util.List;

@Dao
public interface CicloDao {

    // 1. Guardar un nuevo registro de menstruación
    @Insert
    void insertarCiclo(Ciclo ciclo);

    // 2. Obtener todo el historial de una usuaria (ordenado del más reciente al más antiguo)
    @Query("SELECT * FROM Ciclo WHERE id_usuario = :idUsuario ORDER BY id_ciclo DESC")
    List<Ciclo> obtenerCiclosPorUsuario(int idUsuario);
}