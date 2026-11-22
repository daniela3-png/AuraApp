# 🌸 Aura App

**Aura** es una aplicación móvil nativa para Android orientada al autocuidado, la educación sexual integral (ESI) y la prevención, diseñada específicamente para adaptarse a la etapa de desarrollo de la usuaria. El proyecto combina rendimiento y privacidad local mediante cifrado en dispositivo con sincronización remota en tiempo real en la nube y vinculación con la red de salud en Iquique, región de Tarapacá.

## Características Principales

- **Arquitectura Dinámica (Patrón Factory):** Implementación de perfiles de usuario (`PerfilInfantil`, `PerfilAdolescente`, `PerfilAdulto`) que filtran el contenido educativo y las funcionalidades clínicas en tiempo de ejecución según la edad de la usuaria.
- **Seguridad y Privacidad Zero-Trust:** Acceso seguro mediante validación biométrica de hardware (`androidx.biometric`) y protección criptográfica mediante hashing **SHA-256** para PINs de vinculación antes de su transmisión y almacenamiento en la nube.
- **Persistencia Híbrida (SQLite Blindado + Firebase Realtime Database):**
    - **Almacenamiento Local:** Encriptación de datos sensibles en el dispositivo (historial menstrual, citas médicas, usuarios) utilizando el estándar AES-256 mediante **SQLCipher** sobre la arquitectura **Room**, garantizando operación offline sin pérdida de datos.
    - **Sincronización Remota en Tiempo Real:** Integración asíncrona con **Firebase Realtime Database** organizada por nodos de usuario (`aura_app/usuarios`, `aura_app/ciclos`, `aura_app/citas`), con escucha reactiva (`ValueEventListener`) y manejo controlado de errores de conectividad.
- **Control de Acceso Basado en Roles (RBAC):** Restricción estricta de módulos clínicos (agendamiento médico) para perfiles infantiles (<14 años) y flujo de vinculación supervisada Tutor–Menor mediante PIN de aprobación.
- **Red de Apoyo Territorial:** Integración mediante *Implicit Intents* para trazar rutas GPS directas en Google Maps hacia los principales centros asistenciales de Iquique (CESFAM Cirujano Videla, Cirujano Aguirre, Hospital Regional, entre otros), evitando permisos innecesarios de ubicación en segundo plano.
- **Módulo de Emergencia (SOS Híbrido):** Interfaz educativa que instruye sobre la activación del SOS de Emergencia nativo de Android (5 clics al botón de encendido), complementada con la gestión local de contactos de confianza mediante `SharedPreferences`.

## Tecnologías y Herramientas

- **Lenguaje:** Java (Nativo)
- **Entorno de Desarrollo:** Android Studio (Gradle Kotlin DSL)
- **Seguridad:** Android Biometric Prompt, SQLCipher (AES-256), SHA-256 (`MessageDigest`)
- **Persistencia Local:** Room Database (SQLite), SharedPreferences
- **Base de Datos Remota (Cloud):** Firebase Realtime Database (`firebase-bom`, `firebase-database`)
- **Integración Externa:** Google Maps (vía *Implicit Intents*)
- **Control de Versiones:** Git & GitHub

## Estructura del Proyecto

El código fuente mantiene una separación clara entre interfaz de usuario, lógica de negocio y acceso a datos:

- `models/`: Entidades compatibles con Room y Firebase (`Usuario`, `Ciclo`, `CitaMedica`, `TemaEducativo`) e implementación de la interfaz `PerfilUsuario` (`PerfilInfantil`, `PerfilAdolescente`, `PerfilAdulto`).
- `factory/`: Implementación del Patrón Factory (`PerfilFactory`) para la instanciación dinámica de roles según la edad.
- `database/`: Configuración de la base de datos local cifrada (`AppDatabase`), Data Access Objects (`UsuarioDao`, `CicloDao`, `CitaMedicaDao`) y gestor remoto (`FirebaseRepository`) para operaciones CRUD asíncronas y hashing de credenciales.
- `ui/`: Actividades principales (`LoginActivity`, `RegistroActivity`, `DashboardActivity`, `EducacionActivity`, `EmergenciaActivity`, `RedApoyoActivity`) diseñadas con tarjetas Material Design.

## Flujo de Pantallas (UI)

1. **Login y Registro Seguro:** Autenticación local con respaldo biométrico del sistema operativo y validación de estado de cuenta (`Activa` / `Pendiente_Aprobacion`).
2. **Dashboard Central:** Vista principal con registro y consulta en tiempo real de ciclos menstruales, agendamiento de citas médicas y vinculación de perfiles infantiles.
3. **Módulos Educativos:** Contenido adaptado dinámicamente según el perfil activo.
4. **Red Asistencial y Emergencia:** Acceso rápido a centros de salud locales en Iquique y protocolos de emergencia.

## Próximos Pasos (Roadmap)

- **Alertas Push Offline:** Integración de `WorkManager` para programar recordatorios locales sobre ciclos menstruales y controles médicos agendados.
- **Autenticación Federada:** Incorporación de Firebase Authentication para gestión multi-dispositivo de cuentas tutoras.

---
*Aura App — Empoderamiento, privacidad y cuidado integral.*
