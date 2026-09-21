# Contenido de la Sesion 23 - Integracion de Room con MVVM

## 1. Objetivo de la sesion

Integrar la persistencia de datos construida con Room (Sesiones 20 y 21) dentro de la arquitectura MVVM (Model-View-ViewModel), de modo que la interfaz de usuario ya no acceda directamente al DAO, sino a traves de un ViewModel, siguiendo las practicas recomendadas de arquitectura de apps en Android.

## 2. Guion / desarrollo de la clase

### Bloque 1: Apertura (10 min)
- Se recuerda que, hasta la Sesion 21, la UI invocaba directamente al DAO de Room, lo cual es valido para fines didacticos pero no es la practica recomendada en un proyecto real.
- Se plantea el objetivo de la sesion: introducir un ViewModel que actue como intermediario entre la UI (Compose) y la fuente de datos (Room), separando responsabilidades.
- Se revisan brevemente los ejemplos de integracion entre Room y MVVM que se pidio revisar como material previo.

### Bloque 2: Exposicion de contenido (35 min)
- Se retoma el patron MVVM: Model (los datos y la logica de acceso a ellos, en este caso Room), View (los Composables, que solo muestran estado y notifican eventos del usuario) y ViewModel (que mantiene el estado de la UI y expone funciones para modificarlo, comunicandose con el Model).
- Se explica el rol de la clase `ViewModel` de Android Jetpack: sobrevive a cambios de configuracion (como rotaciones de pantalla) y provee un `viewModelScope` para lanzar coroutines de forma segura, vinculadas al ciclo de vida del ViewModel.
- Se introduce el concepto de repositorio como capa opcional entre el ViewModel y el DAO, util cuando hay mas de una fuente de datos (por ejemplo, Room y una API remota), aunque en el ejemplo del curso se mantiene simple usando el DAO directamente desde el ViewModel.
- Se explica como expone el ViewModel el estado a la UI: normalmente mediante `StateFlow`, convertido a partir del `Flow` que entrega el DAO.

### Bloque 3: Demo en vivo (35 min)
- El docente construye en vivo un `TareasViewModel` para el proyecto de ejemplo, que obtiene el TareaDao (a traves de la TareaDatabase) y expone la lista de tareas como `StateFlow`, ademas de funciones para agregar, actualizar y eliminar tareas.
- Se muestra como conectar una pantalla de Compose a este ViewModel usando `viewModel()` (de la libreria de ciclo de vida de Compose) y `collectAsState()`, reemplazando el acceso directo al DAO que se usaba en la Sesion 21.
- Se ejecuta la app y se verifica que el comportamiento observado (agregar, listar, actualizar, eliminar tareas) sigue funcionando igual, pero ahora con una arquitectura mas ordenada.

### Bloque 4: Practica guiada (30 min)
- Los estudiantes migran su propia pantalla (la que en la Sesion 21 invocaba el DAO directamente) para que en su lugar use un ViewModel propio, siguiendo el patron mostrado en la demo.
- El docente apoya con dudas tipicas: como instanciar el ViewModel con dependencias (por ejemplo, usando una `ViewModelFactory` simple si el ViewModel necesita el contexto o el DAO como parametro), y como evitar filtrar detalles de Room hacia los Composables.

### Bloque 5: Cierre (10 min)
- Se resume el flujo final de la arquitectura: View (Compose) observa estado del ViewModel, el ViewModel se comunica con el Model (Room, y opcionalmente DataStore) y expone funciones para modificar los datos, y los Composables nunca acceden directamente a Room ni a DataStore.
- Se explica la tarea posterior a la clase: completar la funcionalidad de persistencia del proyecto integrador siguiendo este patron.

## 3. Explicacion teorica breve

MVVM (Model-View-ViewModel) es un patron de arquitectura ampliamente recomendado para apps Android, y es el que promueve la guia oficial de arquitectura de Android Jetpack. Sus tres componentes son:

- **Model**: representa los datos y la logica de negocio de la app. En el curso, el Model esta compuesto por Room (para datos estructurados, como las tareas) y, opcionalmente, DataStore (para preferencias del usuario, visto en la Sesion 22).
- **View**: en el contexto de Jetpack Compose, la View son los Composables. Su responsabilidad es unicamente mostrar el estado que reciben y notificar eventos del usuario (por ejemplo, un clic en un boton), sin contener logica de negocio ni acceder directamente a la base de datos.
- **ViewModel**: actua como intermediario. Mantiene el estado de la pantalla (por ejemplo, la lista actual de tareas) y expone funciones que la View puede invocar para modificar ese estado (por ejemplo, agregar o eliminar una tarea). Internamente, el ViewModel se comunica con el Model (Room) para leer y escribir los datos reales.

