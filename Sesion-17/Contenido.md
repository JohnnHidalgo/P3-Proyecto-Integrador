# Contenido de la Sesion 17 - Integracion de funcionalidades y optimizacion

**Tema del curso:** Tema 6 - Comunicacion e integracion de datos

## 1. Objetivo de la sesion

Que el estudiante sea capaz de integrar las funcionalidades desarrolladas en las sesiones previas (ViewModel, Retrofit, Coroutines, validaciones y manejo de errores) en una sola experiencia de usuario coherente, aplicando tecnicas de optimizacion de la interfaz en Jetpack Compose para dejar listo el segundo avance del proyecto integrador.

## 2. Guion / desarrollo de la clase (aprox. 2 horas)

### Bloque 1 - Apertura (10 min)
- Bienvenida y repaso del camino recorrido: `ViewModel` (Sesion 13), Retrofit y configuracion del cliente HTTP (Sesion 14), consumo asincrono con Coroutines (Sesion 15) y validaciones/manejo de errores (Sesion 16).
- Se plantea el objetivo de la sesion: unir todas estas piezas en una pantalla completa, fluida y bien optimizada, como cierre del segundo avance del proyecto.

### Bloque 2 - Exposicion de contenido: integracion de funcionalidades (20 min)
- Revision del flujo completo de datos: UI dispara una accion, el `ViewModel` valida, llama al servicio mediante una corrutina, maneja el resultado (exito o error) y actualiza el `StateFlow`, que la UI observa y renderiza.
- Discusion sobre como estructurar una pantalla que debe mostrar tres estados posibles: cargando, contenido cargado con exito, y error.
- Patron de "un solo estado de UI" (`UiState` unico) frente a multiples variables de estado sueltas, y por que el primero facilita razonar sobre la pantalla completa.

### Bloque 3 - Exposicion de contenido: optimizacion de la interfaz en Compose (25 min)
- Concepto de recomposicion y por que recomponer de mas puede afectar el rendimiento.
- Uso de `remember` y `derivedStateOf` para evitar recalculos innecesarios.
- Uso de claves (`key`) en listas (`LazyColumn`) para que Compose identifique correctamente los elementos y evite recomposiciones o animaciones incorrectas al agregar, eliminar o reordenar elementos.
- Extraccion de Composables pequenos y enfocados (principio de responsabilidad unica en la UI) para limitar el alcance de cada recomposicion.
- Uso de `LazyColumn` en lugar de `Column` con `forEach` para listas potencialmente largas, evitando dibujar elementos fuera de pantalla.

### Bloque 4 - Demostracion en vivo (35 min)
- El docente retoma el proyecto "Task Manager" construido a lo largo del modulo y ensambla la pantalla final: un `LazyColumn` con las tareas, un indicador de carga, un area de error y un formulario de creacion con validacion.
- Se migra la lista de tareas de un `Column` con `forEach` a un `LazyColumn` con `items(..., key = { it.id })`.
- Se extraen Composables como `ItemTarea`, `FormularioNuevaTarea` y `IndicadorDeEstado` para mejorar la legibilidad y el rendimiento.
- Se muestra como el `when` sobre el estado (cargando / contenido / error) simplifica la logica de la pantalla principal.

### Bloque 5 - Practica guiada (25 min)
- Los estudiantes integran, en su propio proyecto, las piezas construidas en las sesiones anteriores dentro de una sola pantalla coherente.
- Aplican al menos dos tecnicas de optimizacion vistas en clase (por ejemplo, `LazyColumn` con `key`, o extraccion de Composables).
- Verifican visualmente (con las herramientas de Layout Inspector o simplemente observando fluidez al interactuar) que la pantalla responde de forma adecuada.

### Bloque 6 - Cierre (5 min)
- Se resumen los conceptos de integracion y optimizacion cubiertos.
- Se explica la actividad para despues de la clase: consolidar el segundo avance del proyecto.
- Se recuerda que este avance representa el cierre del bloque de "Comunicacion e integracion de datos" del curso.

## 3. Explicacion teorica breve

### 3.1 Integracion de capas en una arquitectura MVVM
Una pantalla bien integrada no mezcla responsabilidades: la View (Composable) solo se encarga de mostrar el estado actual y notificar eventos del usuario; el `ViewModel` orquesta la logica (validacion, llamada de red, manejo de errores) y expone un unico estado consolidado. Esta sesion busca que los estudiantes vean el "producto terminado" de las piezas trabajadas por separado en sesiones anteriores.

