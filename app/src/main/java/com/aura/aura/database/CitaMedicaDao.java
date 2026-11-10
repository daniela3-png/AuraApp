package com.aura.aura.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.aura.aura.models.CitaMedica;
import java.util.List;

@Dao
public interface CitaMedicaDao {

    @Insert
    void insertarCita(CitaMedica cita);

    @Query("SELECT * FROM CitaMedica WHERE id_usuario = :idUsuario ORDER BY fecha_cita ASC")
    List<CitaMedica> obtenerCitasPorUsuario(int idUsuario);
}