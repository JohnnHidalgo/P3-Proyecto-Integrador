# Contenido de la Sesion 06 - Material Design 3

**Tema del curso:** Tema 3 - Interfaces de usuario con Jetpack Compose

## 1. Objetivo de la sesion

Aplicar los principios de Material Design 3 (temas, colores, tipografia y componentes reutilizables) a
la interfaz del proyecto integrador, mejorando su diseno visual de forma consistente y profesional.

## 2. Guion / desarrollo de la clase

Bloque de aproximadamente 120 minutos.

### Bloque 1 (0:00 - 0:10) Apertura
- Revision breve de la interfaz interactiva construida en la Sesion 5 (TextField, Button, Card,
  Scaffold).
- Presentacion del objetivo: darle una identidad visual coherente usando Material Design 3.

### Bloque 2 (0:10 - 0:30) Que es Material Design 3
- Material Design como sistema de diseno de Google: principios, no solo un conjunto de componentes.
- Que cambia respecto a versiones anteriores de Material Design (Material 2): enfasis en color
  dinamico, mayor personalizacion de temas.
- Rol de `MaterialTheme` en Compose como punto central de aplicacion del tema.

### Bloque 3 (0:30 - 0:55) Temas y esquemas de color
- El archivo `Theme.kt` generado por Android Studio y la funcion `NombreDeLaAppTheme`.
- El `ColorScheme` de Material 3: colores primarios, secundarios, de superficie y de fondo.
- Soporte de tema claro y tema oscuro (`lightColorScheme` / `darkColorScheme`).
- Demostracion en vivo: personalizar el `ColorScheme` del proyecto integrador con una paleta propia.

### Bloque 4 (0:55 - 1:15) Tipografia
- El archivo `Type.kt` y el objeto `Typography` de Material 3.
- Estilos tipograficos predefinidos (`headlineSmall`, `titleLarge`, `bodyMedium`, `labelSmall`, entre
  otros) y cuando usar cada uno.
- Aplicacion consistente de la tipografia del tema en lugar de tamanos de fuente "sueltos".

### Bloque 5 (1:15 - 1:45) Componentes reutilizables con el tema aplicado
- Refactorizacion de los composables construidos en la Sesion 5 (`TarjetaDeTarea`, boton de agregar)
  para que usen los colores y la tipografia del `MaterialTheme` en lugar de valores fijos.
- Creacion de un composable reutilizable de "chip" de prioridad con color segun la prioridad de la
  tarea, como ejemplo de componente reutilizable con Material 3.

### Bloque 6 (1:45 - 2:00) Practica guiada y cierre
- Los estudiantes aplican los cambios de tema a su propio proyecto integrador.
- Recapitulacion de los conceptos: `ColorScheme`, `Typography`, componentes reutilizables.
- Indicaciones para la tarea: mejorar el diseno visual de la aplicacion.

## 3. Explicacion teorica breve

**Material Design 3** (tambien llamado "Material You") es la version mas reciente del sistema de diseno
de Google. Ademas de definir componentes visuales (botones, tarjetas, campos de texto), propone un
sistema de temas basado en roles de color (primario, secundario, terciario, superficie, error, entre
otros) que se aplican de forma consistente en toda la aplicacion.

En Jetpack Compose, el tema se aplica envolviendo el contenido de la app en `MaterialTheme`, al que se le
pasa un `colorScheme` y un `typography`. Cualquier composable dentro de ese `MaterialTheme` puede acceder
a esos valores mediante `MaterialTheme.colorScheme.primary`, `MaterialTheme.typography.titleLarge`, etc.,
en lugar de usar colores o tamanos de fuente escritos directamente ("hardcodeados"). Esto tiene dos
ventajas principales: consistencia visual en toda la app y la posibilidad de cambiar el aspecto completo
de la aplicacion modificando un solo lugar (el tema).

Un **esquema de color** (`ColorScheme`) define, entre otros, los colores `primary`, `secondary`,
`tertiary`, `background`, `surface` y `error`, junto con sus contrapartes "on" (por ejemplo `onPrimary`,
el color de texto que debe usarse sobre un fondo `primary`). Material 3 en Compose permite definir un
esquema para tema claro y otro para tema oscuro.

La **tipografia** de Material 3 define una escala de estilos de texto (`displayLarge`, `headlineMedium`,
`titleLarge`, `bodyLarge`, `labelSmall`, etc.), cada uno pensado para un proposito especifico dentro de
la jerarquia visual de la pantalla.