La clase `ViewModel` de Android Jetpack tiene una ventaja practica importante: su ciclo de vida esta ligado a la pantalla (o al grafo de navegacion) y no a la actividad o configuracion especifica, por lo que sobrevive a cambios de configuracion como la rotacion de pantalla, evitando perdidas de estado innecesarias. Ademas, provee `viewModelScope`, un `CoroutineScope` que se cancela automaticamente cuando el ViewModel se destruye, lo cual es ideal para lanzar las operaciones suspend de Room sin riesgo de fugas de memoria.

Esta integracion cierra el circuito de persistencia del curso: Room y DataStore (Model) proveen y almacenan los datos, el ViewModel gestiona el estado y la logica de la pantalla, y los Composables (View) se limitan a mostrar ese estado y a notificar las acciones del usuario, respetando la separacion de responsabilidades que se viene reforzando desde los laboratorios integradores anteriores.

## 4. Ejemplo de codigo en Kotlin

Se construye un TareasViewModel que integra el TareaDao de las Sesiones 20 y 21 dentro de la arquitectura MVVM.

### ViewModel: TareasViewModel.kt

```kotlin
package com.upb.gestortareas.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.upb.gestortareas.data.TareaDatabase
import com.upb.gestortareas.data.TareaEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TareasViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = TareaDatabase.getDatabase(application).tareaDao()

    val tareas: StateFlow<List<TareaEntity>> = dao.obtenerTodas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun agregarTarea(titulo: String, descripcion: String) {
        viewModelScope.launch {
            dao.insertar(TareaEntity(titulo = titulo, descripcion = descripcion))
        }
    }

    fun actualizarTarea(tarea: TareaEntity) {
        viewModelScope.launch {
            dao.actualizar(tarea)
        }
    }

    fun eliminarTarea(tarea: TareaEntity) {
        viewModelScope.launch {
            dao.eliminar(tarea)
        }
    }
}
```

### Pantalla de Compose conectada al ViewModel

```kotlin
package com.upb.gestortareas.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PantallaTareas(viewModel: TareasViewModel = viewModel()) {
    val tareas by viewModel.tareas.collectAsState()

    Column {
        Text("Total de tareas: ${tareas.size}")

        Button(onClick = {
            viewModel.agregarTarea("Nueva tarea", "Descripcion de ejemplo")
        }) {
            Text("Agregar tarea")
        }

        tareas.forEach { tarea ->
            Button(onClick = { viewModel.eliminarTarea(tarea) }) {
                Text("Eliminar: ${tarea.titulo}")
            }
        }
    }
}
```

Nota: se usa `AndroidViewModel` para simplificar el acceso al contexto de la aplicacion (necesario para `TareaDatabase.getDatabase`). En proyectos con inyeccion de dependencias mas formal, esto se resolveria con un framework como Hilt, que queda fuera del alcance de esta sesion.

## 5. Ejercicio practico para los estudiantes

Como tarea posterior a la clase, en linea con "completar la funcionalidad de persistencia" del proyecto integrador, cada estudiante debe:

1. Crear un ViewModel propio para la pantalla principal de su proyecto, que encapsule el acceso al DAO de Room construido en las Sesiones 20 y 21.
2. Migrar la pantalla de Compose correspondiente para que consuma el estado y las funciones expuestas por el ViewModel, en lugar de acceder directamente al DAO.
3. Si el proyecto ya implemento DataStore en la Sesion 22, integrar tambien esas preferencias a traves del mismo ViewModel (o de uno adicional), manteniendo la misma logica de separacion de responsabilidades.
4. Verificar que toda la funcionalidad (crear, leer, actualizar, eliminar datos, y las preferencias del usuario) sigue funcionando correctamente tras la migracion a MVVM.

## 6. Material de apoyo / enlaces

- Guia oficial de arquitectura de apps en Android (patron MVVM y capas recomendadas): https://developer.android.com/topic/architecture
- Documentacion de ViewModel: https://developer.android.com/topic/libraries/architecture/viewmodel
- Documentacion oficial de Room: https://developer.android.com/training/data-storage/room
- Documentacion oficial de DataStore: https://developer.android.com/topic/libraries/architecture/datastore
- Documentacion de Kotlin Coroutines y Flow: https://kotlinlang.org/docs/coroutines-overview.html
