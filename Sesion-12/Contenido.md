# Contenido de la sesion - Sesion 12: Introduccion a la arquitectura MVVM

**Tema del curso:** Tema 5 - Componentes interactivos y arquitectura de la aplicacion (MVVM)

---

## 1. Objetivo de la sesion

Que el estudiante comprenda los fundamentos del patron de arquitectura MVVM (Model-View-ViewModel) y comience a reorganizar el proyecto "Task Manager" separando la logica de negocio y el estado de la interfaz mediante un `ViewModel`.

---

## 2. Guion / desarrollo de la clase

Duracion total estimada: 120 minutos.

### Bloque 1 - Apertura y contexto (10 min)
- Retroalimentacion breve sobre el Primer Momento de Evaluacion (Sesion 11): que patrones se observaron en los proyectos presentados (por ejemplo, estado manejado directamente dentro de los composables).
- Pregunta disparadora: "Que pasaria si necesitamos que el estado de la lista de tareas sobreviva a un cambio de configuracion (como rotar la pantalla) o se comparta entre varias pantallas?" Se conecta con la necesidad de una arquitectura mas robusta.
- Presentacion de la agenda: Model, View, ViewModel, y como aplican al proyecto Task Manager.

### Bloque 2 - Exposicion de contenido: que es MVVM (25 min)
- Se explica el problema que resuelve MVVM: evitar que toda la logica (estado, reglas de negocio, acceso a datos) viva dentro de los composables, lo cual dificulta las pruebas, el mantenimiento y la reutilizacion.
- Se presentan los tres componentes del patron:
  - **Model**: los datos y la logica de negocio (por ejemplo, la clase `Tarea` y las reglas para crear/completar/eliminar tareas).
  - **View**: los composables que muestran la interfaz (lo que ya se construyo en las Sesiones 7 y 8), y que idealmente no deberian contener logica de negocio.
  - **ViewModel**: la clase intermedia que expone el estado a la View (usando `State`/`StateFlow`) y recibe eventos de la View (clicks, texto ingresado), traduciendolos en cambios sobre el Model.
- Se explica el ciclo de comunicacion: la View observa el estado del ViewModel y le notifica eventos; el ViewModel nunca conoce directamente a los composables (evita referencias circulares y facilita las pruebas).
- Se menciona brevemente `ViewModel` de Android Jetpack y su ventaja principal: sobrevive a cambios de configuracion (como la rotacion de pantalla) porque no esta atado al ciclo de vida de la Activity/composable en si.

### Bloque 3 - Demo en vivo: migrando Task Manager a MVVM (30 min)
- El docente parte del proyecto Task Manager (con lista, detalle y navegacion) y muestra en vivo:
  - Creacion de `TareaViewModel` que extiende `ViewModel`, con un `StateFlow<List<Tarea>>` (o `mutableStateListOf` expuesto de forma controlada) como fuente de verdad.
  - Migracion de la logica de "agregar tarea" y "marcar como completada" desde el composable hacia metodos del `ViewModel`.
  - Actualizacion del composable `PantallaListaTareas` para que reciba el `ViewModel` (por ejemplo via `viewModel()`) y solo se encargue de mostrar el estado y delegar eventos.
- Se resalta como el composable queda mas simple y "tonto" (solo describe la UI), mientras el `ViewModel` concentra la logica.

### Bloque 4 - Practica guiada (35 min)
- Los estudiantes migran su propio proyecto (o el Task Manager) aplicando el mismo patron:
  - Crear un `ViewModel` para la pantalla de lista de tareas.
  - Mover al menos dos operaciones (agregar, completar, eliminar) desde el composable hacia el `ViewModel`.
  - Verificar que el comportamiento visual de la app no cambio para el usuario final, aunque el codigo interno si lo hizo (refuerza la idea de refactorizacion sin romper funcionalidad).
- El docente circula ayudando con dudas tipicas: como inyectar el `ViewModel` en un composable, diferencia entre `remember` y `viewModel()`, y como exponer el estado de forma inmutable hacia la View.

### Bloque 5 - Cierre y siguientes pasos (10 min)
- Puesta en comun de 1-2 migraciones realizadas.
- Resumen de conceptos: Model, View, ViewModel, separacion de responsabilidades, estado sobreviviendo a cambios de configuracion.
- Se explica la tarea de "Despues de la clase" (reestructurar el proyecto aplicando MVVM) y se comparte el material de apoyo.

---

## 3. Explicacion teorica breve

**El problema que resuelve MVVM.** A medida que un proyecto crece, mezclar en un mismo composable la descripcion visual, el manejo de estado y las reglas de negocio (por ejemplo, validaciones, persistencia, llamadas a red) genera codigo dificil de mantener, dificil de probar de forma aislada, y dificil de reutilizar entre distintas pantallas.

**Los tres componentes:**
- **Model**: representa los datos de la aplicacion y las reglas de negocio asociadas (por ejemplo, la clase `Tarea` y funciones que definen como se crea o se completa una tarea). No sabe nada de la interfaz.
- **View**: en el contexto de Jetpack Compose, son los composables. Su responsabilidad es exclusivamente mostrar el estado actual y notificar eventos del usuario (clicks, texto ingresado). Idealmente no contiene logica de negocio ni decide como transformar los datos.
- **ViewModel**: actua como intermediario. Expone el estado de forma observable (tipicamente con `StateFlow` o `State`) para que la View lo consuma, y ofrece funciones publicas que la View invoca para comunicar eventos (por ejemplo `agregarTarea(titulo)`, `marcarComoCompletada(id)`). Internamente, el `ViewModel` aplica las reglas del Model y actualiza el estado.