Un **componente reutilizable** es un composable disenado para usarse en varios lugares de la app con
distintos parametros, manteniendo el mismo estilo visual definido por el tema. Disenar componentes
reutilizables evita duplicar codigo de estilo y facilita mantener la consistencia visual.

## 4. Ejemplo de codigo en Kotlin

Aplicacion de un tema Material 3 personalizado y de un componente reutilizable ("chip" de prioridad) a
la pantalla principal del proyecto integrador Task Manager.

```kotlin
package com.upb.taskmanager.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val ColorSchemeClaro = lightColorScheme(
    primary = Color(0xFF3B5BDB),
    onPrimary = Color.White,
    secondary = Color(0xFF12B886),
    background = Color(0xFFF7F7FA),
    surface = Color.White
)

private val ColorSchemeOscuro = darkColorScheme(
    primary = Color(0xFF748FFC),
    onPrimary = Color.Black,
    secondary = Color(0xFF63E6BE),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E)
)

val TipografiaTaskManager = Typography(
    titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 11.sp)
)

@Composable
fun TaskManagerTheme(temaOscuro: Boolean = false, content: @Composable () -> Unit) {
    val esquemaDeColor = if (temaOscuro) ColorSchemeOscuro else ColorSchemeClaro
    MaterialTheme(
        colorScheme = esquemaDeColor,
        typography = TipografiaTaskManager,
        content = content
    )
}
```

```kotlin
package com.upb.taskmanager

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class Prioridad { BAJA, MEDIA, ALTA }
data class Tarea(val id: Int, val titulo: String, val prioridad: Prioridad = Prioridad.MEDIA)

// Componente reutilizable: un "chip" de prioridad con color segun el nivel
@Composable
fun ChipDePrioridad(prioridad: Prioridad) {
    val color = when (prioridad) {
        Prioridad.BAJA -> MaterialTheme.colorScheme.secondary
        Prioridad.MEDIA -> MaterialTheme.colorScheme.primary
        Prioridad.ALTA -> MaterialTheme.colorScheme.error
    }

    Card(
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = prioridad.name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

// Tarjeta de tarea reutilizando el tema y el componente ChipDePrioridad
@Composable
fun TarjetaDeTarea(tarea: Tarea) {
    Card(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(modifier = Modifier.padding(12.dp)) {
            Text(
                text = tarea.titulo,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
            ChipDePrioridad(prioridad = tarea.prioridad)
        }
    }
}
```

Puntos a resaltar en la demo:
- `TaskManagerTheme` centraliza el esquema de color y la tipografia de toda la app; para aplicarlo basta
  envolver `setContent { TaskManagerTheme { ... } }` en `MainActivity`.
- `ChipDePrioridad` es un componente reutilizable que cambia de color segun la prioridad, siempre usando
  colores del tema (`MaterialTheme.colorScheme...`) en lugar de valores fijos.
- Cambiar la paleta de colores en `TaskManagerTheme` actualiza automaticamente el aspecto de toda la
  aplicacion, sin tocar cada pantalla individualmente.

## 5. Ejercicio practico para los estudiantes

1. Mejorar el diseno visual del proyecto integrador aplicando Material Design 3:
   - Definir un `ColorScheme` propio (claro y, opcionalmente, oscuro) con al menos tres colores
     personalizados (primario, secundario y de fondo).
   - Definir al menos dos estilos de `Typography` personalizados y aplicarlos en la pantalla principal.
   - Reemplazar cualquier color o tamano de fuente "hardcodeado" en composables existentes por
     referencias a `MaterialTheme.colorScheme` y `MaterialTheme.typography`.
2. Crear al menos un componente reutilizable adicional (por ejemplo, un chip de estado "Pendiente" /
   "Completada") que use el tema de la app.
3. Verificar visualmente el resultado en el emulador, comparando el "antes" (Sesion 5) y el "despues"
   (Sesion 6) de la interfaz.
4. Subir el avance al repositorio de GitHub con un commit descriptivo (por ejemplo:
   "Aplica tema Material Design 3 y componentes reutilizables a la interfaz").

## 6. Material de apoyo / enlaces

- Material Design 3 - sitio oficial: https://m3.material.io/
- Material Design 3 en Jetpack Compose: https://developer.android.com/develop/ui/compose/designsystems/material3
- Jetpack Compose - sistema de temas (Theming): https://developer.android.com/develop/ui/compose/designsystems/material2-material3
- Jetpack Compose - documentacion general: https://developer.android.com/jetpack/compose
- Documentacion oficial de Kotlin: https://kotlinlang.org/docs/home.html
