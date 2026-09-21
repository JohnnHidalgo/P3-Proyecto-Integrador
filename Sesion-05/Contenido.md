# Contenido de la Sesion 05 - Componentes basicos de Compose

**Tema del curso:** Tema 3 - Interfaces de usuario con Jetpack Compose

## 1. Objetivo de la sesion

Conocer y aplicar los componentes basicos de Jetpack Compose (`Text`, `Button`, `TextField`, `Image`,
`Card` y `Scaffold`) para completar la interfaz principal del proyecto integrador, permitiendo agregar
y visualizar tareas de forma interactiva.

## 2. Guion / desarrollo de la clase

Bloque de aproximadamente 120 minutos.

### Bloque 1 (0:00 - 0:10) Apertura
- Revision breve de la pantalla estatica de tareas construida en la Sesion 4.
- Presentacion del objetivo: hacerla interactiva y mejor organizada visualmente.

### Bloque 2 (0:10 - 0:25) Estado en Compose: `remember` y `mutableStateOf`
- Necesidad de mantener y observar estado para que la interfaz reaccione a cambios (recomposicion).
- Uso basico de `remember { mutableStateOf(...) }`.
- Este bloque es prerrequisito para poder usar `TextField` y `Button` de forma interactiva.

### Bloque 3 (0:25 - 0:45) `Text`, `Image` y `Card`
- `Text`: estilos de texto, tamano, color, uso de `MaterialTheme.typography`.
- `Image`: mostrar imagenes desde recursos (`painterResource`) o iconos vectoriales.
- `Card`: agrupar visualmente contenido con elevacion y bordes redondeados; util para representar cada
  tarea como una tarjeta.

### Bloque 4 (0:45 - 1:05) `Button` y `TextField`
- `Button`: manejo del evento `onClick`.
- `TextField`: campo de texto editable, manejo de `value` y `onValueChange` junto con el estado
  recordado con `remember`.
- Demostracion en vivo: un campo de texto y un boton para agregar una nueva tarea a la lista.

### Bloque 5 (1:05 - 1:35) `Scaffold` y estructura general de pantalla
- `Scaffold` como esqueleto estandar de una pantalla Android: `topBar`, `content`, `floatingActionButton`.
- Integracion de todo lo anterior: una pantalla con `Scaffold`, una barra superior con el titulo de la
  app, un campo de texto y boton para agregar tareas, y la lista de tareas mostrada en `Card`.

### Bloque 6 (1:35 - 2:00) Practica guiada y cierre
- Los estudiantes adaptan la demo a su propio proyecto integrador.
- Recapitulacion de los componentes vistos.
- Indicaciones para la tarea: completar la interfaz principal del proyecto.

## 3. Explicacion teorica breve

En Jetpack Compose, el **estado** (`state`) representa cualquier valor que puede cambiar con el tiempo y
que, al cambiar, debe reflejarse en la interfaz. La funcion `remember { mutableStateOf(valorInicial) }`
crea una variable de estado que Compose recuerda entre recomposiciones; cuando su valor cambia, los
composables que la leen se vuelven a dibujar automaticamente.

Los componentes basicos mas usados son:

- **`Text`**: muestra texto en pantalla; admite parametros de estilo como tamano de fuente, color y peso.
- **`Image`**: muestra una imagen o icono, ya sea desde recursos del proyecto o desde un vector.
- **`Card`**: un contenedor con elevacion (sombra) y esquinas redondeadas, ideal para agrupar
  visualmente informacion relacionada, como los datos de una tarea.
- **`Button`**: un boton clickeable que ejecuta una accion definida en su parametro `onClick`.
- **`TextField`**: un campo de entrada de texto editable por el usuario; su valor actual y la accion a
  ejecutar cuando cambia se controlan mediante los parametros `value` y `onValueChange`, generalmente
  conectados a una variable de estado.
- **`Scaffold`**: un composable estructural que provee las areas estandar de una pantalla Android (barra
  superior, contenido principal, boton flotante, barra inferior, entre otras), facilitando una
  disposicion consistente con las guias de Material Design.

## 4. Ejemplo de codigo en Kotlin

Pantalla principal del proyecto integrador Task Manager, ahora interactiva: permite escribir el titulo
de una nueva tarea y agregarla a la lista, mostrando cada tarea dentro de una `Card`.

```kotlin
package com.upb.taskmanager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Tarea(val id: Int, val titulo: String)

@Composable
fun PantallaPrincipalTaskManager() {
    // Estado: texto que se esta escribiendo en el campo
    var nuevoTitulo by remember { mutableStateOf("") }

    // Estado: lista de tareas, observable por Compose
    val tareas = remember { mutableStateListOf(
        Tarea(1, "Estudiar componentes de Compose"),
        Tarea(2, "Practicar TextField y Button")
    ) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Task Manager") })
        }
    ) { paddingInterno ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingInterno).padding(16.dp)) {

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = nuevoTitulo,
                    onValueChange = { textoEscrito -> nuevoTitulo = textoEscrito },
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
                Button(onClick = {
                    if (nuevoTitulo.isNotBlank()) {
                        val nuevoId = (tareas.maxOfOrNull { it.id } ?: 0) + 1
                        tareas.add(Tarea(nuevoId, nuevoTitulo))
                        nuevoTitulo = ""
                    }
                }) {
                    Text("Agregar")
                }
            }

            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                items(tareas) { tarea ->
                    TarjetaDeTarea(tarea = tarea)
                }
            }
        }
    }
}

@Composable
fun TarjetaDeTarea(tarea: Tarea) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = "${tarea.id}. ${tarea.titulo}",
            modifier = Modifier.padding(12.dp)
        )
    }
}
```

Puntos a resaltar en la demo:
- `by remember { mutableStateOf(...) }` junto con `getValue`/`setValue` permite usar `nuevoTitulo` como
  una variable normal que dispara recomposicion al cambiar.
- `mutableStateListOf` crea una lista observable: agregar un elemento con `tareas.add(...)` actualiza la
  interfaz automaticamente.
- `LazyColumn` es la version eficiente de `Column` para listas potencialmente largas, ya que solo
  compone los elementos visibles en pantalla.

## 5. Ejercicio practico para los estudiantes

1. Completar la interfaz principal del proyecto integrador incorporando, como minimo:
   - Un `Scaffold` con una barra superior (`TopAppBar`) que muestre el nombre de la aplicacion.
   - Un `TextField` y un `Button` que permitan agregar una nueva tarea a la lista.
   - Cada tarea mostrada dentro de una `Card`, usando `LazyColumn` para la lista completa.
2. Agregar un icono o `Image` simple junto al titulo de la app (puede ser un icono vectorial de Material
   Icons o una imagen propia colocada en `res/drawable`).
3. Verificar que al agregar una tarea, la lista se actualice automaticamente sin reiniciar la app.
4. Subir el avance al repositorio de GitHub con un commit descriptivo (por ejemplo:
   "Agrega interfaz interactiva con TextField, Button, Card y Scaffold").

## 6. Material de apoyo / enlaces

- Jetpack Compose - estado y `remember`: https://developer.android.com/develop/ui/compose/state
- Jetpack Compose - componentes de texto y entrada: https://developer.android.com/develop/ui/compose/text
- Jetpack Compose - listas y grillas (`LazyColumn`): https://developer.android.com/develop/ui/compose/lists
- Jetpack Compose - componentes de Material (`Card`, `Scaffold`, `Button`): https://developer.android.com/jetpack/compose
- Documentacion oficial de Kotlin: https://kotlinlang.org/docs/home.html
