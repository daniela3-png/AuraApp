package com.aura.aura.models;
import java.util.ArrayList;
import java.util.List;

public class PerfilAdolescente implements PerfilUsuario {
    @Override
    public String getNombrePerfil() { return "Adolescente (14 a 18 años)"; }

    @Override
    public String getEnfoque() {
        return "Integral: Decisiones informadas, relaciones respetuosas, prevención de riesgos y fomento de la autonomía.";
    }

    @Override
    public List<TemaEducativo> getMenu() {
        List<TemaEducativo> menu = new ArrayList<>();
        menu.add(new TemaEducativo("Consentimiento Pleno", "Debe ser libre, claro y reversible. La presión, el control o el miedo NO son consentimiento."));
        menu.add(new TemaEducativo("Prevención Activa", "Uso correcto del condón, métodos anticonceptivos y acceso a anticoncepción de emergencia."));
        menu.add(new TemaEducativo("ITS y Salud", "Señales de alerta, testeo rápido y búsqueda de atención médica sin vergüenza ni mitos."));
        menu.add(new TemaEducativo("Salud Mental y Vínculos", "Manejo de celos, sexting seguro, prevención de violencia en el pololeo y redes de apoyo."));
        return menu;
    }

    @Override
    public String getAcompanamiento() {
        return "Ruta para Padres/Docentes: Promover el pensamiento crítico sobre el cuerpo, la igualdad y la responsabilidad sin invadir.";
    }

    @Override
    public String getSoporte() {
        return "Espacios Amigables & CESFAM Tarapacá: Consejería y entrega de métodos anticonceptivos/píldora de emergencia para jóvenes de 14 a 18 años.";
    }

    @Override
    public String getCuandoConsultar() {
        return "Inicio de vida sexual activa, consejería o entrega confidencial de métodos anticonceptivos (incluida la píldora de emergencia), sospechas de Infecciones de Transmisión Sexual (ITS) o control de violencia en el pololeo.";
    }
}