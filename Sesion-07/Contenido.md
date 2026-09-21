# Contenido de la sesion - Sesion 07: Layouts y estado de la interfaz

**Tema del curso:** Tema 3 - Interfaces de usuario con Jetpack Compose

---

## 1. Objetivo de la sesion

Que el estudiante comprenda y aplique los layouts basicos de Jetpack Compose (`Row`, `Column`, `Box`, `LazyColumn`) junto con el manejo de estado (`State`, `remember`, `mutableStateOf`) para construir interfaces dinamicas, tomando como caso de estudio una lista de tareas (Task Manager) que reacciona a los cambios del usuario.

---

## 2. Guion / desarrollo de la clase

Duracion total estimada: 120 minutos.

### Bloque 1 - Apertura y repaso (10 min)
- Bienvenida y verificacion de asistencia.
- Repaso rapido de la clase anterior sobre composables basicos (`Text`, `Button`, `Image`, modificadores).
- Pregunta disparadora: "Si tuviera que dibujar en un papel la pantalla principal de una app de tareas, que bloques dibujaria?" Se recogen 2-3 respuestas para conectar con layouts.
- Se presenta la agenda de la sesion y el objetivo.

### Bloque 2 - Exposicion de contenido: Layouts (25 min)
- Explicacion de los contenedores basicos de Compose:
  - `Column`: apila elementos verticalmente.
  - `Row`: apila elementos horizontalmente.
  - `Box`: superpone elementos (util para overlays, badges, indicadores).
- Modificadores clave: `Modifier.fillMaxWidth()`, `Modifier.padding()`, `Modifier.weight()`, `Modifier.align()`.
- Comparacion visual (pizarra o diapositiva) de como se organiza una tarjeta de tarea usando `Row` + `Column` anidados.
- Se introduce `LazyColumn` como la version "eficiente" de una lista vertical, contrastando con usar un `Column` con scroll manual.

### Bloque 3 - Demo en vivo: armando la lista de tareas (30 min)
- El docente crea en vivo (o continua) el proyecto "Task Manager".
- Paso 1: modelo de datos simple `data class Tarea(val id: Int, val titulo: String, val completada: Boolean)`.
- Paso 2: composable `TareaItem` que muestra una fila con checkbox + texto, usando `Row`.
- Paso 3: composable `ListaDeTareas` que usa `LazyColumn` con `items()` para renderizar una lista de `Tarea`.
- Paso 4: se introduce el estado con `remember { mutableStateOf(...) }` para marcar una tarea como completada al tocar el checkbox, y se observa la recomposicion en caliente (Live Edit / re-ejecucion).
- Se enfatiza el concepto de "estado como fuente de verdad" y la recomposicion automatica cuando el estado cambia.

### Bloque 4 - Practica guiada (35 min)
- Los estudiantes, en pareja o individualmente, extienden el proyecto demo:
  - Agregar un campo de texto (`TextField`) y un boton para anadir nuevas tareas a la lista (usando `mutableStateListOf` o una lista dentro de un `State`).
  - Agregar un contador en la parte superior (`Box` con `Text`) que muestre "X de Y tareas completadas", derivado del estado.
- El docente circula resolviendo dudas y revisando avances.
- Se sugiere probar con `Box` para mostrar un mensaje "No hay tareas" cuando la lista esta vacia (superponiendo un texto centrado sobre el area de la lista).

### Bloque 5 - Cierre y siguientes pasos (10 min)
- Puesta en comun: 1 o 2 estudiantes muestran su avance.
- Se resumen los conceptos clave: `Row`/`Column`/`Box` para estructura, `LazyColumn` para listas eficientes, `State`/`remember` para reactividad.
- Se explica la tarea para despues de la clase (formularios y listas dinamicas) y se deja el enlace al material de apoyo.

---

## 3. Explicacion teorica breve

**Layouts en Jetpack Compose.** Compose reemplaza los `LinearLayout`/`ConstraintLayout` de XML por composables declarativos:
- `Column` organiza a sus hijos en el eje vertical, uno debajo del otro.
- `Row` organiza a sus hijos en el eje horizontal, uno junto al otro.
- `Box` superpone a sus hijos en el mismo espacio, permitiendo alinearlos (`Alignment.Center`, `Alignment.TopEnd`, etc.), lo que resulta util para overlays, indicadores de carga o insignias (badges).

Estos tres layouts se pueden anidar libremente para construir estructuras complejas (por ejemplo, una `Column` que contiene varias `Row`, cada una representando una tarjeta de tarea).

