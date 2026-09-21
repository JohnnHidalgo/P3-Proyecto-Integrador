# Contenido de la Sesion 13 - ViewModel y gestion del estado

**Tema del curso:** Tema 5 - Componentes interactivos y arquitectura de la aplicacion (MVVM)

## 1. Objetivo de la sesion

Que el estudiante comprenda el rol del `ViewModel` dentro de la arquitectura MVVM en Android y sea capaz de separar la logica de negocio del estado de la interfaz de usuario, gestionando el estado de una pantalla de Jetpack Compose de forma robusta y sobreviviente a cambios de configuracion.

## 2. Guion / desarrollo de la clase (aprox. 2 horas)

### Bloque 1 - Apertura (10 min)
- Bienvenida y verificacion de asistencia.
- Recordatorio rapido de lo visto en sesiones anteriores sobre componentes interactivos de Compose (`State`, `remember`, `mutableStateOf`).
- Pregunta disparadora: "Que pasa con el estado de una pantalla cuando el celular rota o cuando el proceso del sistema recupera memoria?"
- Se anuncia el objetivo de la clase: introducir `ViewModel` como solucion arquitectonica a ese problema.

### Bloque 2 - Exposicion de contenido (30 min)
- Repaso del patron MVVM (Model - View - ViewModel) y por que Android lo recomienda.
- Ciclo de vida de un `ViewModel` frente al ciclo de vida de un `Composable` / `Activity`.
- Diferencia entre estado efimero de UI (`remember`) y estado que debe sobrevivir a recomposiciones y cambios de configuracion (`ViewModel` + `StateFlow`/`State`).
- Introduccion a `viewModelScope` como alcance de corrutinas ligado al ciclo de vida del `ViewModel`.
- Presentacion del caso de estudio que se usara durante el resto del modulo: una aplicacion de **gestion de tareas** (Task Manager) que mas adelante consumira una API REST.

### Bloque 3 - Demostracion en vivo (25 min)
- El docente crea, en vivo, un `TareasViewModel` minimo con una lista de tareas en memoria.
- Se muestra como exponer el estado con `StateFlow` y como consumirlo desde un `Composable` con `collectAsState()`.
- Se demuestra el uso de `by viewModel()` o `viewModel()` (dependiendo de la libreria elegida) para obtener la instancia del `ViewModel` desde la UI.
- Se provoca un cambio de configuracion (rotacion) para mostrar que el estado del `ViewModel` persiste, mientras que una variable `remember` comun se reinicia.

### Bloque 4 - Practica guiada (35 min)
- Los estudiantes replican el `TareasViewModel` en su propio proyecto (o en un proyecto de practica).
- Se agregan funciones para: agregar una tarea, marcarla como completada y eliminarla, todas actualizando el `StateFlow` interno.
- El docente circula por la sala resolviendo dudas y corrigiendo errores comunes (mutar el estado directamente en lugar de emitir un nuevo valor inmutable, no usar `MutableStateFlow` privado, etc.).

### Bloque 5 - Puesta en comun y cierre (15 min)
- Dos o tres estudiantes muestran su avance en pantalla.
- Se resumen los errores mas frecuentes detectados durante la practica.
- Se explica la tarea para despues de la clase.
- Se adelanta que en la Sesion 14 este mismo `ViewModel` se conectara a una API real usando Retrofit.

### Bloque 6 - Cierre administrativo (5 min)
- Recordatorio de material de lectura complementario.
- Espacio para preguntas finales.

## 3. Explicacion teorica breve

### 3.1 Arquitectura MVVM en Android
MVVM organiza la aplicacion en tres capas:
- **Model**: representa los datos y la logica de negocio (en este curso, mas adelante, los datos que llegan de una API REST).
- **View**: en Jetpack Compose, son las funciones `@Composable` que dibujan la interfaz y reaccionan a los cambios de estado.
- **ViewModel**: actua como intermediario. Expone el estado de la pantalla a la View y contiene la logica para modificarlo, sin conocer detalles de como se dibuja la UI.

Esta separacion facilita las pruebas unitarias, evita que la logica de negocio quede atrapada dentro de un `Composable` y hace que la aplicacion sea mas facil de mantener.

### 3.2 Ciclo de vida del ViewModel
Un `ViewModel` esta disenado para sobrevivir a cambios de configuracion (como la rotacion de pantalla) porque su ciclo de vida esta ligado al `ViewModelStoreOwner` (por ejemplo, la `Activity` o un `NavBackStackEntry`), no a la `Activity` individual que se recrea. Esto evita perder el estado de la pantalla innecesariamente y evita relanzar llamadas de red cada vez que gira el dispositivo.

