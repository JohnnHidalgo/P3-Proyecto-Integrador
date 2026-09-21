package com.upb.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import com.upb.taskmanager.model.GestorDeTareas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Sesion 12: introduccion a MVVM.
 *
 * ViewModel de la pantalla de tareas. Envuelve un [GestorDeTareas] (la logica
 * de negocio) y expone su estado a la UI. El nombre de la clase se mantiene
 * `TareasViewModel` durante todo el curso, aunque su implementacion interna
 * cambie mas adelante (Sesion 23: pasara a observar Room mediante `Flow`).
 *
 * Sesion 13: en vez de exponer una `mutableStateListOf` suelta, el estado se
 * modela como un unico [TareasUiState] envuelto en un [StateFlow]. Esto deja
 * la puerta abierta a agregar mas campos de estado (por ejemplo `cargando`,
 * usado a partir de la Sesion 15 con las llamadas de red) sin tener que
 * multiplicar variables de estado sueltas.
 */
class TareasViewModel : ViewModel() {

    private val gestorDeTareas = GestorDeTareas()

    private val _uiState = MutableStateFlow(TareasUiState(tareas = gestorDeTareas.obtenerTareas()))

    /** Estado de UI de solo lectura, observable desde Compose con `collectAsState()`. */
    val uiState: StateFlow<TareasUiState> = _uiState.asStateFlow()

    /** Agrega una tarea nueva y refresca el estado de UI. */
    fun agregarTarea(titulo: String) {
        gestorDeTareas.agregarTarea(titulo)
        sincronizarTareas()
    }

    /** Alterna el estado completada/pendiente de una tarea y refresca el estado de UI. */
    fun alternarCompletada(id: Int) {
        gestorDeTareas.alternarCompletada(id)
        sincronizarTareas()
    }

    private fun sincronizarTareas() {
        _uiState.update { estadoActual -> estadoActual.copy(tareas = gestorDeTareas.obtenerTareas()) }
    }
}