**Listas eficientes con `LazyColumn`.** A diferencia de un `Column` normal (que compone todos sus hijos de una sola vez, sin importar si son visibles), `LazyColumn` solo compone y dibuja los elementos que estan (o estaran pronto) visibles en pantalla. Esto es equivalente conceptualmente a un `RecyclerView` en el mundo de Vistas tradicionales, pero sin necesidad de `Adapter` ni `ViewHolder`: basta con describir como se ve un item y Compose se encarga del reciclaje interno.

**Estado (`State`) en Compose.** Una interfaz declarativa se "re-dibuja" (recompone) cuando cambia el estado que lee. Los elementos clave son:
- `mutableStateOf(valorInicial)`: crea un contenedor observable de estado.
- `remember { ... }`: le indica a Compose que recuerde ese valor entre recomposiciones (si no se usa, el valor se reiniciaria cada vez que la funcion se vuelve a ejecutar).
- Cuando el valor de un `State` cambia, Compose marca como "sucios" (invalidados) unicamente los composables que efectivamente leen ese valor, y los vuelve a ejecutar (recomposicion), sin necesidad de manipular vistas manualmente.

Este modelo mental ("la UI es una funcion del estado") es la base de todo lo que se construira en las siguientes sesiones, incluyendo la arquitectura MVVM de la Sesion 12.

---

## 4. Ejemplo de codigo en Kotlin

```kotlin
// Modelo de datos simple para el proyecto "Task Manager"
data class Tarea(
    val id: Int,
    val titulo: String,
    val completada: Boolean = false
)

@Composable
fun ListaDeTareas() {
    // Estado que sobrevive a las recomposiciones: la lista de tareas
    val tareas = remember {
        mutableStateListOf(
            Tarea(1, "Repasar Jetpack Compose"),
            Tarea(2, "Practicar LazyColumn"),
            Tarea(3, "Subir avance al repositorio")
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Contador de tareas completadas, derivado del estado actual
        val completadas = tareas.count { it.completada }
        Text(
            text = "$completadas de ${tareas.size} tareas completadas",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.weight(1f)) {
            if (tareas.isEmpty()) {
                Text(
                    text = "No hay tareas pendientes",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn {
                    items(tareas, key = { it.id }) { tarea ->
                        TareaItem(
                            tarea = tarea,
                            onToggle = { id ->
                                val index = tareas.indexOfFirst { it.id == id }
                                if (index != -1) {
                                    val actual = tareas[index]
                                    tareas[index] = actual.copy(completada = !actual.completada)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TareaItem(tarea: Tarea, onToggle: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = tarea.completada,
            onCheckedChange = { onToggle(tarea.id) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = tarea.titulo)
    }
}
```

Puntos a resaltar en clase:
- `mutableStateListOf` permite trabajar con una lista observable directamente, sin recrear listas inmutables en cada cambio.
- El `key = { it.id }` en `items()` ayuda a Compose a identificar cada elemento de forma estable, mejorando el rendimiento y evitando animaciones/recomposiciones erroneas.
- `Box(modifier = Modifier.weight(1f))` dentro de una `Column` permite que la lista ocupe todo el espacio restante disponible.

---

## 5. Ejercicio practico para los estudiantes

Sobre el proyecto "Task Manager" (o el proyecto propio si ya iniciaron uno distinto):

1. Agregar un `TextField` y un `Button` en la parte superior de la pantalla que permitan escribir el titulo de una nueva tarea y anadirla a la lista al presionar el boton.
2. Validar que no se agreguen tareas con titulo vacio (mostrar un mensaje de error simple usando estado, por ejemplo un `Text` en color rojo).
3. Agregar la posibilidad de eliminar una tarea de la lista (por ejemplo con un icono o boton "Eliminar" en cada `TareaItem`).
4. Mostrar, usando un `Box` superpuesto, un mensaje "No hay tareas pendientes" cuando la lista este vacia, y ocultarlo automaticamente cuando se agregue la primera tarea.
5. Subir el avance al repositorio de trabajo (se retomara en la Sesion 9 al trabajar con ramas de Git).

Este ejercicio corresponde a la actividad de "Despues de la clase": implementar formularios y listas dinamicas.

---

## 6. Material de apoyo / enlaces

- Documentacion general de Kotlin: https://kotlinlang.org/docs/home.html
- Documentacion oficial de Android: https://developer.android.com/docs
- Jetpack Compose (guia general): https://developer.android.com/jetpack/compose
- Estado en Compose (`State` y `remember`): https://developer.android.com/jetpack/compose/state
- Listas y grillas con `LazyColumn`/`LazyRow`: https://developer.android.com/jetpack/compose/lists
- Layouts basicos (`Row`, `Column`, `Box`): https://developer.android.com/jetpack/compose/layouts/basics
