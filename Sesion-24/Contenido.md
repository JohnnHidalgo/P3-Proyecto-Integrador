# Contenido de la Sesion 24 - Depuracion y resolucion de errores

## 1. Objetivo de la sesion

Que el estudiante aprenda a usar las herramientas de depuracion de Android Studio (breakpoints, Logcat, el depurador visual y el inspector de variables) para identificar y resolver errores en tiempo de ejecucion dentro de una aplicacion Android construida con Kotlin y Jetpack Compose, aplicando ese flujo de trabajo sobre el proyecto integrador que vienen desarrollando durante el curso.

## 2. Guion / desarrollo de la clase (aprox. 2 horas)

### Apertura (10 min)
- Bienvenida y encuadre: recordar que estamos en el Tema 8 (Integracion y despliegue del proyecto) y que esta sesion se enfoca en dejar el proyecto libre de errores criticos antes de documentarlo (Sesion 26) y empaquetarlo (Sesion 27).
- Pregunta disparadora al curso: "que error, bug o crash les ha costado mas tiempo resolver en su proyecto hasta ahora?". Se anotan 2 o 3 respuestas en la pizarra para retomarlas en la practica guiada.

### Exposicion de contenido (30 min)
- Tipos de errores en Android: errores de compilacion, errores de layout/composicion, excepciones en tiempo de ejecucion (crashes) y errores logicos (la app corre pero el resultado no es el esperado).
- Herramientas de Android Studio para depurar:
  - Logcat: filtros por proceso, por tag y por nivel (Verbose, Debug, Info, Warn, Error, Assert); uso de `Log.d`, `Log.e`, etc.
  - Breakpoints: breakpoint de linea, breakpoint condicional, breakpoint de excepcion (exception breakpoint) para detener la ejecucion apenas se lanza una excepcion especifica como `NullPointerException`.
  - El depurador (Debugger): panel de "Variables", panel de "Watches", pila de llamadas (call stack), botones de Step Over / Step Into / Step Out / Resume Program.
  - Particularidades de depurar composables: recomposicion, `recomposeHighlighter` (Layout Inspector), y por que un `println` a veces no basta cuando el problema es un estado que no dispara recomposicion.
- Buenas practicas: reproducir el error de forma controlada, aislar el componente/funcion sospechosa, leer el stack trace de abajo hacia arriba buscando la primera linea de codigo propio, no dejar breakpoints ni logs de depuracion en el codigo final.

### Demo en vivo (30 min)
- El docente toma la app "Task Manager" (el proyecto de gestion de tareas usado como hilo conductor del curso) y provoca dos fallas tipicas:
  1. Un `NullPointerException` al abrir el detalle de una tarea cuya lista todavia no cargo.
  2. Un estado (`mutableStateOf`) que no se actualiza en la interfaz porque se modifico una copia de la lista en vez de reemplazar el `State`.
- Se muestra paso a paso: reproducir el crash, leer el stack trace en Logcat, colocar un breakpoint condicional en la linea sospechosa, inspeccionar variables en el panel "Variables", corregir el codigo y volver a ejecutar para confirmar la solucion (ver seccion 4 para el codigo completo).

### Practica guiada (35 min)
- Los estudiantes trabajan en parejas sobre su propio proyecto integrador (o sobre el repositorio de ejemplo si el de ellos aun no tiene errores visibles).
- Consigna: cada pareja debe encontrar al menos un bug real (o inducirlo intencionalmente si no encuentran uno) y documentar en su bitacora: sintoma, hipotesis, herramienta usada para confirmarla, y la correccion aplicada.
- El docente circula por las mesas resolviendo dudas puntuales sobre Logcat, breakpoints condicionales y lectura de stack traces.

### Cierre (15 min)
- Puesta en comun: 2 o 3 parejas comparten brevemente el bug que encontraron y como lo resolvieron.
- Se explica la tarea para despues de la clase (seccion 5) y se enlaza con la Sesion 25 (refactorizacion), aclarando que primero se corrige y luego se mejora la estructura del codigo.

## 3. Explicacion teorica breve

