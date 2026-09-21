package com.upb.taskmanager.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.upb.taskmanager.model.GestorDeTareas
import com.upb.taskmanager.model.Tarea

/**
 * Sesion 12: introduccion a MVVM.
 *
 * ViewModel de la pantalla de tareas. Envuelve un [GestorDeTareas] (la logica
 * de negocio) y expone su estado a la UI mediante `tareas`, una lista
 * observable por Compose (`mutableStateListOf`). El nombre de la clase se
 * mantiene `TareasViewModel` durante todo el curso, aunque su implementacion
 * interna cambie mas adelante (ver Sesion 13, donde `tareas` se reemplaza por
 * un `StateFlow<TareasUiState>`).
 *
 * Al sobrevivir a los cambios de configuracion (por ejemplo, rotar la
 * pantalla), el ViewModel evita que la lista de tareas en memoria se pierda,
 * algo que si ocurria con el `remember` usado en las sesiones anteriores.
 */
class TareasViewModel : ViewModel() {

    private val gestorDeTareas = GestorDeTareas()

    /** Lista de tareas observable: cualquier cambio recompone la UI que la lea. */
    val tareas = mutableStateListOf<Tarea>().apply { addAll(gestorDeTareas.obtenerTareas()) }

    /** Agrega una tarea nueva y refresca la lista observable. */
    fun agregarTarea(titulo: String) {
        gestorDeTareas.agregarTarea(titulo)
        sincronizarTareas()
    }

    /** Alterna el estado completada/pendiente de una tarea y refresca la lista. */
    fun alternarCompletada(id: Int) {
        gestorDeTareas.alternarCompletada(id)
        sincronizarTareas()
    }

    private fun sincronizarTareas() {
        tareas.clear()
        tareas.addAll(gestorDeTareas.obtenerTareas())
    }
}
