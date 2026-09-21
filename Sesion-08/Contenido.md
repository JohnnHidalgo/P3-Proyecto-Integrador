# Contenido de la sesion - Sesion 08: Navigation Compose

**Tema del curso:** Tema 4 - Navegacion y ciclo de vida de la aplicacion

---

## 1. Objetivo de la sesion

Que el estudiante implemente navegacion entre pantallas usando Navigation Compose, incluyendo el paso de parametros entre ellas, extendiendo el proyecto "Task Manager" para navegar desde una lista de tareas hacia el detalle de una tarea especifica.

---

## 2. Guion / desarrollo de la clase

Duracion total estimada: 120 minutos.

### Bloque 1 - Apertura y repaso (10 min)
- Repaso breve de la sesion anterior: layouts (`Row`, `Column`, `Box`, `LazyColumn`) y estado.
- Pregunta disparadora: "En la app de tareas que armamos, que pasaria si quisieramos ver el detalle completo de una tarea al tocarla?" Se conecta con la necesidad de multiples pantallas.
- Presentacion de la agenda y objetivo de la sesion.

### Bloque 2 - Exposicion de contenido: conceptos de Navigation Compose (25 min)
- Que problema resuelve la navegacion: pasar de tener "una sola pantalla con estado condicional" a tener pantallas independientes, cada una con su propia responsabilidad.
- Elementos centrales de Navigation Compose:
  - `NavController`: objeto que controla la pila de navegacion (back stack).
  - `NavHost`: composable contenedor que define el grafo de navegacion y el destino inicial.
  - `composable(route)`: define cada pantalla (destino) dentro del grafo.
  - Rutas con argumentos: `"detalle/{tareaId}"` y como se declaran los `navArgument`.
- Diferencia entre navegar hacia adelante (`navController.navigate(...)`) y volver atras (`navController.popBackStack()` o el boton back del sistema).

### Bloque 3 - Demo en vivo: navegando en Task Manager (30 min)
- El docente parte del proyecto de la Sesion 7 (lista de tareas) y agrega:
  - Una pantalla `PantallaListaTareas` (la lista existente).
  - Una pantalla `PantallaDetalleTarea` que recibe el `id` de la tarea.
  - Un `NavHost` con dos rutas: `"lista"` y `"detalle/{tareaId}"`.
  - El evento de click sobre un `TareaItem` que llama a `navController.navigate("detalle/${tarea.id}")`.
- Se muestra en vivo como el back stack permite volver a la lista con el boton atras del dispositivo/emulador, conservando el estado previo.
- Se discute brevemente donde conviene guardar el estado de la lista para que no se pierda al navegar (introduccion conceptual, sin profundizar todavia en `ViewModel`, que se vera en la Sesion 12).

### Bloque 4 - Practica guiada (35 min)
- Los estudiantes replican la navegacion lista-detalle en su propio proyecto Task Manager.
- Se pide adicionalmente:
  - Agregar un boton "Volver" explicito en la pantalla de detalle, ademas del gesto/boton del sistema.
  - Pasar mas de un parametro (por ejemplo, `tareaId` y un flag `origen` que indique desde que pantalla se navego), practicando el uso de multiples `navArgument`.
- El docente recorre los puestos resolviendo errores comunes: rutas mal formadas, argumentos no declarados, olvido de convertir el argumento de `String` a `Int`.

### Bloque 5 - Cierre y siguientes pasos (10 min)
- Puesta en comun de 1-2 casos.
- Resumen de conceptos: `NavController`, `NavHost`, rutas con argumentos, back stack.
- Se explica la tarea de "Despues de la clase" (integrar la navegacion en el proyecto propio) y se comparte el material de apoyo.

---

## 3. Explicacion teorica breve

**Por que navegacion y no solo estado condicional.** Es posible simular "pantallas" cambiando un estado booleano o un enum y mostrando un composable u otro con un `if`/`when`. Sin embargo, esto no escala: se pierde el manejo automatico del boton atras, no hay un historial (back stack) claro, y pasar datos entre "pantallas" se vuelve dificil de mantener. Navigation Compose resuelve esto de forma declarativa y estandarizada.

