# Task Manager UPB - Proyecto Integrador

Proyecto Android (Kotlin + Jetpack Compose) usado como ejemplo en vivo durante
la materia Programacion III. El historial de commits de este proyecto avanza
sesion a sesion junto con el silabo: cada commit relevante esta etiquetado con
el numero de sesion en su mensaje, para poder revisar `git log` y ver
exactamente que codigo se agrego en cada clase.

## Como abrirlo

1. Abrir Android Studio (Hedgehog o superior) y seleccionar "Open" sobre esta
   carpeta (`ProyectoIntegrador`).
2. Si Android Studio pide generar el Gradle Wrapper, aceptar (este repositorio
   no incluye el binario `gradle-wrapper.jar`; Android Studio lo genera al
   sincronizar el proyecto).
3. Esperar la sincronizacion de Gradle y ejecutar la app en un emulador o
   dispositivo con API 24 o superior.

## Version del entorno usada

- Android Gradle Plugin 8.7.0
- Kotlin 1.9.24
- Gradle 8.9 (compatible con JDK 17 a 21)
- compileSdk / targetSdk 34, minSdk 24
- Jetpack Compose BOM 2023.05.01 (Compose Compiler 1.5.14)

## Como recorrer la evolucion del proyecto

```bash
git log --oneline -- ProyectoIntegrador
```

Cada entrada corresponde a los contenidos de una sesion del curso; el detalle
teorico de cada sesion esta en `../Sesion-XX/Contenido.md`.

## Arquitectura

La app sigue una arquitectura por capas, del tipo UI -> ViewModel ->
Repositorio -> fuente de datos, organizada en paquetes bajo
`com.upb.taskmanager`:

- **`model`** - El dominio de la app. `Tarea` es el modelo principal (id,
  titulo, completada). `GestorDeTareas` contiene la logica de negocio;
  empezo como una lista en memoria (Sesion 3) y desde la Sesion 23 delega
  toda la persistencia en `TareasLocalRepository` (Room), exponiendo la
  lectura como un `Flow<List<Tarea>>` y las escrituras como `suspend fun`.
- **`ui.screens` / `ui.components` / `ui.theme`** - La capa de presentacion,
  en Jetpack Compose. `ui.screens` tiene las pantallas completas
  (`TareaListScreen`, `TareaDetailScreen`); `ui.components` tiene piezas
  reutilizables entre pantallas (`TareaCard`, `TareaFormulario`, extraidas en
  la Sesion 25); `ui.theme` define el tema Material 3 propio
  (`TaskManagerTheme`). Las pantallas nunca acceden directamente a Room o a
  Retrofit: solo leen el `StateFlow` del ViewModel y le delegan las acciones
  del usuario.
- **`navigation`** - `AppNavigation` define el grafo de Navigation Compose
  (rutas "lista" y "detalle/{tareaId}") y es quien crea el `TareasViewModel`
  compartido entre esas rutas.
- **`viewmodel`** - `TareasViewModel` es el puente entre la UI y los datos.
  Expone un unico `StateFlow<TareasUiState>` (tareas, cargando, mensajeError)
  que combina el `Flow` reactivo de Room con el estado de carga/error del
  propio ViewModel, y traduce las acciones de la UI (agregar, alternar
  completada) en llamadas a `GestorDeTareas`.
- **`data.local`** - Persistencia con Room: `TareaEntity` (la fila de la
  tabla "tareas"), `TareaDao` (las consultas SQL generadas por Room) y
  `TareaDatabase` (la base de datos, con un singleton para evitar abrir mas
  de una instancia). `TareasLocalRepository` envuelve el DAO con operaciones
  orientadas al dominio.
- **`data.remote`** - Acceso a red con Retrofit: `ApiService` (los
  endpoints), `RetrofitInstance` (el cliente Retrofit) y `TareaDto` (la
  forma exacta de la respuesta JSON). `TareasRemoteRepository` envuelve la
  llamada de red en un `Resultado` (exito/error) para que el ViewModel no
  tenga que lidiar con excepciones directamente. Se usa solo para sincronizar
  tareas de ejemplo la primera vez que se abre la app (ver Sesion 23).
- **`data.datastore`** - Preferencias simples que sobreviven a que se cierre
  la app (`PreferenciasUsuario`, con Preferences DataStore), como si el
  usuario prefiere ver solo las tareas pendientes por defecto.
- **`util`** - Funciones y tipos compartidos que no pertenecen a ninguna capa
  en particular: validaciones (`tituloValido`, `contarPendientes`) y el
  envoltorio `Resultado` para manejo de errores.

En resumen, la fuente de verdad de las tareas es siempre Room (a traves de
`GestorDeTareas.observarTareas()`); Retrofit solo aporta datos de ejemplo la
primera vez, y DataStore guarda una preferencia de UI independiente de los
datos de las tareas.

## Generar el APK/AAB (Sesion 27)

El build type `release` de `app/build.gradle.kts` ya tiene `isMinifyEnabled
= true` (con las reglas de ProGuard/R8 en `proguard-rules.pro`) y un
`signingConfig` de ejemplo basado en la clave de depuracion, solo para poder
generar un instalable desde este proyecto sin configurar nada adicional.

Para generar el paquete desde Android Studio:

1. Menu **Build > Generate Signed Bundle / APK...**
2. Elegir **Android App Bundle** (recomendado para Google Play) o **APK**.
3. En la pantalla de firma, se puede usar la clave de depuracion de este
   proyecto (la que ya esta configurada) para probar el flujo, o crear un
   keystore propio con **Create new...**.
4. Seleccionar el build variant `release` y terminar el asistente; el
   archivo generado queda dentro de `app/release/`.

**Importante:** el `signingConfig` de este repositorio es solo un ejemplo
para el curso. Antes de publicar la app de verdad hay que generar un
keystore propio, mantenerlo fuera del control de versiones y usarlo en el
build type `release` en vez del de depuracion.