### 3.2 Recomposicion en Jetpack Compose
Compose reconstruye (recompone) las partes de la UI cuyo estado observado cambio. Si un Composable es demasiado grande, cualquier cambio de estado pequeno puede forzar la recomposicion de una porcion mayor de la interfaz de la necesaria. Dividir la UI en Composables mas pequenos y especificos permite que Compose recomponga solo lo estrictamente necesario.

### 3.3 Listas eficientes con LazyColumn
`LazyColumn` (y su equivalente `LazyRow`) solo compone y dibuja los elementos visibles en pantalla, a diferencia de un `Column` con `forEach`, que compone todos los elementos de la lista de inmediato. Para listas que pueden crecer (como una lista de tareas obtenida desde una API), `LazyColumn` es la eleccion correcta en terminos de rendimiento. El uso del parametro `key` en la funcion `items` ayuda a Compose a identificar de forma estable cada elemento, incluso cuando la lista cambia de orden o de tamano.

### 3.4 Un unico estado de UI como fuente de verdad
Consolidar todo el estado de una pantalla en una sola `data class` (como se ha hecho a lo largo del modulo con `TareasUiState`) facilita representar con un solo `when` los distintos escenarios posibles de la pantalla (cargando, con datos, con error), evitando combinaciones inconsistentes de variables de estado sueltas.

## 4. Ejemplo de codigo en Kotlin

```kotlin
data class TareasUiState(
    val tareas: List<TareaDto> = emptyList(),
    val cargando: Boolean = false,
    val mensajeError: String? = null,
    val tituloNuevaTarea: String = ""
)
```

```kotlin
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PantallaTareas(viewModel: TareasViewModel = viewModel()) {
    val estado by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        FormularioNuevaTarea(
            titulo = estado.tituloNuevaTarea,
            onTituloCambiado = viewModel::actualizarTituloNuevaTarea,
            onAgregarClick = viewModel::agregarTarea
        )

        when {
            estado.cargando -> IndicadorDeEstado(mensaje = "Cargando tareas...")
            estado.mensajeError != null -> IndicadorDeEstado(mensaje = estado.mensajeError!!)
            else -> ListaDeTareas(tareas = estado.tareas)
        }
    }
}

@Composable
private fun ListaDeTareas(tareas: List<TareaDto>) {
    // LazyColumn solo compone los elementos visibles, ideal para listas largas
    LazyColumn {
        items(tareas, key = { tarea -> tarea.id }) { tarea ->
            ItemTarea(tarea = tarea)
        }
    }
}

@Composable
private fun ItemTarea(tarea: TareaDto) {
    Text(text = if (tarea.completada) "[hecho] ${tarea.titulo}" else tarea.titulo)
}

@Composable
private fun IndicadorDeEstado(mensaje: String) {
    Column {
        CircularProgressIndicator()
        Text(text = mensaje)
    }
}

@Composable
private fun FormularioNuevaTarea(
    titulo: String,
    onTituloCambiado: (String) -> Unit,
    onAgregarClick: () -> Unit
) {
    // Aqui se colocaria un OutlinedTextField ligado a `titulo` y `onTituloCambiado`,
    // junto a un Button que invoque `onAgregarClick`.
}
```

## 5. Ejercicio practico para los estudiantes

Consolidar el segundo avance del proyecto integrador, cumpliendo los siguientes puntos:

1. Ensamblar en una sola pantalla las funcionalidades trabajadas en las sesiones 13 a 16: estado gestionado por `ViewModel`, consumo de datos desde una API con Retrofit y Coroutines, y manejo de validaciones/errores.
2. Aplicar al menos dos optimizaciones de interfaz vistas en clase: uso de `LazyColumn` con `key` para listas, y extraccion de Composables pequenos y especificos.
3. Representar los tres estados principales de la pantalla (cargando, contenido, error) mediante un unico `UiState` y un `when` claro en la capa de presentacion.
4. Revisar que no queden bloques `catch` vacios ni mensajes de error tecnicos expuestos directamente al usuario.
5. Entregar el proyecto como el segundo avance formal del portafolio, con el codigo organizado y comentado donde sea necesario.

## 6. Material de apoyo / enlaces

- Documentacion oficial de Jetpack Compose: https://developer.android.com/jetpack/compose
- Guia de rendimiento y recomposicion en Compose: https://developer.android.com/jetpack/compose/performance
- Guia de listas eficientes (LazyColumn / LazyRow): https://developer.android.com/jetpack/compose/lists
- Guia de arquitectura de aplicaciones Android: https://developer.android.com/topic/architecture
- Documentacion de Kotlin Coroutines: https://kotlinlang.org/docs/coroutines-overview.html
