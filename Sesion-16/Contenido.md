# Contenido de la Sesion 16 - Validaciones y manejo de errores

**Tema del curso:** Tema 6 - Comunicacion e integracion de datos

## 1. Objetivo de la sesion

Que el estudiante sea capaz de aplicar validaciones de datos y un manejo de errores robusto (excepciones de red, respuestas HTTP no exitosas y datos invalidos) en una aplicacion Android que consume servicios REST, siguiendo buenas practicas de programacion defensiva.

## 2. Guion / desarrollo de la clase (aprox. 2 horas)

### Bloque 1 - Apertura (10 min)
- Bienvenida y recuperacion de contexto: se recuerda que en la Sesion 15 se logro consumir la API de tareas de forma asincrona, pero sin manejar que pasa si la red falla.
- Se simula en vivo un error (por ejemplo, desconectando el emulador de internet) para mostrar que la aplicacion actual se "cae" (excepcion no controlada) o se queda cargando indefinidamente.
- Se plantea el objetivo: hacer que la aplicacion responda de forma controlada ante cualquier escenario de error.

### Bloque 2 - Exposicion de contenido: tipos de error (25 min)
- Errores de red: sin conexion, tiempo de espera agotado (`timeout`), servidor inaccesible. Se representan tipicamente con excepciones como `IOException`.
- Errores de servidor: respuestas HTTP no exitosas (4xx, 5xx) que Retrofit no convierte automaticamente en excepcion cuando se usa `Response<T>`.
- Errores de datos: JSON malformado, campos nulos inesperados, datos que no cumplen las reglas de negocio (por ejemplo, una tarea con titulo vacio).
- Errores de validacion de entrada del usuario: formularios con campos vacios, formatos incorrectos, valores fuera de rango.

### Bloque 3 - Exposicion de contenido: manejo estructurado de errores en Kotlin (25 min)
- Uso de bloques `try/catch` alrededor de las llamadas `suspend` a Retrofit.
- Patron de resultado tipado con `sealed class` (por ejemplo `Resultado.Exito`, `Resultado.Error`) como alternativa mas expresiva que devolver `null` o lanzar excepciones hacia la UI.
- Buenas practicas: no capturar excepciones genericas sin registrarlas o informarlas, no exponer mensajes tecnicos crudos al usuario final, centralizar la logica de validacion.

### Bloque 4 - Demostracion en vivo (30 min)
- El docente define una `sealed class Resultado<out T>` con variantes `Exito` y `Error`.
- Se envuelve la llamada `apiService.obtenerTareas()` del `TareasViewModel` (Sesion 15) dentro de un `try/catch`, capturando `IOException` (problema de red) y una excepcion generica como respaldo.
- Se agrega una funcion de validacion `validarTitulo(titulo: String)` que se usa antes de agregar una nueva tarea, evitando titulos vacios o demasiado largos.
- Se actualiza `TareasUiState` para incluir un campo `mensajeError: String?` que la UI puede mostrar (por ejemplo, en un `Snackbar` o un `Text` de error).

### Bloque 5 - Practica guiada (25 min)
- Los estudiantes envuelven la llamada de red de su propio proyecto en `try/catch`, distinguiendo al menos entre error de red y error inesperado.
- Agregan al menos una validacion de entrada de usuario (por ejemplo, un campo de texto que no puede estar vacio) antes de enviar datos al `ViewModel`.
- Prueban ambos escenarios: desconectando la red y enviando datos invalidos, verificando que la aplicacion muestra un mensaje adecuado en lugar de fallar.

### Bloque 6 - Cierre (5 min)
- Se resumen los tipos de error cubiertos y el patron `sealed class` para resultados.
- Se explica la actividad para despues de la clase.
- Se adelanta que en la Sesion 17 se integrara todo lo construido hasta ahora y se optimizara la interfaz resultante.

## 3. Explicacion teorica breve

### 3.1 Excepciones en Kotlin
Kotlin usa el mecanismo estandar de excepciones de la JVM (`try`, `catch`, `finally`, `throw`). En el contexto de llamadas de red con corrutinas, las excepciones lanzadas dentro de una funcion `suspend` se propagan igual que en codigo sincrono, por lo que un bloque `try/catch` alrededor de la llamada a Retrofit es suficiente para interceptarlas, sin necesidad de mecanismos especiales adicionales para el caso basico.

### 3.2 Sealed class para representar resultados
Una `sealed class` permite modelar de forma exhaustiva y segura en tiempo de compilacion los distintos resultados posibles de una operacion: exito con datos, error con un mensaje, o incluso un estado de carga. Esto evita el uso de valores `null` ambiguos y obliga, mediante `when`, a manejar todos los casos posibles al consumir el resultado, lo cual reduce errores por omision.