La depuracion (debugging) es el proceso sistematico de localizar y corregir defectos en el software. En Android Studio, el flujo tipico combina tres herramientas:

- **Logcat**: es la consola de registro del sistema Android. Cada proceso, y en particular cada excepcion no capturada, escribe ahi su informacion. Filtrar por el nombre del paquete de la app y por nivel `Error` es el primer paso frente a un crash. El stack trace muestra, de arriba hacia abajo, la secuencia de llamadas que terminaron en la excepcion; conviene leerlo buscando la primera linea que pertenezca al codigo propio (no a librerias del framework).
- **Breakpoints**: un breakpoint detiene la ejecucion en una linea especifica para poder inspeccionar el estado del programa en ese instante. Android Studio permite:
  - Breakpoints de linea (clic en el margen izquierdo del editor).
  - Breakpoints condicionales (se activan solo si una expresion booleana es verdadera, por ejemplo `tarea == null`).
  - Breakpoints de excepcion (se configuran en `Run > View Breakpoints`, y detienen la app en el instante exacto en que se lanza, por ejemplo, un `NullPointerException`, aunque este ocurra dentro de una libreria).
- **El depurador visual**: una vez detenida la ejecucion en un breakpoint, el panel "Variables" muestra el valor de cada variable en ese punto del programa, el panel "Watches" permite agregar expresiones propias a vigilar, y la pila de llamadas (call stack) muestra la cadena de funciones que llevo hasta ese punto. Los controles Step Over (ejecuta la linea actual sin entrar en las funciones que llama), Step Into (entra a la funcion llamada) y Step Out (termina la funcion actual y vuelve al llamador) permiten avanzar la ejecucion de forma controlada.

En aplicaciones con Jetpack Compose existe una dificultad adicional: los errores de estado no siempre generan una excepcion, sino un comportamiento visual incorrecto (la pantalla "no se actualiza"). Esto suele deberse a que se modifico una coleccion mutable en el lugar (in place) en vez de crear una nueva instancia y asignarla al `State`, por lo que Compose no detecta el cambio y no recompone. Para estos casos, ademas del depurador, es util el Layout Inspector (para ver el arbol de composicion actual) y agregar logs puntuales en los `LaunchedEffect` o en los `ViewModel` para confirmar cuando efectivamente se dispara una actualizacion de estado.

## 4. Ejemplo de codigo en Kotlin

A continuacion, un fragmento simplificado de la app "Task Manager" con dos errores tipicos, seguido de la version corregida.

### Version con errores

```kotlin
data class Tarea(
    val id: Int,
    val titulo: String,
    val completada: Boolean = false
)

class TareasViewModel : ViewModel() {

    // Error 1: la lista se inicializa vacia y se carga de forma asincrona,
    // pero la pantalla de detalle asume que siempre hay datos.
    private val _tareas = mutableStateOf<List<Tarea>>(emptyList())
    val tareas: State<List<Tarea>> = _tareas

    fun cargarTareas() {
        viewModelScope.launch {
            val resultado = repositorio.obtenerTareas() // operacion asincrona
            _tareas.value = resultado
        }
    }

    // Error 2: se modifica la lista "en el lugar" con toMutableList()
    // y luego se reasigna la MISMA referencia interna sin que Compose
    // detecte realmente un cambio de identidad en algunos casos de uso.
    fun marcarComoCompletada(id: Int) {
        val listaActual = _tareas.value as MutableList<Tarea>
        val index = listaActual.indexOfFirst { it.id == id }
        listaActual[index] = listaActual[index].copy(completada = true)
        // _tareas.value nunca se reasigna: Compose no recompone.
    }
}

@Composable
fun DetalleTareaScreen(id: Int, viewModel: TareasViewModel) {
    // Error 1 en accion: si la lista aun esta vacia (cargando),
    // first { it.id == id } lanza NoSuchElementException,
    // y al usar !! sobre un resultado nulo se produce NullPointerException.
    val tarea = viewModel.tareas.value.first { it.id == id }
    Text(text = tarea.titulo)
}
```

