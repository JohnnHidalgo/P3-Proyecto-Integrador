# Contenido de la Sesion 15 - Retrofit y Coroutines

**Tema del curso:** Tema 6 - Comunicacion e integracion de datos

## 1. Objetivo de la sesion

Que el estudiante sea capaz de consumir un servicio REST de forma asincrona utilizando Retrofit junto con Kotlin Coroutines, integrando la llamada de red dentro del `ViewModel` para actualizar el estado de la interfaz de usuario.

## 2. Guion / desarrollo de la clase (aprox. 2 horas)

### Bloque 1 - Apertura (10 min)
- Bienvenida y recuperacion de contexto: se recuerda que en la Sesion 14 se configuro el cliente Retrofit y la interfaz de servicio, pero aun no se realizo ninguna llamada real.
- Pregunta disparadora: "Que pasaria con la interfaz de la aplicacion si la llamada de red se hiciera en el hilo principal?"
- Se plantea el objetivo: ejecutar llamadas de red de forma asincrona sin bloquear la UI.

### Bloque 2 - Exposicion de contenido: Coroutines (25 min)
- Repaso de por que Android no permite hacer operaciones de red en el hilo principal (Main thread / UI thread).
- Concepto de corrutina como una forma ligera de programacion asincrona y concurrente en Kotlin.
- `suspend fun`: funciones que pueden pausarse y reanudarse sin bloquear el hilo que las invoca.
- Dispatchers relevantes: `Dispatchers.Main`, `Dispatchers.IO`, `Dispatchers.Default`, y por que las llamadas de red se asocian normalmente a `Dispatchers.IO` (aunque Retrofit con soporte de corrutinas ya gestiona esto internamente).
- Relacion entre `viewModelScope` (visto en la Sesion 13) y el lanzamiento de corrutinas para llamadas de red.

### Bloque 3 - Exposicion de contenido: Retrofit + Coroutines (20 min)
- Como declarar funciones `suspend` en una interfaz de servicio de Retrofit en lugar de usar `Call<T>`.
- Ventajas: codigo mas lineal y legible, sin necesidad de callbacks anidados (`enqueue`, `onResponse`, `onFailure`).
- Uso de `Response<T>` cuando se necesita inspeccionar el codigo de estado HTTP ademas del cuerpo de la respuesta.

### Bloque 4 - Demostracion en vivo (35 min)
- El docente modifica la interfaz `TareaApiService` de la Sesion 14 para que el metodo `obtenerTareas()` sea una funcion `suspend`.
- Se actualiza el `TareasViewModel` (de la Sesion 13) agregando una funcion `cargarTareas()` que lanza una corrutina en `viewModelScope` y llama al servicio.
- Se muestra como actualizar el `StateFlow` con el resultado de la llamada, incluyendo un indicador de carga (`cargando = true/false`).
- Se ejecuta la aplicacion contra la API de practica (JSONPlaceholder) y se observa la lista de tareas llegando desde la red y reflejandose en la UI mediante Compose.

### Bloque 5 - Practica guiada (25 min)
- Los estudiantes implementan la version `suspend` de su propio servicio Retrofit.
- Agregan al `ViewModel` de su proyecto una funcion que llame al servicio dentro de `viewModelScope.launch` y actualice el estado observable.
- Prueban la integracion ejecutando la aplicacion y verificando que los datos remotos aparecen en pantalla.

### Bloque 6 - Cierre (5 min)
- Se resumen los conceptos: corrutinas, funciones `suspend`, `viewModelScope` y su combinacion con Retrofit.
- Se explica la actividad para despues de la clase.
- Se adelanta que en la Sesion 16 se agregaran validaciones y manejo de errores robusto sobre esta misma llamada de red.

## 3. Explicacion teorica breve

### 3.1 Por que corrutinas para llamadas de red
Una llamada de red puede tardar un tiempo indeterminado en completarse. Si se ejecutara de forma sincrona en el hilo principal de Android, la interfaz se congelaria y el sistema podria mostrar el dialogo de "aplicacion no responde" (ANR). Las corrutinas de Kotlin permiten expresar esta espera de forma asincrona pero con una sintaxis secuencial y legible, evitando el anidamiento de callbacks tipico de otras aproximaciones.