### 3.3 Validacion de datos de entrada
La validacion de datos de entrada (ya sea del usuario o de una respuesta externa) debe hacerse lo mas cerca posible del punto donde el dato ingresa al sistema, idealmente antes de que llegue a la capa de negocio. Las validaciones comunes incluyen: campos no vacios, longitudes minimas y maximas, formatos (correo electronico, numeros), y rangos de valores aceptables.

### 3.4 Buenas practicas de manejo de errores en Android
- Nunca dejar un bloque `catch` vacio: como minimo, registrar el error (`log`) o transformarlo en un estado que la UI pueda mostrar.
- Distinguir errores recuperables (el usuario puede reintentar) de errores no recuperables.
- Mostrar mensajes de error claros y en el idioma del usuario, evitando exponer trazas tecnicas (stack traces) en la interfaz.
- Mantener el estado de error como parte del estado de la UI (por ejemplo, en el `StateFlow` del `ViewModel`), para que la vista pueda reaccionar de forma declarativa.

## 4. Ejemplo de codigo en Kotlin

```kotlin
// Patron de resultado tipado para representar exito o error de forma explicita
sealed class Resultado<out T> {
    data class Exito<T>(val datos: T) : Resultado<T>()
    data class Error(val mensaje: String) : Resultado<Nothing>()
}
```

```kotlin
import java.io.IOException

// Funcion de validacion simple para el titulo de una tarea
fun validarTitulo(titulo: String): Resultado<String> {
    return when {
        titulo.isBlank() -> Resultado.Error("El titulo no puede estar vacio")
        titulo.length > 60 -> Resultado.Error("El titulo no puede superar 60 caracteres")
        else -> Resultado.Exito(titulo.trim())
    }
}
```

```kotlin
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

data class TareasUiState(
    val tareas: List<TareaDto> = emptyList(),
    val cargando: Boolean = false,
    val mensajeError: String? = null
)

class TareasViewModel(
    private val apiService: TareaApiService = RetrofitClient.tareaApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(TareasUiState())
    val uiState: StateFlow<TareasUiState> = _uiState.asStateFlow()

    fun cargarTareas() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(cargando = true, mensajeError = null)

            try {
                val tareasRemotas = apiService.obtenerTareas()
                _uiState.value = _uiState.value.copy(
                    tareas = tareasRemotas,
                    cargando = false
                )
            } catch (sinConexion: IOException) {
                _uiState.value = _uiState.value.copy(
                    cargando = false,
                    mensajeError = "No se pudo conectar con el servidor. Verifica tu conexion a internet."
                )
            } catch (errorInesperado: Exception) {
                _uiState.value = _uiState.value.copy(
                    cargando = false,
                    mensajeError = "Ocurrio un error inesperado al cargar las tareas."
                )
            }
        }
    }

    fun agregarTarea(tituloIngresado: String) {
        val resultadoValidacion = validarTitulo(tituloIngresado)

        when (resultadoValidacion) {
            is Resultado.Error -> {
                _uiState.value = _uiState.value.copy(mensajeError = resultadoValidacion.mensaje)
            }
            is Resultado.Exito -> {
                // Aqui se procederia a agregar la tarea validada,
                // por ejemplo llamando a un endpoint POST del servicio.
            }
        }
    }
}
```

## 5. Ejercicio practico para los estudiantes

Implementar validaciones y manejo de errores en el proyecto integrador, cumpliendo los siguientes puntos:

1. Envolver la llamada de red principal del proyecto en un bloque `try/catch`, distinguiendo al menos un error de conectividad (`IOException`) de un error generico.
2. Agregar un campo de error (por ejemplo `mensajeError: String?`) al estado de la pantalla y mostrarlo en la interfaz cuando corresponda (texto visible o `Snackbar`).
3. Implementar al menos una funcion de validacion de datos de entrada del usuario (por ejemplo, un formulario de creacion de un elemento), que impida enviar datos invalidos o vacios.
4. Utilizar una `sealed class` de tipo `Resultado` (o equivalente) para representar el exito o el fracaso de al menos una operacion del proyecto.
5. Probar el comportamiento de la aplicacion en al menos tres escenarios: sin conexion a internet, con una respuesta de error del servidor (o simulada) y con datos de entrada invalidos.

## 6. Material de apoyo / enlaces

- Guia de manejo de excepciones en Kotlin: https://kotlinlang.org/docs/exceptions.html
- Documentacion de Kotlin Coroutines (manejo de excepciones en corrutinas): https://kotlinlang.org/docs/exception-handling.html
- Documentacion oficial de Retrofit (manejo de respuestas y errores): https://square.github.io/retrofit/
- Guia de arquitectura de la capa de datos en Android: https://developer.android.com/topic/architecture/data-layer
- Guia de pruebas y manejo de estados en Compose: https://developer.android.com/jetpack/compose/state
