package com.upb.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upb.taskmanager.data.remote.TareasRemoteRepository
import com.upb.taskmanager.model.GestorDeTareas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Sesion 12: introduccion a MVVM.
 *
 * ViewModel de la pantalla de tareas. Envuelve un [GestorDeTareas] (la logica
 * de negocio) y expone su estado a la UI. El nombre de la clase se mantiene
 * `TareasViewModel` durante todo el curso, aunque su implementacion interna
 * cambie mas adelante (Sesion 23: pasara a observar Room mediante `Flow`).
 *
 * Sesion 13: el estado se modela como un unico [TareasUiState] envuelto en un
 * [StateFlow], en vez de una `mutableStateListOf` suelta.
 *
 * Sesion 15: al crearse, el ViewModel sincroniza una vez con el repositorio
 * remoto ([TareasRemoteRepository]) usando `viewModelScope.launch`, y expone
 * `cargando = true` mientras esa llamada de red esta en curso, para que la UI
 * pueda mostrar un indicador de progreso.
 */
class TareasViewModel(
    private val tareasRemoteRepository: TareasRemoteRepository = TareasRemoteRepository()
) : ViewModel() {

    private val gestorDeTareas = GestorDeTareas()

    private val _uiState = MutableStateFlow(TareasUiState(tareas = gestorDeTareas.obtenerTareas()))

    /** Estado de UI de solo lectura, observable desde Compose con `collectAsState()`. */
    val uiState: StateFlow<TareasUiState> = _uiState.asStateFlow()

    init {
        cargarTareasRemotas()
    }

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

    /**
     * Trae tareas de ejemplo desde el repositorio remoto y las agrega al
     * gestor local. Se ejecuta en `viewModelScope`, un `CoroutineScope` que
     * el propio ViewModel cancela automaticamente cuando se destruye, para no
     * dejar corrutinas huerfanas.
     */
    private fun cargarTareasRemotas() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            val tareasRemotas = tareasRemoteRepository.obtenerTareasRemotas()
            for (tareaRemota in tareasRemotas) {
                val tareaCreada = gestorDeTareas.agregarTarea(tareaRemota.title)
                if (tareaRemota.completed) {
                    gestorDeTareas.alternarCompletada(tareaCreada.id)
                }
            }
            _uiState.update { it.copy(tareas = gestorDeTareas.obtenerTareas(), cargando = false) }
        }
    }

    private fun sincronizarTareas() {
        _uiState.update { estadoActual -> estadoActual.copy(tareas = gestorDeTareas.obtenerTareas()) }
    }
}
