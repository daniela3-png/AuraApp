package com.aura.aura.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.aura.aura.models.Usuario;

@Dao
public interface UsuarioDao {

    @Insert
    void insertarUsuario(Usuario usuario);

    @Query("SELECT * FROM tabla_usuarios LIMIT 1")
    Usuario obtenerUsuarioActual();
}