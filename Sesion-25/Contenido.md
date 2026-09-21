# Contenido de la Sesion 25 - Refactorizacion y optimizacion

## 1. Objetivo de la sesion

Que el estudiante aplique principios de refactorizacion y optimizacion sobre codigo Kotlin/Jetpack Compose ya funcional, identificando componentes repetidos o mal estructurados en su proyecto integrador y transformandolos en componentes reutilizables, sin alterar el comportamiento observable de la aplicacion.

## 2. Guion / desarrollo de la clase (aprox. 2 horas)

### Apertura (10 min)
- Recordatorio de la sesion anterior: en la Sesion 24 se corrigieron errores; hoy se trabaja sobre codigo que ya funciona pero puede mejorar en claridad, mantenibilidad y reutilizacion.
- Se presenta la idea central: "hacer que el codigo funcione" y "hacer que el codigo sea bueno" son dos pasos distintos; refactorizar es el segundo paso.

### Exposicion de contenido (30 min)
- Que es refactorizar: cambiar la estructura interna del codigo sin cambiar su comportamiento externo.
- Senales de que un composable o una funcion necesitan refactorizacion ("code smells"): codigo duplicado en varias pantallas, funciones de composable muy largas (mas de una responsabilidad visual), parametros excesivos, logica de negocio mezclada con la interfaz, nombres poco descriptivos.
- Principios aplicables en Compose:
  - Extraer composables pequenos y con una sola responsabilidad (Single Responsibility aplicado a la UI).
  - Elevar el estado (state hoisting): un composable reutilizable no deberia guardar su propio estado si ese estado debe sobrevivir o ser compartido; en su lugar recibe el estado y una funcion de callback (`value` + `onValueChange`).
  - Parametros con valores por defecto y `Modifier` como primer parametro opcional, siguiendo las convenciones de la API de Compose.
  - Separar la logica de negocio (ViewModel, repositorios, casos de uso) de la logica de presentacion (composables).
  - Optimizacion basica de recomposicion: usar `remember`, evitar lambdas que se recrean innecesariamente, usar claves (`key`) en `LazyColumn` para listas.
- Diferencia entre refactorizar y optimizar: refactorizar mejora la estructura/legibilidad; optimizar mejora el rendimiento (por ejemplo, reducir recomposiciones innecesarias). Ambas pueden ir de la mano pero no son lo mismo.

### Demo en vivo (30 min)
- El docente muestra en la app "Task Manager" dos o tres composables casi identicos que muestran una tarjeta de tarea en distintas pantallas (lista de pendientes, lista de completadas, resultados de busqueda), cada uno con su propio codigo duplicado.
- Se refactoriza en vivo extrayendo un unico composable reutilizable `TareaCard`, aplicando state hoisting y parametros con valores por defecto (ver seccion 4).
- Se muestra como usar el atajo de Android Studio "Extract Function" / "Extract Composable" (Refactor > Extract) para acelerar este proceso.

### Practica guiada (35 min)
- En parejas, los estudiantes revisan su propio proyecto integrador buscando al menos un composable duplicado o una funcion demasiado larga.
- Aplican la extraccion de un componente reutilizable siguiendo el mismo patron de la demo: identificar duplicacion, definir la firma del nuevo composable (parametros, callbacks, modifier), reemplazar los usos originales.
- El docente circula resolviendo dudas, prestando especial atencion a errores comunes como olvidar elevar el estado o dejar el nuevo componente demasiado acoplado a una pantalla especifica.

### Cierre (15 min)
- Puesta en comun breve: una o dos parejas muestran su "antes y despues".
- Se enfatiza que la refactorizacion de hoy facilitara la documentacion (Sesion 26) porque un codigo mas ordenado es mas facil de explicar.
- Se explica la tarea para despues de la clase (seccion 5).

## 3. Explicacion teorica breve

La **refactorizacion** es el proceso de reestructurar codigo existente sin modificar su comportamiento externo, con el objetivo de mejorar atributos internos como la legibilidad, la mantenibilidad y la extensibilidad. A diferencia de corregir un bug (Sesion 24), refactorizar parte de codigo que ya funciona correctamente.

En el contexto de Jetpack Compose, la refactorizacion tiene particularidades propias:

- **Extraccion de composables**: cuando una funcion `@Composable` crece demasiado o mezcla varias responsabilidades visuales (por ejemplo, una pantalla completa que dibuja encabezado, lista y pie en un solo bloque), conviene dividirla en composables mas pequenos, cada uno enfocado en una parte especifica de la interfaz. Esto mejora la legibilidad y permite reutilizar esas piezas en otras pantallas.
- **State hoisting (elevacion de estado)**: un patron central en Compose. Un composable reutilizable idealmente no mantiene su propio estado mutable interno si ese estado necesita ser compartido, persistido o controlado desde fuera. En cambio, recibe el valor actual como parametro y expone una funcion lambda (`onXxxChange`) para notificar cambios, dejando que el llamador (usualmente un `ViewModel` o un composable de nivel superior) decida donde vive realmente el estado. Esto hace que el componente sea mas facil de probar y de reutilizar en contextos distintos.
- **Reutilizacion mediante parametros y valores por defecto**: Kotlin permite declarar parametros opcionales con valores por defecto y usar *named arguments*, lo que permite que un mismo composable sirva para varios casos de uso sin duplicar codigo (por ejemplo, una tarjeta de tarea que opcionalmente muestra un boton de eliminar segun el contexto).
- **Optimizacion de recomposicion**: Compose recompone (vuelve a ejecutar) las funciones `@Composable` cuando el estado que leen cambia. Usar `remember` evita recalcular valores costosos en cada recomposicion; usar `key` en los items de una `LazyColumn` o `LazyRow` ayuda a Compose a identificar que elementos realmente cambiaron, evitando recomposiciones innecesarias de toda la lista; evitar crear lambdas nuevas en cada recomposicion (por ejemplo, definiendolas fuera del cuerpo del composable cuando es posible) tambien reduce trabajo innecesario.