### 3.3 Estado observable: State y StateFlow
- `mutableStateOf` es ideal para estado local y efimero de un `Composable`.
- `StateFlow` (de Kotlin Coroutines) es ideal para exponer estado desde un `ViewModel`, porque es observable, tiene un valor actual siempre disponible y se integra bien con corrutinas.
- La UI se suscribe al `StateFlow` mediante `collectAsState()` (o `collectAsStateWithLifecycle()` en proyectos que usan la libreria de lifecycle-compose), traduciendo el flujo en un `State<T>` que Compose puede observar para recomponer la UI.

### 3.4 viewModelScope
`viewModelScope` es un `CoroutineScope` que ya viene atado al ciclo de vida del `ViewModel`: cuando el `ViewModel` se destruye, todas las corrutinas lanzadas en ese scope se cancelan automaticamente. Esto evita fugas de memoria y llamadas huerfanas, y sera fundamental cuando en la Sesion 15 se lancen llamadas de red asincronas.

## 4. Ejemplo de codigo en Kotlin

```kotlin
// Modelo de datos simple para el caso de estudio "Task Manager"
data class Tarea(
    val id: Int,
    val titulo: String,
    val completada: Boolean = false
)

// Representa el estado completo de la pantalla de tareas
data class TareasUiState(
    val tareas: List<Tarea> = emptyList(),
    val cargando: Boolean = false
)
```

```kotlin
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TareasViewModel : ViewModel() {

    // El estado interno es mutable, pero solo el ViewModel puede modificarlo
    private val _uiState = MutableStateFlow(TareasUiState())

    // La UI solo puede leer el estado, nunca modificarlo directamente
    val uiState: StateFlow<TareasUiState> = _uiState.asStateFlow()

    private var siguienteId = 1

    fun agregarTarea(titulo: String) {
        if (titulo.isBlank()) return

        val nuevaTarea = Tarea(id = siguienteId++, titulo = titulo)
        _uiState.value = _uiState.value.copy(
            tareas = _uiState.value.tareas + nuevaTarea
        )
    }

    fun marcarComoCompletada(id: Int) {
        val tareasActualizadas = _uiState.value.tareas.map { tarea ->
            if (tarea.id == id) tarea.copy(completada = true) else tarea
        }
        _uiState.value = _uiState.value.copy(tareas = tareasActualizadas)
    }

    fun eliminarTarea(id: Int) {
        val tareasFiltradas = _uiState.value.tareas.filterNot { it.id == id }
        _uiState.value = _uiState.value.copy(tareas = tareasFiltradas)
    }
}
```

```kotlin
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PantallaTareas(viewModel: TareasViewModel = viewModel()) {
    val estado by viewModel.uiState.collectAsState()

    Column {
        estado.tareas.forEach { tarea ->
            // Aqui se dibujaria cada tarea con su titulo y su estado
            // usando componentes de Compose como Text, Checkbox, etc.
        }
    }
}
```

## 5. Ejercicio practico para los estudiantes

Implementar un `ViewModel` en el proyecto personal del curso (el proyecto integrador que cada estudiante viene desarrollando), aplicando lo siguiente:

1. Crear una clase que extienda `ViewModel` para al menos una pantalla del proyecto.
2. Definir una clase de estado (`data class`) que represente todo lo que la pantalla necesita mostrar.
3. Exponer el estado mediante `StateFlow` (privado mutable, publico inmutable) siguiendo el patron mostrado en clase.
4. Migrar al menos una porcion de logica que hoy vive dentro de un `Composable` (por ejemplo, una lista o un contador) hacia el `ViewModel`.
5. Verificar, provocando una rotacion de pantalla en el emulador o dispositivo, que el estado gestionado por el `ViewModel` no se pierde.
6. Documentar brevemente en el repositorio del proyecto (en un README o comentario) que decisiones se tomaron al separar la logica de negocio de la interfaz.

Esta tarea es la base sobre la cual, en las siguientes sesiones, se conectara el `ViewModel` a un servicio REST real mediante Retrofit y Coroutines.

## 6. Material de apoyo / enlaces

- Guia oficial de ViewModel en Android Developers: https://developer.android.com/topic/libraries/architecture/viewmodel
- Guia de estado y arquitectura de la app: https://developer.android.com/topic/architecture
- Manejo de estado en Jetpack Compose: https://developer.android.com/jetpack/compose/state
- Documentacion de Kotlin Coroutines (para entender `viewModelScope` mas adelante): https://kotlinlang.org/docs/coroutines-overview.html
- Documentacion general de Jetpack Compose: https://developer.android.com/jetpack/compose