### 3.2 Funciones suspend
Una funcion marcada con `suspend` puede pausar su ejecucion sin bloquear el hilo, y solo puede ser invocada desde otra funcion `suspend` o desde un `CoroutineScope` (como `viewModelScope`). Retrofit, desde sus versiones mas recientes, soporta de forma nativa que los metodos de una interfaz de servicio sean funciones `suspend`, lo cual elimina la necesidad de manejar manualmente hilos o callbacks.

### 3.3 viewModelScope y el flujo de datos
Cuando el `ViewModel` lanza una corrutina con `viewModelScope.launch`, esa corrutina esta atada al ciclo de vida del `ViewModel`: si la pantalla se destruye definitivamente, la corrutina se cancela automaticamente, evitando actualizaciones de estado sobre un `ViewModel` que ya no existe. Dentro de esa corrutina, se invoca la funcion `suspend` del servicio Retrofit y, al recibir la respuesta, se actualiza el `StateFlow` que la UI observa.

### 3.4 Response<T> frente a un valor directo
Cuando la funcion `suspend` del servicio devuelve directamente el tipo de dato esperado (por ejemplo, `List<TareaDto>`), Retrofit lanza una excepcion si la respuesta no es exitosa. Cuando se necesita inspeccionar el codigo de estado HTTP explicitamente, conviene declarar el retorno como `Response<List<TareaDto>>`, revisando `response.isSuccessful` antes de leer `response.body()`. Esta distincion sera clave en la Sesion 16 para el manejo de errores.

## 4. Ejemplo de codigo en Kotlin

```kotlin
import retrofit2.http.GET

// La interfaz de servicio ahora usa una funcion suspend en lugar de Call<T>
interface TareaApiService {

    @GET("tareas")
    suspend fun obtenerTareas(): List<TareaDto>
}
```

```kotlin
data class TareaDto(
    val id: Int,
    val titulo: String,
    val completada: Boolean
)

data class TareasUiState(
    val tareas: List<TareaDto> = emptyList(),
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

class TareasViewModel(
    private val apiService: TareaApiService = RetrofitClient.tareaApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(TareasUiState())
    val uiState: StateFlow<TareasUiState> = _uiState.asStateFlow()

    init {
        cargarTareas()
    }

    fun cargarTareas() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(cargando = true)

            // Llamada asincrona a la API gracias a la funcion suspend
            val tareasRemotas = apiService.obtenerTareas()

            _uiState.value = _uiState.value.copy(
                tareas = tareasRemotas,
                cargando = false
            )
        }
    }
}
```

```kotlin
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PantallaTareas(viewModel: TareasViewModel = viewModel()) {
    val estado by viewModel.uiState.collectAsState()

    Column {
        if (estado.cargando) {
            CircularProgressIndicator()
        } else {
            estado.tareas.forEach { tarea ->
                Text(text = tarea.titulo)
            }
        }
    }
}
```

## 5. Ejercicio practico para los estudiantes

Integrar en el proyecto los datos obtenidos desde la API, cumpliendo los siguientes puntos:

1. Convertir el metodo del servicio Retrofit del proyecto integrador en una funcion `suspend`.
2. Agregar al `ViewModel` correspondiente una funcion que lance una corrutina en `viewModelScope` y llame al servicio.
3. Actualizar el estado observable (`StateFlow`) con los datos recibidos, incluyendo un indicador visual de carga mientras la peticion esta en curso.
4. Mostrar en la interfaz de Compose los datos reales obtenidos desde la red (no datos de prueba locales).
5. Verificar el comportamiento ejecutando la aplicacion en el emulador con conexion a internet activa, confirmando que la lista se llena con datos remotos.

Esta integracion es la base sobre la cual, en la Sesion 16, se agregara manejo de errores para los casos en que la red falle o la respuesta no sea la esperada.

## 6. Material de apoyo / enlaces

- Documentacion oficial de Kotlin Coroutines: https://kotlinlang.org/docs/coroutines-overview.html
- Guia de corrutinas en Android: https://developer.android.com/kotlin/coroutines
- Soporte de corrutinas en Retrofit (repositorio oficial): https://square.github.io/retrofit/
- Guia de ViewModel y viewModelScope: https://developer.android.com/topic/libraries/architecture/viewmodel
- API publica de practica JSONPlaceholder: https://jsonplaceholder.typicode.com