**Por que usar la clase `ViewModel` de Android Jetpack.** Ademas de organizar el codigo segun este patron, Android proporciona una clase base `ViewModel` que:
- Sobrevive a cambios de configuracion (como rotar la pantalla), evitando perder el estado innecesariamente.
- Tiene un ciclo de vida asociado al `NavBackStackEntry` o a la Activity/Fragment que lo alberga, y se limpia automaticamente (`onCleared()`) cuando ya no se necesita.
- Se integra naturalmente con Navigation Compose, permitiendo compartir un `ViewModel` entre pantallas cuando el diseno lo requiere, o mantenerlo aislado por pantalla cuando no.

**Relacion con lo aprendido antes.** MVVM no reemplaza los conceptos de las Sesiones 7 y 8 (layouts, estado, navegacion); los reorganiza. El estado sigue siendo el mecanismo que dispara la recomposicion, solo que ahora "vive" en el `ViewModel` en lugar de estar directamente dentro del composable.

---

## 4. Ejemplo de codigo en Kotlin

```kotlin
// Model: datos y reglas basicas de negocio
data class Tarea(
    val id: Int,
    val titulo: String,
    val completada: Boolean = false
)

// ViewModel: concentra el estado y la logica de negocio
class TareaViewModel : ViewModel() {

    private val _tareas = MutableStateFlow<List<Tarea>>(emptyList())
    val tareas: StateFlow<List<Tarea>> = _tareas.asStateFlow()

    private var siguienteId = 1

    fun agregarTarea(titulo: String) {
        if (titulo.isBlank()) return
        val nuevaTarea = Tarea(id = siguienteId++, titulo = titulo)
        _tareas.value = _tareas.value + nuevaTarea
    }

    fun marcarComoCompletada(id: Int) {
        _tareas.value = _tareas.value.map { tarea ->
            if (tarea.id == id) tarea.copy(completada = !tarea.completada) else tarea
        }
    }

    fun eliminarTarea(id: Int) {
        _tareas.value = _tareas.value.filterNot { it.id == id }
    }
}

// View: el composable solo describe la UI y delega eventos al ViewModel
@Composable
fun PantallaListaTareas(
    viewModel: TareaViewModel = viewModel()
) {
    val tareas by viewModel.tareas.collectAsState()
    var nuevoTitulo by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = nuevoTitulo,
                onValueChange = { nuevoTitulo = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Nueva tarea") }
            )
            Button(onClick = {
                viewModel.agregarTarea(nuevoTitulo)
                nuevoTitulo = ""
            }) {
                Text("Agregar")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(tareas, key = { it.id }) { tarea ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = tarea.completada,
                        onCheckedChange = { viewModel.marcarComoCompletada(tarea.id) }
                    )
                    Text(text = tarea.titulo, modifier = Modifier.weight(1f))
                    IconButton(onClick = { viewModel.eliminarTarea(tarea.id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }
    }
}
```

Puntos a resaltar en clase:
- El composable `PantallaListaTareas` ya no decide como se agrega o elimina una tarea: solo llama a metodos del `ViewModel`.
- `viewModel.tareas.collectAsState()` conecta el `StateFlow` del `ViewModel` con el mundo de Compose, disparando recomposicion cuando la lista cambia.
- La funcion `viewModel()` (de la libreria de integracion Compose-ViewModel) se encarga de crear o reutilizar la instancia correcta del `ViewModel` respetando su ciclo de vida.

---

## 5. Ejercicio practico para los estudiantes

1. Crear un `ViewModel` para la pantalla principal del proyecto Task Manager (o el proyecto propio), moviendo hacia el toda la logica que actualmente vive dentro del composable (agregar, completar, eliminar tareas).
2. Exponer el estado de la lista mediante `StateFlow` (o `State`) y conectar la View usando `collectAsState()`.
3. Verificar que, al rotar la pantalla del emulador o dispositivo, el estado de las tareas no se pierda (comportamiento esperado gracias al uso de `ViewModel`).
4. Si el proyecto tiene una pantalla de detalle de tarea (Sesion 8), evaluar si conviene que comparta el mismo `ViewModel` que la lista o si necesita uno propio, justificando la decision.
5. Dejar documentado brevemente (por ejemplo en el README del proyecto) como quedo organizado el codigo despues de aplicar MVVM.

Este ejercicio corresponde a la actividad de "Despues de la clase": reestructurar el proyecto aplicando MVVM.

---

## 6. Material de apoyo / enlaces

- Documentacion general de Kotlin: https://kotlinlang.org/docs/home.html
- Documentacion oficial de Android: https://developer.android.com/docs
- Jetpack Compose (guia general): https://developer.android.com/jetpack/compose
- Arquitectura de aplicaciones Android (guia oficial, incluye MVVM): https://developer.android.com/topic/architecture
- `ViewModel` en Android Jetpack: https://developer.android.com/topic/libraries/architecture/viewmodel
- Estado y Compose (`StateFlow`, `collectAsState`): https://developer.android.com/jetpack/compose/state
