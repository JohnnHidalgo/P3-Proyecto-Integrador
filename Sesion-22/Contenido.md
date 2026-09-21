# Contenido de la Sesion 22 - DataStore y preferencias

## 1. Objetivo de la sesion

Introducir Jetpack DataStore como mecanismo recomendado para almacenar preferencias simples del usuario (por ejemplo, tema oscuro/claro o un filtro preferido), en contraste con Room, que es adecuado para datos estructurados de mayor volumen.

## 2. Guion / desarrollo de la clase

### Bloque 1: Apertura (10 min)
- Se recupera el hilo de la sesion anterior: ya se tiene un CRUD funcional con Room para las tareas de la app.
- Se plantea una pregunta motivadora: como guardamos configuraciones pequenas del usuario, como si prefiere el tema oscuro o que filtro de tareas quiere ver por defecto, sin necesidad de crear una tabla completa en Room para eso.
- Se presenta DataStore como la solucion moderna de Android para este tipo de datos, reemplazando a SharedPreferences.

### Bloque 2: Exposicion de contenido (35 min)
- Se explica la diferencia entre Room y DataStore: Room esta pensado para datos estructurados y relacionales (tablas, consultas complejas), mientras que DataStore esta pensado para pares clave-valor simples o para objetos pequenos serializados (preferencias, configuraciones).
- Se presentan los dos tipos de DataStore: Preferences DataStore (pares clave-valor, mas simple) y Proto DataStore (objetos tipados con Protocol Buffers, mas robusto pero mas complejo de configurar). La sesion se enfoca en Preferences DataStore por su simplicidad para el nivel del curso.
- Se explica que DataStore usa Kotlin Flow y coroutines de forma nativa: las lecturas se exponen como Flow y las escrituras son funciones suspend, siguiendo el mismo espiritu asincrono ya visto con Room.
- Se menciona que, al ser basado en Flow, cualquier cambio en una preferencia se refleja automaticamente en la UI si esta se observa correctamente, de forma similar a como se observaban los datos de Room.

### Bloque 3: Demo en vivo (30 min)
- El docente crea en vivo un DataStore de preferencias para el proyecto de ejemplo (gestion de tareas), guardando dos preferencias sencillas: si el usuario prefiere el tema oscuro, y cual es su filtro de tareas preferido (por ejemplo: todas, pendientes o completadas).
- Se muestra como leer estas preferencias desde una pantalla de Compose usando `collectAsState()`, y como escribir un nuevo valor cuando el usuario cambia una opcion (por ejemplo, un switch de tema oscuro).

### Bloque 4: Practica guiada (30 min)
- Los estudiantes implementan, sobre el proyecto de ejemplo o el propio, un DataStore de preferencias con al menos una preferencia booleana o de texto relevante para su app.
- Se conecta esa preferencia a un control de UI (switch, radio buttons, o similar) y se verifica que el valor persiste al cerrar y volver a abrir la app.
- El docente apoya con errores comunes: olvidar el `.name` al definir las claves de preferencia, no usar `Dispatchers` adecuados, o no observar el Flow correctamente en Compose.

### Bloque 5: Cierre (15 min)
- Se resume cuando conviene usar DataStore frente a Room: preferencias y configuraciones simples versus datos estructurados con relaciones o consultas complejas.
- Se explica la tarea posterior a la clase: implementar preferencias en la aplicacion propia de cada estudiante.

## 3. Explicacion teorica breve

Jetpack DataStore es la solucion moderna de Android Jetpack para almacenar pequenas cantidades de datos de forma asincrona, consistente y transaccional, reemplazando el uso de SharedPreferences. Existen dos variantes:

- **Preferences DataStore**: almacena datos como pares clave-valor, de forma similar a SharedPreferences pero con soporte nativo de coroutines y Flow, y sin las inconsistencias de la API antigua (por ejemplo, evita operaciones de escritura sincronas y bloqueantes en el hilo principal).
- **Proto DataStore**: almacena objetos tipados definidos con Protocol Buffers, ofreciendo mayor seguridad de tipos a costa de mayor complejidad de configuracion. No se profundiza en esta variante en el curso, pero es importante que el estudiante sepa que existe para casos mas avanzados.

