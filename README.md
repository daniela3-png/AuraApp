# 🌸 Aura App

Aura es una aplicación móvil de Android orientada al autocuidado, la educación sexual integral (ESI) y la prevención, diseñada específicamente para adaptarse a la etapa de desarrollo de la usuaria. El proyecto prioriza la privacidad, la seguridad de los datos sensibles y la vinculación con la red de salud local en Iquique, región de Tarapacá.

## Características Principales

*   **Arquitectura Dinámica (Patrón Factory):** Implementación de perfiles de usuario (Infantil, Adolescente, Adulto) que filtran el contenido educativo y las funcionalidades clínicas según la edad.
*   **Autenticación Biométrica:** Acceso seguro a la aplicación mediante huella dactilar o reconocimiento facial (`androidx.biometric`).
*   **Base de Datos Blindada:** Encriptación de datos locales (historial menstrual, citas médicas) utilizando SQLite con **SQLCipher** (AES-256) sobre la arquitectura Room.
*   **Reglas de Negocio (RBAC):** Restricción estricta de módulos clínicos (ej. agendamiento en CESFAM) para perfiles infantiles (<14 años), protegiendo la información y evitando la autogestión inapropiada.
*   **Red de Apoyo Territorial:** Integración mediante *Implicit Intents* para trazar rutas GPS directas en Google Maps hacia los principales centros asistenciales de Iquique (CESFAM Cirujano Videla, Cirujano Aguirre, Hospital Regional, etc.).
*   **Módulo de Emergencia (Botón de Pánico):** Configuración de contactos de confianza mediante `SharedPreferences` y acceso rápido para alertas, acompañado de educación sobre el uso del SOS nativo de Android.

## Tecnologías y Herramientas

*   **Lenguaje:** Java
*   **Entorno de Desarrollo:** Android Studio
*   **Seguridad:** Android Biometric Prompt, SQLCipher (AES-256)
*   **Persistencia Local:** Room Database, SharedPreferences
*   **Integración Externa:** Google Maps (Implicit Intents)
*   **Control de Versiones:** Git & GitHub

## Estructura del Proyecto

El código fuente está estructurado de manera modular para facilitar la escalabilidad:
*   `models/`: Contiene las clases base y la implementación de la interfaz `PerfilUsuario` (Infantil, Adolescente, Adulto).
*   `factory/`: Implementación del Patrón Factory (`PerfilFactory`) para la inyección de dependencias según la edad.
*   `ui/`: Actividades principales (Dashboard, Educación, Emergencia, Red de Apoyo) diseñadas con un enfoque de Single-Scroll UI basado en tarjetas.
*   `database/`: Configuración de la base de datos cifrada y los DAOs.

## Flujo de Pantallas (UI)
1.  **Login Seguro:** Ingreso local que detona la biometría.
2.  **Dashboard Central:** Vista de scroll único con acceso al calendario menstrual, agendamiento de citas médicas y módulos auxiliares.
3.  **Módulos Educativos:** Pantallas de contenido generado dinámicamente según el rol.
4.  **Red Asistencial y Emergencia:** Funciones de acción rápida y vinculación con la comunidad.

---
*Aura App - Empoderamiento, privacidad y cuidado integral.*
