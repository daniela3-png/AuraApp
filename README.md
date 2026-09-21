# 🌸 Aura App

Aura es una aplicación móvil nativa para Android orientada al autocuidado, la educación sexual integral (ESI) y la prevención, diseñada específicamente para adaptarse a la etapa de desarrollo de la usuaria. El proyecto prioriza el rendimiento local, la privacidad absoluta de los datos sensibles y la vinculación con la red de salud en Iquique, región de Tarapacá.

## Características Principales

* **Arquitectura Dinámica (Patrón Factory):** Implementación de perfiles de usuario (Infantil, Adolescente, Adulto) que filtran el contenido educativo y las funcionalidades clínicas en tiempo de ejecución según la edad de la usuaria.
* **Seguridad y Privacidad Zero-Trust:** Acceso seguro a la aplicación mediante validación de hardware (huella dactilar o reconocimiento facial usando `androidx.biometric`), sin almacenamiento local de contraseñas.
* **Base de Datos Blindada:** Encriptación de datos locales (historial menstrual, citas médicas) utilizando el estándar AES-256 mediante SQLCipher sobre la arquitectura Room.
* **Control de Acceso Basado en Roles (RBAC):** Restricción estricta de módulos clínicos (ej. agendamiento en CESFAM) para perfiles infantiles (<14 años), protegiendo la información y evitando la autogestión médica inapropiada para la edad.
* **Red de Apoyo Territorial:** Integración mediante Implicit Intents para trazar rutas GPS directas en Google Maps hacia los principales centros asistenciales de Iquique (CESFAM Cirujano Videla, Cirujano Aguirre, Hospital Regional, etc.), evitando la sobrecarga de permisos de ubicación.
* **Módulo de Emergencia (SOS Híbrido):** Para evitar el drenaje de batería y asegurar la mayor rapidez en situaciones de crisis, la aplicación omite el secuestro de botones físicos. En su lugar, despliega una UI educativa que instruye a la usuaria sobre la configuración y activación del **SOS de Emergencia nativo de Android** (5 clics al botón de encendido), complementado con la gestión local de contactos de confianza mediante SharedPreferences.

## Tecnologías y Herramientas

* **Lenguaje:** Java (Nativo)
* **Entorno de Desarrollo:** Android Studio
* **Seguridad:** Android Biometric Prompt, SQLCipher (AES-256)
* **Persistencia Local:** Room Database, SharedPreferences
* **Integración Externa:** Google Maps API (vía Implicit Intents)
* **Control de Versiones:** Git & GitHub

## Estructura del Proyecto

El código fuente está estructurado de manera modular para garantizar baja cohesión y fácil escalabilidad:
* `models/`: Contiene las clases base y la implementación de la interfaz `PerfilUsuario` (Infantil, Adolescente, Adulto).
* `factory/`: Implementación del Patrón Factory (`PerfilFactory`) para la inyección de dependencias y renderizado según la edad.
* `ui/`: Actividades principales (Dashboard, Educación, Emergencia, Red de Apoyo) diseñadas con un enfoque de Single-Scroll UI basado en tarjetas Material Design.
* `database/`: Configuración de la base de datos cifrada y los Data Access Objects (DAOs).

## Flujo de Pantallas (UI)

1. **Login Seguro:** Ingreso local que detona la biometría del sistema operativo.
2. **Dashboard Central:** Vista de scroll único con acceso al calendario menstrual, agendamiento de citas médicas y módulos auxiliares.
3. **Módulos Educativos:** Pantallas de contenido inyectado dinámicamente según el rol activo.
4. **Red Asistencial y Emergencia:** Funciones de acción rápida, orientación territorial y vinculación con la comunidad.

## Próximos Pasos (Roadmap)
* **Cloud Sync:** Migración opcional de los repositorios cifrados hacia una infraestructura en la nube utilizando **MongoDB Atlas** vía API REST, permitiendo la vinculación segura de cuentas (ej. Tutor-Menor).
* **Alertas Push Offline:** Integración de `WorkManager` para agendar notificaciones locales sobre ciclos menstruales y controles médicos sin depender de servidores externos.

---
*Aura App - Empoderamiento, privacidad y cuidado integral.*
