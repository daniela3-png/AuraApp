package com.aura.aura.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.aura.aura.models.CitaMedica;
import java.util.List;

@Dao
public interface CitaMedicaDao {

    @Insert
    void insertarCita(CitaMedica cita);

    @Update
    void actualizarCita(CitaMedica cita);

    @Delete
    void eliminarCita(CitaMedica cita);

    @Query("SELECT * FROM tabla_citas_medicas WHERE id_usuario = :idUsuario ORDER BY fecha_cita ASC")
    List<CitaMedica> obtenerCitasPorUsuario(int idUsuario);
}