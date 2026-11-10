package com.aura.aura.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// @Entity le dice a SQLite que esta clase es una tabla
@Entity(tableName = "Usuario")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public int id_usuario;

    public String nombre;
    public String email;
    public String pin_acceso;

    // SQLite no tiene un tipo "DATE" nativo.
    // Guardaremos la fecha como String en formato "YYYY-MM-DD" para facilitar el cálculo de edad.
    public String fecha_nacimiento;
    public String tipo_perfil;
    public String estado_cuenta;
    public String codigo_vinculacion;

    // Usamos la clase 'Integer' (con mayúscula) en vez del primitivo 'int'
    // porque 'Integer' permite valores NULL, lo cual es obligatorio para Adolescentes y Adultos.
    public Integer id_tutor;

    // Constructor vacío obligatorio para que Room pueda leer los datos
    public Usuario() {
    }
}