En terminos generales, una buena refactorizacion se guia por el principio de que cada componente debe tener una responsabilidad clara y un contrato (sus parametros de entrada y sus callbacks de salida) explicito y facil de entender sin tener que leer su implementacion interna.

## 4. Ejemplo de codigo en Kotlin: antes y despues

### Antes: composables duplicados

```kotlin
// Pantalla de tareas pendientes
@Composable
fun PantallaPendientes(tareas: List<Tarea>, onCompletar: (Int) -> Unit) {
    LazyColumn {
        items(tareas) { tarea ->
            Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = tarea.completada, onCheckedChange = { onCompletar(tarea.id) })
                    Text(text = tarea.titulo, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

// Pantalla de resultados de busqueda (mismo diseno de tarjeta, copiado y pegado)
@Composable
fun PantallaResultadosBusqueda(tareas: List<Tarea>, onCompletar: (Int) -> Unit) {
    LazyColumn {
        items(tareas) { tarea ->
            Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = tarea.completada, onCheckedChange = { onCompletar(tarea.id) })
                    Text(text = tarea.titulo, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
```

### Despues: componente reutilizable con state hoisting

```kotlin
/**
 * Tarjeta reutilizable que representa una tarea individual.
 * No mantiene estado propio: recibe la tarea y notifica los eventos
 * hacia arriba mediante callbacks (state hoisting).
 */
@Composable
fun TareaCard(
    tarea: Tarea,
    onCompletar: (Int) -> Unit,
    modifier: Modifier = Modifier,
    mostrarBotonEliminar: Boolean = false,
    onEliminar: (Int) -> Unit = {}
) {
    Card(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = tarea.completada, onCheckedChange = { onCompletar(tarea.id) })
            Text(
                text = tarea.titulo,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            if (mostrarBotonEliminar) {
                IconButton(onClick = { onEliminar(tarea.id) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar tarea")
                }
            }
        }
    }
}

@Composable
fun ListaDeTareas(
    tareas: List<Tarea>,
    onCompletar: (Int) -> Unit,
    mostrarBotonEliminar: Boolean = false,
    onEliminar: (Int) -> Unit = {}
) {
    LazyColumn {
        items(tareas, key = { it.id }) { tarea ->
            TareaCard(
                tarea = tarea,
                onCompletar = onCompletar,
                mostrarBotonEliminar = mostrarBotonEliminar,
                onEliminar = onEliminar
            )
        }
    }
}

// Uso en las distintas pantallas: ya no hay codigo duplicado.
@Composable
fun PantallaPendientes(tareas: List<Tarea>, onCompletar: (Int) -> Unit) {
    ListaDeTareas(tareas = tareas, onCompletar = onCompletar)
}

@Composable
fun PantallaResultadosBusqueda(tareas: List<Tarea>, onCompletar: (Int) -> Unit) {
    ListaDeTareas(tareas = tareas, onCompletar = onCompletar)
}
```

Notese que `ListaDeTareas` agrego `key = { it.id }` en el `items` de la `LazyColumn`: esta es la optimizacion de recomposicion mencionada en la seccion 3, que ayuda a Compose a identificar cada tarjeta de forma estable aunque la lista cambie de orden o de tamano.

## 5. Ejercicio practico para los estudiantes

Sobre el proyecto integrador:

1. Identificar al menos un composable duplicado (o muy similar) que aparezca en dos o mas pantallas del proyecto, o una funcion `@Composable` que mezcle mas de una responsabilidad visual clara.
2. Extraer un componente reutilizable siguiendo el patron visto en clase: definir su firma (parametros de datos, `Modifier` con valor por defecto, callbacks para eventos), aplicar state hoisting cuando corresponda, y reemplazar los usos duplicados por el nuevo componente.
3. Revisar al menos una `LazyColumn` o `LazyRow` del proyecto y asegurarse de que use `key` con un identificador estable de cada elemento.
4. Verificar que la app sigue funcionando exactamente igual que antes de refactorizar (la refactorizacion no debe introducir cambios de comportamiento visible).
5. Documentar brevemente en el `CHANGELOG.md` o en un comentario de commit que componentes se extrajeron y por que, como evidencia del proceso de optimizacion de la estructura del proyecto.

Esta actividad corresponde a la seccion "Despues de la clase" del silabo: optimizar la estructura del proyecto.

## 6. Material de apoyo / enlaces

- Documentacion oficial de Jetpack Compose: https://developer.android.com/jetpack/compose
- Pensar en Compose (thinking in Compose) y principios de composicion: https://developer.android.com/jetpack/compose/mental-model
- Estado y ciclo de vida en Compose (state hoisting): https://developer.android.com/jetpack/compose/state
- Rendimiento en Jetpack Compose: https://developer.android.com/jetpack/compose/performance
- Guia de estilo y convenciones de codigo Kotlin: https://kotlinlang.org/docs/coding-conventions.html
