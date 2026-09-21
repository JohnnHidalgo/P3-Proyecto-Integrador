package com.upb.taskmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.upb.taskmanager.data.local.TareaDatabase
import com.upb.taskmanager.data.local.TareasLocalRepository
import com.upb.taskmanager.data.remote.TareasRemoteRepository
import com.upb.taskmanager.model.GestorDeTareas
import com.upb.taskmanager.model.Tarea
import com.upb.taskmanager.util.LONGITUD_MINIMA_TITULO
import com.upb.taskmanager.util.Resultado
import com.upb.taskmanager.util.tituloValido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Sesion 12: introduccion a MVVM.
 *
 * ViewModel de la pantalla de tareas. Envuelve un [GestorDeTareas] (la logica
 * de negocio) y expone su estado a la UI. El nombre de la clase se mantiene
 * `TareasViewModel` durante todo el curso.
 *
 * Sesion 13: el estado se modela como un unico [TareasUiState].
 *
 * Sesion 15/16: al crearse, el ViewModel puede sincronizar con el
 * repositorio remoto ([TareasRemoteRepository]), reportando `cargando` y
 * `mensajeError` mientras esa llamada de red esta en curso.
 *
 * Sesion 23: Room + MVVM final. El ViewModel pasa a extender [AndroidViewModel]
 * porque ahora necesita un `Context` (a traves de `Application`) para
 * construir la base de datos Room. `GestorDeTareas` ya no guarda las tareas
 * en memoria: `uiState.tareas` se construye combinando el `Flow` de Room
 * (`gestorDeTareas.observarTareas()`, la fuente de verdad) con el estado de
 * carga y de error, que siguen viviendo solo en el ViewModel.
 */
class TareasViewModel(application: Application) : AndroidViewModel(application) {

    private val tareasRemoteRepository = TareasRemoteRepository()

    private val tareasLocalRepository = TareasLocalRepository(
        TareaDatabase.obtenerInstancia(application).tareaDao()
    )

    private val gestorDeTareas = GestorDeTareas(tareasLocalRepository)

    private val _cargando = MutableStateFlow(false)
    private val _mensajeError = MutableStateFlow<String?>(null)

    /**
     * Estado de UI de solo lectura. Combina tres fuentes: las tareas que
     * llegan de Room (siempre actualizadas), y el estado de carga/error que
     * administra este ViewModel. `stateIn` convierte ese combinado en un
     * `StateFlow` "caliente" que sigue vivo mientras haya al menos un
     * observador (con un margen de 5 segundos para sobrevivir a rotaciones
     * de pantalla, `SharingStarted.WhileSubscribed(5000)`).
     */
    val uiState: StateFlow<TareasUiState> = combine(
        gestorDeTareas.observarTareas(),
        _cargando,
        _mensajeError
    ) { tareas, cargando, mensajeError ->
        TareasUiState(tareas = tareas, cargando = cargando, mensajeError = mensajeError)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TareasUiState(cargando = true)
    )

    init {
        sincronizarConRepositorioRemotoSiHaceFalta()
    }

    /**
     * Agrega una tarea nueva si el titulo es valido (ver [tituloValido]).
     * Devuelve `true` si la operacion se acepto, o `false` si se rechazo por
     * una validacion (en ese caso, `uiState.mensajeError` queda con el motivo).
     */
    fun agregarTarea(titulo: String): Boolean {
        if (!tituloValido(titulo)) {
            _mensajeError.value = "El titulo debe tener al menos $LONGITUD_MINIMA_TITULO caracteres."
            return false
        }
        viewModelScope.launch {
            gestorDeTareas.agregarTarea(titulo)
            _mensajeError.value = null
        }
        return true
    }

    /**
     * Alterna el estado completada/pendiente de una tarea.
     *
     * Sesion 24 (depuracion): recibe la [tarea] completa (tal como la
     * muestra la UI en ese momento) en vez de solo su id, para evitar la
     * condicion de carrera de "leer antes de escribir" que tenia
     * `GestorDeTareas.alternarCompletada` cuando volvia a consultar Room por
     * su cuenta antes de alternar el estado. Ver el comentario en
     * [GestorDeTareas.alternarCompletada] para el detalle del bug.
     */
    fun alternarCompletada(tarea: Tarea) {
        viewModelScope.launch {
            gestorDeTareas.alternarCompletada(tarea)
        }
    }

    /**
     * Trae tareas de ejemplo desde el repositorio remoto solo la primera vez
     * que la app se ejecuta (cuando la tabla de Room todavia esta vacia).
     *
     * Esto es importante porque, a diferencia de las sesiones anteriores
     * (donde `GestorDeTareas` vivia en memoria y se reiniciaba en cada
     * apertura de la app), ahora los datos quedan guardados en Room: si se
     * volviera a insertar la lista remota en cada inicio, se duplicarian las
     * tareas cada vez que se abre la app.
     */
    private fun sincronizarConRepositorioRemotoSiHaceFalta() {
        viewModelScope.launch {
            val hayTareasGuardadas = tareasLocalRepository.obtenerTareas().isNotEmpty()
            if (hayTareasGuardadas) return@launch

            _cargando.value = true
            when (val resultado = tareasRemoteRepository.obtenerTareasRemotas()) {
                is Resultado.Exito -> {
                    for (tareaRemota in resultado.datos) {
                        gestorDeTareas.agregarTarea(
                            titulo = tareaRemota.title,
                            completadaInicial = tareaRemota.completed
                        )
                    }
                }
                is Resultado.Error -> {
                    _mensajeError.value = resultado.mensaje
                }
            }
            _cargando.value = false
        }
    }
}
