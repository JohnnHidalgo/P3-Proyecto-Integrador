package com.upb.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upb.taskmanager.data.remote.TareasRemoteRepository
import com.upb.taskmanager.model.GestorDeTareas
import com.upb.taskmanager.util.LONGITUD_MINIMA_TITULO
import com.upb.taskmanager.util.Resultado
import com.upb.taskmanager.util.tituloValido
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
 * `cargando = true` mientras esa llamada de red esta en curso.
 *
 * Sesion 16: tanto la carga remota como el formulario de agregar tarea usan
 * [Resultado] / [tituloValido] para reportar errores en `uiState.mensajeError`
 * en vez de fallar en silencio o lanzar una excepcion hacia la UI.
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

    /**
     * Agrega una tarea nueva si el titulo es valido (ver [tituloValido]).
     * Devuelve `true` si se agrego correctamente, o `false` si se rechazo por
     * una validacion (en ese caso, `uiState.mensajeError` queda con el motivo).
     */
    fun agregarTarea(titulo: String): Boolean {
        if (!tituloValido(titulo)) {
            _uiState.update {
                it.copy(mensajeError = "El titulo debe tener al menos $LONGITUD_MINIMA_TITULO caracteres.")
            }
            return false
        }
        gestorDeTareas.agregarTarea(titulo)
        sincronizarTareas()
        return true
    }

    /** Alterna el estado completada/pendiente de una tarea y refresca el estado de UI. */
    fun alternarCompletada(id: Int) {
        gestorDeTareas.alternarCompletada(id)
        sincronizarTareas()
    }

    /**
     * Trae tareas de ejemplo desde el repositorio remoto y las agrega al
     * gestor local. Se ejecuta en `viewModelScope`, un `CoroutineScope` que
     * el propio ViewModel cancela automaticamente cuando se destruye.
     */
    private fun cargarTareasRemotas() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, mensajeError = null) }
            when (val resultado = tareasRemoteRepository.obtenerTareasRemotas()) {
                is Resultado.Exito -> {
                    for (tareaRemota in resultado.datos) {
                        val tareaCreada = gestorDeTareas.agregarTarea(tareaRemota.title)
                        if (tareaRemota.completed) {
                            gestorDeTareas.alternarCompletada(tareaCreada.id)
                        }
                    }
                    _uiState.update {
                        it.copy(tareas = gestorDeTareas.obtenerTareas(), cargando = false)
                    }
                }
                is Resultado.Error -> {
                    _uiState.update { it.copy(cargando = false, mensajeError = resultado.mensaje) }
                }
            }
        }
    }

    private fun sincronizarTareas() {
        _uiState.update { estadoActual ->
            estadoActual.copy(tareas = gestorDeTareas.obtenerTareas(), mensajeError = null)
        }
    }
}
