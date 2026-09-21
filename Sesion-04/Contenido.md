# Contenido de la Sesion 04 - Introduccion a Jetpack Compose

**Tema del curso:** Tema 3 - Interfaces de usuario con Jetpack Compose

## 1. Objetivo de la sesion

Comprender la estructura de una aplicacion construida con Jetpack Compose y crear la primera pantalla
de la interfaz del proyecto integrador, mostrando de forma visual las tareas modeladas en Kotlin puro
en la sesion anterior.

## 2. Guion / desarrollo de la clase

Bloque de aproximadamente 120 minutos.

### Bloque 1 (0:00 - 0:10) Apertura
- Revision breve del modelo de datos (`Tarea`, `GestorDeTareas`) construido en la Sesion 3.
- Presentacion del objetivo: llevar ese modelo a una pantalla visual con Compose.

### Bloque 2 (0:10 - 0:30) Que es Jetpack Compose y por que se usa
- Diferencia entre el enfoque declarativo de Compose y el enfoque imperativo de las Vistas (Views) y XML
  tradicionales de Android.
- Ventajas de describir la interfaz como funciones de Kotlin.
- El concepto de "recomposicion": la interfaz se vuelve a dibujar automaticamente cuando cambian los
  datos que la describen.

### Bloque 3 (0:30 - 0:55) Estructura de una aplicacion Compose
- Recorrido por los archivos generados por Android Studio: `MainActivity.kt`, el paquete `ui.theme`
  (`Color.kt`, `Theme.kt`, `Type.kt`).
- La funcion `setContent { ... }` como punto de entrada de la interfaz.
- Anatomia de una funcion `@Composable`: por que se nombran en mayuscula inicial, por que no retornan
  un valor de interfaz sino que "emiten" UI.
- El modificador `Modifier` como forma de ajustar tamano, espaciado y comportamiento de un componente.

### Bloque 4 (0:55 - 1:20) Composicion de funciones y jerarquia visual
- Como una pantalla se construye combinando composables mas pequenos dentro de composables mas grandes.
- Contenedores basicos para organizar elementos: `Column` (vertical) y `Row` (horizontal).
- Demostracion en vivo: construir una pantalla simple combinando `Column`, `Row` y `Text`.

### Bloque 5 (1:20 - 1:50) Practica guiada: primera pantalla del proyecto integrador
- Construccion conjunta de una pantalla que muestra el titulo de la app y una lista simple (estatica por
  ahora) de tareas de ejemplo, usando `Column` y `Text`.
- Uso de `@Preview` para visualizar la pantalla sin ejecutar el emulador completo.

### Bloque 6 (1:50 - 2:00) Cierre
- Recapitulacion de los conceptos clave: composable, recomposicion, `Modifier`, `Column`/`Row`.
- Indicaciones para la tarea: implementar la pantalla inicial del proyecto integrador.

## 3. Explicacion teorica breve

**Jetpack Compose** es el toolkit declarativo de Android para construir interfaces de usuario. En lugar
de describir la interfaz en archivos XML y luego manipularla de forma imperativa desde codigo (como en
el sistema tradicional de Vistas), en Compose la interfaz se describe directamente con funciones de
Kotlin marcadas con la anotacion `@Composable`.

Una funcion `@Composable` no "devuelve" una vista como tal; en su lugar, describe que UI deberia
mostrarse dado un determinado estado. Cuando ese estado cambia, Compose vuelve a ejecutar (recomponer)
las funciones afectadas para reflejar el nuevo estado en pantalla, proceso conocido como
**recomposicion**.

El punto de entrada de una app Compose suele ser `setContent { ... }` dentro de una `Activity`. Dentro de
ese bloque se invoca la jerarquia de composables que forman la pantalla, normalmente envuelta en un tema
(`MaterialTheme`) que se vera con mayor detalle en la Sesion 6.

Los contenedores `Column` y `Row` son composables que organizan a sus elementos hijos en una disposicion
vertical y horizontal respectivamente, de forma analoga a un `LinearLayout` vertical u horizontal en el
sistema tradicional de Vistas. El parametro `Modifier` se usa para ajustar el aspecto y comportamiento de
un composable (tamano, relleno, margenes, alineacion, entre otros) de forma encadenada.

## 4. Ejemplo de codigo en Kotlin

Primera pantalla del proyecto integrador Task Manager, mostrando una lista estatica de tareas de ejemplo
usando Compose (la version dinamica y con componentes interactivos se vera en la Sesion 5):

```kotlin
package com.upb.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaListaDeTareas()
                }
            }
        }
    }
}

// Reutilizamos la clase Tarea modelada en la Sesion 3 (version simplificada aqui)
data class Tarea(val id: Int, val titulo: String, val completada: Boolean = false)

@Composable
fun PantallaListaDeTareas() {
    val tareasDeEjemplo = listOf(
        Tarea(1, "Estudiar Jetpack Compose"),
        Tarea(2, "Modelar la clase Tarea", completada = true),
        Tarea(3, "Configurar el repositorio en GitHub")
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Mis tareas",
            style = MaterialTheme.typography.headlineSmall
        )

        tareasDeEjemplo.forEach { tarea ->
            FilaDeTarea(tarea = tarea)
        }
    }
}

@Composable
fun FilaDeTarea(tarea: Tarea) {
    val estado = if (tarea.completada) "Completada" else "Pendiente"
    Text(
        text = "${tarea.id}. ${tarea.titulo} - $estado",
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PantallaListaDeTareasPreview() {
    MaterialTheme {
        PantallaListaDeTareas()
    }
}
```

Puntos a resaltar en la demo:
- `PantallaListaDeTareas` es el composable "de pantalla" y `FilaDeTarea` es un composable mas pequeno que
  reutiliza dentro de un `forEach`.
- El uso de `Modifier.padding(...)` para dar espaciado sin necesidad de XML.
- Por ahora la lista de tareas es estatica (una lista fija en el codigo); en sesiones posteriores se
  hara dinamica con estado (`remember`, `mutableStateOf`).

## 5. Ejercicio practico para los estudiantes

1. En el proyecto Compose creado en la Sesion 1, implementar la pantalla inicial del proyecto integrador
   siguiendo el patron mostrado en clase:
   - Un titulo visible en la parte superior con el nombre de la aplicacion.
   - Una lista (estatica por ahora) de al menos cinco tareas de ejemplo, reutilizando la clase `Tarea`
     modelada en la Sesion 3 (o una version simplificada de ella).
   - Cada tarea debe mostrarse en su propio composable reutilizable (similar a `FilaDeTarea`).
2. Agregar una `@Preview` que permita visualizar la pantalla sin ejecutar el emulador.
3. Ejecutar la app en el emulador o dispositivo fisico y verificar que la pantalla se muestre
   correctamente.
4. Subir el avance al repositorio de GitHub con un commit descriptivo (por ejemplo:
   "Implementa pantalla inicial de lista de tareas con Compose").

## 6. Material de apoyo / enlaces

- Jetpack Compose - documentacion general: https://developer.android.com/jetpack/compose
- Jetpack Compose - conceptos clave (mental model): https://developer.android.com/develop/ui/compose/mental-model
- Jetpack Compose - diseno con Column, Row y Box: https://developer.android.com/develop/ui/compose/layouts/basics
- Documentacion oficial de Kotlin: https://kotlinlang.org/docs/home.html
- Documentacion general de Android: https://developer.android.com/docs
