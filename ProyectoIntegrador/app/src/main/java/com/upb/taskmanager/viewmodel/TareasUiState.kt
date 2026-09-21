package com.upb.taskmanager.viewmodel

import com.upb.taskmanager.model.Tarea

/**
 * Sesion 13: ViewModel + estado con StateFlow.
 *
 * Representa todo el estado que necesita la UI de la lista de tareas en un
 * solo objeto inmutable: la lista de tareas actual y si hay una operacion en
 * curso (`cargando`). Modelar el estado como una sola data class facilita
 * razonar sobre la UI: en todo momento hay un unico "TareasUiState" valido.
 */
data class TareasUiState(
    val tareas: List<Tarea> = emptyList(),
    val cargando: Boolean = false
)