Al ejecutar la app y navegar rapidamente al detalle de una tarea antes de que termine `cargarTareas()`, Logcat muestra algo como:

```
FATAL EXCEPTION: main
java.util.NoSuchElementException: Collection contains no element matching the predicate.
    at com.upb.taskmanager.ui.DetalleTareaScreenKt.DetalleTareaScreen(DetalleTareaScreen.kt:42)
```

Con un breakpoint condicional en la linea de `first { it.id == id }` (condicion: `viewModel.tareas.value.isEmpty()`), se confirma la hipotesis: la pantalla de detalle se dibuja antes de que la lista tenga datos.

### Version corregida

```kotlin
class TareasViewModel : ViewModel() {

    private val _tareas = mutableStateOf<List<Tarea>>(emptyList())
    val tareas: State<List<Tarea>> = _tareas

    private val _cargando = mutableStateOf(true)
    val cargando: State<Boolean> = _cargando

    fun cargarTareas() {
        viewModelScope.launch {
            _cargando.value = true
            _tareas.value = repositorio.obtenerTareas()
            _cargando.value = false
        }
    }

    // Correccion: se crea una NUEVA lista inmutable y se reasigna
    // por completo el valor del State, para que Compose detecte el cambio.
    fun marcarComoCompletada(id: Int) {
        _tareas.value = _tareas.value.map { tarea ->
            if (tarea.id == id) tarea.copy(completada = true) else tarea
        }
    }
}

@Composable
fun DetalleTareaScreen(id: Int, viewModel: TareasViewModel) {
    val cargando by viewModel.cargando
    val tarea = viewModel.tareas.value.find { it.id == id }

    when {
        cargando -> CircularProgressIndicator()
        tarea == null -> Text("No se encontro la tarea solicitada")
        else -> Text(text = tarea.titulo)
    }
}
```

La correccion aplica dos principios: (1) exponer un estado de carga explicito para que la interfaz nunca asuma datos que todavia no llegaron, y (2) reemplazar el `State` con una lista nueva (`map` en vez de mutar en el lugar) para que Compose detecte el cambio y recomponga correctamente.

## 5. Ejercicio practico para los estudiantes

Sobre el proyecto integrador que cada estudiante o equipo viene desarrollando:

1. Ejecutar la app en modo debug y navegar por todas las pantallas principales buscando crashes o comportamientos incorrectos (estados que no se actualizan, listas vacias que no deberian estarlo, botones que no responden).
2. Elegir al menos dos incidencias reales (si no aparecen espontaneamente, provocarlas con datos limite: listas vacias, valores nulos, rotacion de pantalla, doble clic rapido en un boton).
3. Para cada incidencia, documentar en un archivo `bitacora-depuracion.md` dentro del repositorio del proyecto:
   - Sintoma observado y pasos para reproducirlo.
   - Fragmento relevante del stack trace de Logcat (si aplica).
   - Herramienta usada para diagnosticar (breakpoint de linea, breakpoint condicional, breakpoint de excepcion, inspeccion de variables, Layout Inspector).
   - Hipotesis de la causa y correccion aplicada (con el fragmento de codigo antes/despues).
4. Confirmar que, tras la correccion, el error ya no se reproduce siguiendo los mismos pasos.
5. Subir los cambios al repositorio con un mensaje de commit que describa el bug corregido (por ejemplo: "fix: corrige NPE en DetalleTareaScreen cuando la lista aun esta cargando").

Esta actividad corresponde a la seccion "Despues de la clase" del silabo: corregir incidencias detectadas en el proyecto.

## 6. Material de apoyo / enlaces

- Depuracion de apps en Android Studio: https://developer.android.com/studio/debug
- Uso de Logcat: https://developer.android.com/studio/debug/logcat
- Layout Inspector (inspeccionar la jerarquia de Compose en ejecucion): https://developer.android.com/studio/debug/layout-inspector
- Documentacion oficial de Jetpack Compose: https://developer.android.com/jetpack/compose
- Guia de Kotlin sobre manejo de nulabilidad (para evitar `NullPointerException`): https://kotlinlang.org/docs/null-safety.html
