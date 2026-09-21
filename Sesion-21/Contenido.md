# Contenido de la Sesion 21 - Operaciones CRUD con Room

## 1. Objetivo de la sesion

Implementar el conjunto completo de operaciones CRUD (Crear, Leer, Actualizar, Eliminar) usando Room, extendiendo el DAO introducido en la Sesion 20 para que la app pueda gestionar datos de forma persistente y completa.

## 2. Guion / desarrollo de la clase

### Bloque 1: Apertura (10 min)
- Se recuerda brevemente lo visto en la Sesion 20: Entity, DAO y Database, con el ejemplo de la app de gestion de tareas.
- Se plantea el objetivo de hoy: pasar de un DAO que solo lee datos a uno que permite crear, leer, actualizar y eliminar tareas (CRUD completo).
- Se revisan en conjunto los ejemplos de operaciones CRUD con Room que se pidio revisar como material previo.

### Bloque 2: Exposicion de contenido (30 min)
- Se explica el significado de CRUD y su relacion directa con las anotaciones de Room: `@Insert`, `@Query` (para lectura), `@Update` y `@Delete`.
- Se detalla el comportamiento por defecto de cada anotacion (por ejemplo, `@Insert` usa la clave primaria para decidir si inserta o reemplaza segun la estrategia de conflicto configurada).
- Se explica por que todas estas funciones del DAO deben marcarse como `suspend` (o devolver `Flow` en el caso de las lecturas), y se recuerda la necesidad de invocarlas desde una coroutine (por ejemplo, dentro de un `viewModelScope` o `lifecycleScope`).
- Se introduce brevemente el concepto de repositorio como una capa intermedia opcional entre el DAO y la UI, que se formalizara mejor en la Sesion 23 con MVVM.

### Bloque 3: Demo en vivo (35 min)
- El docente extiende en vivo el TareaDao de la sesion anterior, agregando las operaciones de insertar, actualizar y eliminar tareas.
- Se muestra como invocar estas funciones desde una pantalla simple de Compose (por ejemplo, un boton para agregar una tarea de prueba y un boton para eliminarla), usando una coroutine lanzada con `rememberCoroutineScope` o similar, a modo de demostracion previa a introducir el ViewModel.
- Se verifica en el inspector de base de datos que los cambios (insercion, actualizacion, eliminacion) se reflejan correctamente en la tabla.

### Bloque 4: Practica guiada (30 min)
- Los estudiantes completan su propio TareaDao (o el DAO de su entidad equivalente) agregando las cuatro operaciones CRUD.
- Se practica invocar cada operacion desde una pantalla de prueba sencilla, verificando que los datos persisten correctamente al cerrar y volver a abrir la app.
- El docente apoya con casos comunes de error: no marcar las funciones como suspend, no observar el Flow correctamente en la UI, o intentar actualizar un registro sin que su clave primaria coincida con uno existente.

### Bloque 5: Cierre (15 min)
- Se resume el flujo completo: la UI invoca al DAO (directa o indirectamente), el DAO ejecuta la operacion sobre la base de datos, y los cambios se reflejan en la UI gracias al uso de Flow para las lecturas.
- Se explica la tarea posterior a la clase: integrar esta persistencia local en el proyecto propio de cada estudiante.

## 3. Explicacion teorica breve

Las operaciones CRUD son las cuatro operaciones basicas que se pueden realizar sobre datos persistentes: Crear (Create), Leer (Read), Actualizar (Update) y Eliminar (Delete). En Room, cada una de estas operaciones se expresa mediante anotaciones especificas dentro de la interfaz DAO:

- `@Insert`: inserta uno o mas registros en la tabla. Permite configurar una estrategia de conflicto (por ejemplo, `OnConflictStrategy.REPLACE`) para decidir que hacer si ya existe un registro con la misma clave primaria.
- `@Query`: permite escribir una consulta SQL personalizada, usada tipicamente para las operaciones de lectura, pero tambien util para eliminaciones o actualizaciones mas complejas que no se ajustan a `@Update` o `@Delete` directos.
- `@Update`: actualiza uno o mas registros existentes, identificandolos por su clave primaria. Si el registro no existe, la operacion no tiene efecto.
- `@Delete`: elimina uno o mas registros, tambien identificados por su clave primaria.

