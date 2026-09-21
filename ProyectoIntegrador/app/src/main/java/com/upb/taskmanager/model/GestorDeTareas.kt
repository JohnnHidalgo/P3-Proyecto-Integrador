package com.upb.taskmanager.model

import com.upb.taskmanager.data.local.TareaEntity
import com.upb.taskmanager.data.local.TareasLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Sesion 03: Programacion Orientada a Objetos.
 *
 * Clase que encapsula la logica de negocio para administrar tareas. El
 * nombre de la clase y su rol dentro del proyecto se mantienen iguales
 * durante todo el curso, aunque su implementacion interna cambia:
 *
 * - Sesiones 3 a 22: lista mutable en memoria (los datos se perdian al
 *   cerrar la app).
 * - Sesion 23 (esta version): delega toda la persistencia en un
 *   [TareasLocalRepository] respaldado por Room. Por eso sus metodos de
 *   escritura pasan a ser `suspend fun` (para no bloquear el hilo principal
 *   mientras se accede a la base de datos) y la lectura se expone como un
 *   [Flow] en vez de una lista simple.
 */
class GestorDeTareas(private val tareasLocalRepository: TareasLocalRepository) {

    /**
     * Observa la lista de tareas actual. Room emite un valor nuevo por este
     * `Flow` automaticamente cada vez que la tabla "tareas" cambia (al
     * agregar, eliminar o actualizar una fila), sin que quien lo observa
     * tenga que volver a pedir los datos.
     */
    fun observarTareas(): Flow<List<Tarea>> =
        tareasLocalRepository.observarTareas().map { entidades -> entidades.map { it.aTarea() } }

    /**
     * Agrega una tarea nueva a partir de su titulo.
     *
     * @param completadaInicial permite crear la tarea ya marcada como
     *   completada; se usa al sincronizar tareas de ejemplo desde el
     *   repositorio remoto (Sesion 15), donde el servicio ya indica si la
     *   tarea esta terminada.
     */
    suspend fun agregarTarea(titulo: String, completadaInicial: Boolean = false): Tarea {
        val entidadCreada = tareasLocalRepository.agregarTarea(titulo)
        if (completadaInicial) {
            tareasLocalRepository.alternarCompletada(entidadCreada)
            return entidadCreada.copy(completada = true).aTarea()
        }
        return entidadCreada.aTarea()
    }

    /**
     * Elimina la tarea con el [id] indicado, si existe. Sigue aplicando null
     * safety con `find`: si no se encuentra ninguna tarea con ese id, no
     * hace nada.
     */
    suspend fun eliminarTarea(id: Int) {
        val entidadEncontrada = buscarEntidadPorId(id) ?: return
        tareasLocalRepository.eliminarTarea(entidadEncontrada)
    }

    /** Alterna el estado `completada` de la tarea con el [id] indicado. */
    suspend fun alternarCompletada(id: Int) {
        val entidadEncontrada = buscarEntidadPorId(id) ?: return
        tareasLocalRepository.alternarCompletada(entidadEncontrada)
    }

    private suspend fun buscarEntidadPorId(id: Int): TareaEntity? =
        tareasLocalRepository.obtenerTareas().find { it.id == id }
}

private fun TareaEntity.aTarea(): Tarea = Tarea(id = id, titulo = titulo, completada = completada)
