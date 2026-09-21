package com.upb.taskmanager.viewmodel

import com.upb.taskmanager.model.Tarea

/**
 * Sesion 13: ViewModel + estado con StateFlow.
 *
 * Representa todo el estado que necesita la UI de la lista de tareas en un
 * solo objeto inmutable: la lista de tareas actual y si hay una operacion en
 * curso (`cargando`). Modelar el estado como una sola data class facilita
 * razonar sobre la UI: en todo momento hay un unico "TareasUiState" valido.
 *
 * Sesion 16: se agrega `mensajeError`, que guarda el ultimo mensaje de error
 * legible (de una validacion de formulario o de una llamada de red fallida)
 * para que la UI lo muestre. Es `null` cuando no hay ningun error pendiente.
 */
data class TareasUiState(
    val tareas: List<Tarea> = emptyList(),
    val cargando: Boolean = false,
    val mensajeError: String? = null
)
