package com.upb.taskmanager.data.local

/**
 * Sesion 21: CRUD con Room.
 *
 * Repositorio local: envuelve el [TareaDao] y expone operaciones con
 * nombres orientados al dominio (`agregarTarea`, `alternarCompletada`) en
 * vez de exponer directamente `insertar`/`actualizar`/`eliminar`. Esto deja
 * la puerta abierta para que, en la Sesion 23, [com.upb.taskmanager.model.GestorDeTareas]
 * use este repositorio como su fuente de datos sin que el resto de la app
 * note el cambio de una lista en memoria a una base de datos real.
 */
class TareasLocalRepository(private val tareaDao: TareaDao) {

    suspend fun obtenerTareas(): List<TareaEntity> = tareaDao.obtenerTodas()

    /** Inserta una tarea nueva y devuelve la entidad ya con su id autogenerado. */
    suspend fun agregarTarea(titulo: String): TareaEntity {
        val entidadNueva = TareaEntity(titulo = titulo)
        val idGenerado = tareaDao.insertar(entidadNueva)
        return entidadNueva.copy(id = idGenerado.toInt())
    }

    suspend fun eliminarTarea(tarea: TareaEntity) {
        tareaDao.eliminar(tarea)
    }

    /** Alterna el estado completada/pendiente de la [tarea] recibida. */
    suspend fun alternarCompletada(tarea: TareaEntity) {
        tareaDao.actualizar(tarea.copy(completada = !tarea.completada))
    }
}
