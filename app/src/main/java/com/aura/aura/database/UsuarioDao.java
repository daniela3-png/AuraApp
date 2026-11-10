package com.aura.aura.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.aura.aura.models.Usuario;
import java.util.List;

@Dao
public interface UsuarioDao {

    // 1. Guardar una nueva usuaria (Registro)
    @Insert
    void insertarUsuario(Usuario usuario);

    // 2. Actualizar datos (Ej: Cuando el tutor aprueba la cuenta y cambia el estado a "Activa")
    @Update
    void actualizarUsuario(Usuario usuario);

    // 3. Buscar usuaria para el Login
    @Query("SELECT * FROM Usuario WHERE email = :email LIMIT 1")
    Usuario obtenerUsuarioPorEmail(String email);

    // 4. Buscar a una niña pendiente usando el código que le llegó al adulto al correo
    @Query("SELECT * FROM Usuario WHERE codigo_vinculacion = :codigo LIMIT 1")
    Usuario obtenerUsuarioPorCodigo(String codigo);

    // 5. Relación Recursiva: Obtener todas las niñas que están a cargo de un adulto
    @Query("SELECT * FROM Usuario WHERE id_tutor = :idTutor")
    List<Usuario> obtenerMenoresVinculados(int idTutor);

    // 6. Buscar usuaria por su ID (para el saludo del Dashboard)
    @Query("SELECT * FROM Usuario WHERE id_usuario = :id LIMIT 1")
    Usuario obtenerUsuarioPorId(int id);
}