**Piezas principales:**
- **`NavController`**: es el objeto central que sabe en que pantalla esta el usuario y mantiene el historial de navegacion (pila). Se crea normalmente con `rememberNavController()`.
- **`NavHost`**: es el composable que "hospeda" el grafo de navegacion; recibe el `NavController` y una ruta de inicio (`startDestination`), y dentro de el se declaran los destinos disponibles.
- **Rutas (`route`)**: son cadenas de texto que identifican cada pantalla, similares a una URL. Pueden incluir marcadores de posicion para argumentos, por ejemplo `"detalle/{tareaId}"`.
- **Argumentos de navegacion (`navArgument`)**: permiten declarar el tipo esperado de cada parametro (`NavType.IntType`, `NavType.StringType`, etc.) para que Navigation Compose valide y extraiga el valor automaticamente dentro del composable de destino.
- **Back stack**: cada llamada a `navigate()` apila un nuevo destino; `popBackStack()` (o el boton atras) desapila el destino actual y vuelve al anterior.

**Ciclo de vida asociado.** Cada entrada en el back stack tiene su propio ciclo de vida (`Lifecycle`), lo cual es relevante mas adelante cuando se trabaje con `ViewModel` scoped a una pantalla especifica (tema de la Sesion 12 en adelante).

---

## 4. Ejemplo de codigo en Kotlin

```kotlin
// Definicion de rutas como constantes para evitar errores de tipeo
object Rutas {
    const val LISTA = "lista"
    const val DETALLE = "detalle/{tareaId}"

    fun detalleDe(tareaId: Int) = "detalle/$tareaId"
}

@Composable
fun TaskManagerApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.LISTA) {

        composable(Rutas.LISTA) {
            PantallaListaTareas(
                onTareaClick = { tareaId ->
                    navController.navigate(Rutas.detalleDe(tareaId))
                }
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("tareaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tareaId = backStackEntry.arguments?.getInt("tareaId") ?: -1
            PantallaDetalleTarea(
                tareaId = tareaId,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun PantallaListaTareas(onTareaClick: (Int) -> Unit) {
    val tareas = remember {
        listOf(
            Tarea(1, "Repasar Navigation Compose"),
            Tarea(2, "Practicar paso de parametros")
        )
    }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(tareas, key = { it.id }) { tarea ->
            Text(
                text = tarea.titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clickable { onTareaClick(tarea.id) }
            )
        }
    }
}

@Composable
fun PantallaDetalleTarea(tareaId: Int, onVolver: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Detalle de la tarea #$tareaId", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onVolver) {
            Text("Volver a la lista")
        }
    }
}
```

Puntos a resaltar en clase:
- Centralizar las rutas en un `object Rutas` evita errores de tipeo y facilita el mantenimiento.
- El argumento `tareaId` se declara con su tipo (`NavType.IntType`) para que Navigation Compose lo valide automaticamente.
- `onVolver = { navController.popBackStack() }` es la forma recomendada de volver atras de forma programatica, equivalente a presionar el boton atras del sistema.

---

## 5. Ejercicio practico para los estudiantes

Sobre el proyecto "Task Manager" (o el proyecto propio):

1. Implementar al menos dos pantallas conectadas por Navigation Compose: una lista y un detalle.
2. Pasar como minimo un parametro desde la lista hacia el detalle (por ejemplo el id de la tarea) usando `navArgument`.
3. Agregar un tercer destino opcional, por ejemplo una pantalla de "Nueva tarea", accesible desde la lista mediante un boton flotante (`FloatingActionButton`), que al guardar la tarea vuelva a la lista (`popBackStack()`).
4. Verificar que el boton atras del sistema funcione correctamente en cada pantalla y que el back stack no permita volver a una pantalla de "creacion" ya completada de forma inconsistente.
5. Integrar esta navegacion en el proyecto que se viene desarrollando, dejandolo listo para el trabajo colaborativo con Git de la Sesion 9.

Este ejercicio corresponde a la actividad de "Despues de la clase": integrar la navegacion en el proyecto.

---

## 6. Material de apoyo / enlaces

- Documentacion general de Kotlin: https://kotlinlang.org/docs/home.html
- Documentacion oficial de Android: https://developer.android.com/docs
- Jetpack Compose (guia general): https://developer.android.com/jetpack/compose
- Navigation Compose (guia oficial): https://developer.android.com/jetpack/compose/navigation
- Paso de argumentos entre pantallas: https://developer.android.com/jetpack/compose/navigation#nav-with-args
- Ciclo de vida y arquitectura general de la app: https://developer.android.com/topic/architecture