Todas estas operaciones de escritura (insertar, actualizar, eliminar) deben marcarse como funciones `suspend`, ya que implican acceso a disco y no deben ejecutarse en el hilo principal de la aplicacion. Las operaciones de lectura, en cambio, suelen exponerse como `Flow<List<T>>`, lo que permite que la interfaz de usuario se actualice automaticamente cada vez que cambian los datos en la base de datos, sin necesidad de volver a consultar manualmente.

Este comportamiento reactivo de Flow es clave: en Jetpack Compose, un `Flow` expuesto por el DAO se puede recolectar (por ejemplo, con `collectAsState()`) para que la UI se recomponga automaticamente cuando los datos cambian, cerrando el ciclo entre persistencia y presentacion.

## 4. Ejemplo de codigo en Kotlin

Se extiende el TareaDao de la Sesion 20 para incluir el CRUD completo sobre la entidad TareaEntity.

### DAO completo: TareaDao.kt

```kotlin
package com.upb.gestortareas.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {

    @Query("SELECT * FROM tareas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<TareaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(tarea: TareaEntity)

    @Update
    suspend fun actualizar(tarea: TareaEntity)

    @Delete
    suspend fun eliminar(tarea: TareaEntity)

    @Query("DELETE FROM tareas WHERE completada = 1")
    suspend fun eliminarCompletadas()
}
```

### Uso basico desde una pantalla de Compose (demostracion previa al ViewModel)

```kotlin
package com.upb.gestortareas.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.upb.gestortareas.data.TareaDatabase
import com.upb.gestortareas.data.TareaEntity
import kotlinx.coroutines.launch

@Composable
fun PantallaTareasDemo() {
    val contexto = LocalContext.current
    val dao = TareaDatabase.getDatabase(contexto).tareaDao()
    val scope = rememberCoroutineScope()

    val tareas by dao.obtenerTodas().collectAsState(initial = emptyList())

    Column {
        Text("Total de tareas: ${tareas.size}")

        Button(onClick = {
            scope.launch {
                dao.insertar(
                    TareaEntity(titulo = "Nueva tarea", descripcion = "Descripcion de ejemplo")
                )
            }
        }) {
            Text("Agregar tarea de prueba")
        }

        tareas.forEach { tarea ->
            Button(onClick = {
                scope.launch { dao.eliminar(tarea) }
            }) {
                Text("Eliminar: ${tarea.titulo}")
            }
        }
    }
}
```

Nota pedagogica: este ejemplo invoca el DAO directamente desde la UI solo con fines didacticos, para enfocarse en el CRUD. En la Sesion 23 se introducira un ViewModel siguiendo el patron MVVM, que es la forma recomendada de estructurar esta interaccion en un proyecto real.

## 5. Ejercicio practico para los estudiantes

Como tarea posterior a la clase, alineada con "integrar la persistencia local en el proyecto", cada estudiante debe:

1. Completar el DAO de su propio proyecto con las cuatro operaciones CRUD (insertar, leer, actualizar, eliminar), siguiendo el patron mostrado en clase.
2. Conectar estas operaciones a una pantalla real de su app (no solo una pantalla de prueba), permitiendo que el usuario pueda agregar, ver, modificar y eliminar registros desde la interfaz.
3. Verificar que los datos persisten correctamente al cerrar y reabrir la aplicacion.
4. Documentar brevemente en el repositorio (por ejemplo, en el README del proyecto) que operaciones CRUD fueron implementadas y sobre que entidad.

## 6. Material de apoyo / enlaces

- Documentacion oficial de Room: https://developer.android.com/training/data-storage/room
- Guia de acceso a datos con Room (DAO y anotaciones CRUD): https://developer.android.com/training/data-storage/room/accessing-data
- Documentacion de Kotlin Flow: https://kotlinlang.org/docs/flow.html
- Documentacion de Kotlin Coroutines: https://kotlinlang.org/docs/coroutines-overview.html
- Guia de arquitectura de apps en Android: https://developer.android.com/topic/architecture
