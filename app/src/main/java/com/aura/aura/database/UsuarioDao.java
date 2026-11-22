package com.aura.aura.database; // Asegúrate de que coincida con tu paquete

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.aura.aura.models.Usuario;

@Dao
public interface UsuarioDao {

    @Insert
    void insertarUsuario(Usuario usuario);

    @Update
    void actualizarUsuario(Usuario usuario);

    @Query("SELECT * FROM tabla_usuarios WHERE id_usuario = :idUsuario LIMIT 1")
    Usuario obtenerUsuarioPorId(int idUsuario);

    // Consulta para vincular perfil infantil mediante el PIN (Código AURA)
    @Query("SELECT * FROM tabla_usuarios WHERE pin = :codigo LIMIT 1")
    Usuario obtenerUsuarioPorCodigo(String codigo);

    // ESTE ES EL NUEVO MÉTODO PARA TU LOGIN
    @Query("SELECT * FROM tabla_usuarios WHERE email = :correo AND pin = :pinIngresado LIMIT 1")
    Usuario loginUsuario(String correo, String pinIngresado);
}