Para trabajar con Preferences DataStore se definen claves tipadas (por ejemplo, `booleanPreferencesKey`, `stringPreferencesKey`) que identifican cada valor guardado. La lectura de una preferencia se expone como un `Flow`, que emite un nuevo valor cada vez que la preferencia cambia, permitiendo que la UI reaccione automaticamente. La escritura se realiza mediante la funcion `edit`, que es una funcion suspend y por lo tanto debe invocarse desde una coroutine.

Un aspecto importante a resaltar es que DataStore no esta pensado para reemplazar a Room: no es adecuado para almacenar listas grandes de objetos ni para realizar consultas complejas con filtros o relaciones. Su rol complementario en la arquitectura de la app es guardar configuraciones y preferencias del usuario, mientras que Room sigue siendo responsable de los datos de negocio (en el ejemplo del curso, las tareas).

## 4. Ejemplo de codigo en Kotlin

Se agrega un DataStore de preferencias al proyecto de ejemplo de gestion de tareas, para guardar el tema preferido y el filtro de tareas preferido del usuario.

### Dependencia en build.gradle.kts (nivel de modulo)

```kotlin
dependencies {
    implementation("androidx.datastore:datastore-preferences:1.1.1")
}
```

### Definicion del DataStore: PreferenciasUsuario.kt

```kotlin
package com.upb.gestortareas.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "preferencias_usuario")

class PreferenciasUsuario(private val context: Context) {

    private object Claves {
        val TEMA_OSCURO = booleanPreferencesKey("tema_oscuro")
        val FILTRO_TAREAS = stringPreferencesKey("filtro_tareas")
    }

    val temaOscuro: Flow<Boolean> = context.dataStore.data
        .map { preferencias -> preferencias[Claves.TEMA_OSCURO] ?: false }

    val filtroTareas: Flow<String> = context.dataStore.data
        .map { preferencias -> preferencias[Claves.FILTRO_TAREAS] ?: "TODAS" }

    suspend fun guardarTemaOscuro(activado: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[Claves.TEMA_OSCURO] = activado
        }
    }

    suspend fun guardarFiltroTareas(filtro: String) {
        context.dataStore.edit { preferencias ->
            preferencias[Claves.FILTRO_TAREAS] = filtro
        }
    }
}
```

### Uso desde una pantalla de Compose

```kotlin
package com.upb.gestortareas.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.upb.gestortareas.data.PreferenciasUsuario
import kotlinx.coroutines.launch

@Composable
fun PantallaPreferencias() {
    val contexto = LocalContext.current
    val preferencias = remember { PreferenciasUsuario(contexto) }
    val scope = rememberCoroutineScope()

    val temaOscuro by preferencias.temaOscuro.collectAsState(initial = false)

    Column {
        Text("Tema oscuro")
        Switch(
            checked = temaOscuro,
            onCheckedChange = { nuevoValor ->
                scope.launch { preferencias.guardarTemaOscuro(nuevoValor) }
            }
        )
    }
}
```

Nota: se usa `remember { PreferenciasUsuario(contexto) }` de forma simplificada con fines didacticos; en un proyecto real conviene inyectar esta dependencia de forma mas ordenada (por ejemplo, desde el ViewModel que se vera en la Sesion 23).

## 5. Ejercicio practico para los estudiantes

Como tarea posterior a la clase, en linea con "implementar preferencias en la aplicacion", cada estudiante debe:

1. Crear en su propio proyecto un DataStore de preferencias, siguiendo el patron mostrado en clase.
2. Definir al menos dos preferencias relevantes para su app (por ejemplo, tema oscuro/claro y un filtro o configuracion propia del dominio de su proyecto).
3. Conectar estas preferencias a controles reales de la interfaz (switch, opciones de seleccion, etc.), de modo que el usuario pueda cambiarlas desde la app.
4. Verificar que las preferencias persisten correctamente al cerrar y reabrir la aplicacion.

## 6. Material de apoyo / enlaces

- Documentacion oficial de DataStore: https://developer.android.com/topic/libraries/architecture/datastore
- Guia de Preferences DataStore: https://developer.android.com/topic/libraries/architecture/datastore#preferences-datastore
- Documentacion de Kotlin Flow: https://kotlinlang.org/docs/flow.html
- Documentacion de Kotlin Coroutines: https://kotlinlang.org/docs/coroutines-overview.html
- Documentacion oficial de Room, como referencia comparativa: https://developer.android.com/training/data-storage/room
