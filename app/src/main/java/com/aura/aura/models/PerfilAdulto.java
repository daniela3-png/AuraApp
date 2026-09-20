package com.aura.aura.models;
import java.util.ArrayList;
import java.util.List;

public class PerfilAdulto implements PerfilUsuario {
    @Override
    public String getNombrePerfil() { return "Adulto (19+ años)"; }

    @Override
    public String getEnfoque() {
        return "Integral: Autonomía, bienestar sexual, co-responsabilidad, placer cuidado y relaciones adultas sanas.";
    }

    @Override
    public List<TemaEducativo> getMenu() {
        List<TemaEducativo> menu = new ArrayList<>();
        menu.add(new TemaEducativo("Planificación Familiar", "Comparativa de métodos, continuidad de uso y guía en la consulta profesional."));
        menu.add(new TemaEducativo("Controles Preventivos", "Testeo oportuno de ITS, examen PAP, vacuna VPH y chequeos preventivos de mama."));
        menu.add(new TemaEducativo("Relaciones Adultas Sanas", "Comunicación, acuerdos mutuos, límites claros y autocuidado emocional."));
        menu.add(new TemaEducativo("Derechos Reproductivos", "Confidencialidad en la atención, trato digno e información científica oportuna."));
        return menu;
    }

    @Override
    public String getAcompanamiento() {
        return "Ruta de Apoyo: Acompañar en el ejercicio autónomo de controles ginecológicos y derivar ante dolores de pelvis persistentes.";
    }

    @Override
    public String getSoporte() {
        return "Red de Salud Tarapacá: Acceso a Matronería en CESFAM y derivaciones oportunas ante sospechas de ITS, dolores o dudas de anticoncepción.";
    }

    @Override
    public String getCuandoConsultar() {
        return "Controles preventivos ginecológicos anuales (Papanicolaou - PAP, examen físico de mamas), planificación familiar, dolores ginecológicos severos o persistentes, o para orientación sobre derechos reproductivos.";
    }
}