package com.aura.aura.factory;

import com.aura.aura.models.PerfilAdolescente;
import com.aura.aura.models.PerfilAdulto;
import com.aura.aura.models.PerfilInfantil;
import com.aura.aura.models.PerfilUsuario;

public class PerfilFactory {
    public static PerfilUsuario crearPerfil(int edad) {
        if (edad >= 10 && edad <= 13) {
            return new PerfilInfantil();
        } else if (edad >= 14 && edad <= 18) {
            return new PerfilAdolescente();
        } else if (edad >= 19) {
            return new PerfilAdulto();
        }
        return null; // En el futuro podemos manejar excepciones si la edad es menor a 10
    }
}