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

    /**
     * Alterna el estado `completada` de la [tarea] recibida.
     *
     * Sesion 24 (depuracion): esta funcion originalmente recibia solo el
     * `id` de la tarea (`alternarCompletada(id: Int)`) y, antes de alternar
     * el estado, volvia a leer la tarea completa desde Room con
     * `buscarEntidadPorId(id)`. Esa relectura abria una ventana de condicion
     * de carrera (un problema clasico de "leer antes de escribir"): si el
     * usuario tocaba el mismo checkbox dos veces muy rapido, la UI lanzaba
     * dos corrutinas casi al mismo tiempo y las dos podian leer
     * `completada = false` ANTES de que la primera terminara de escribir
     * `true`; el resultado neto era que la tarea quedaba marcada como
     * completada en vez de volver a quedar pendiente en el segundo toque.
     *
     * Se detecto poniendo un breakpoint (o un `Log.d`) dentro de
     * [com.upb.taskmanager.data.local.TareasLocalRepository.alternarCompletada]
     * e inspeccionando el valor de `tarea.completada` en cada llamada: con
     * dos toques rapidos, Logcat mostraba `completada = false` en las DOS
     * llamadas, en vez de `false` y despues `true`.
     *
     * La correccion evita la relectura: en vez de volver a consultar Room,
     * usa directamente la [tarea] que la UI ya tenia (el ultimo valor
     * observado del `Flow` de Room que [observarTareas] expone), asi que no
     * queda ninguna consulta adicional que se pueda quedar desactualizada.
     */
    suspend fun alternarCompletada(tarea: Tarea) {
        tareasLocalRepository.alternarCompletada(tarea.aEntidad())
    }

    private suspend fun buscarEntidadPorId(id: Int): TareaEntity? =
        tareasLocalRepository.obtenerTareas().find { it.id == id }
}

private fun TareaEntity.aTarea(): Tarea = Tarea(id = id, titulo = titulo, completada = completada)
private fun Tarea.aEntidad(): TareaEntity = TareaEntity(id = id, titulo = titulo, completada = completada)
