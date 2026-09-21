package com.upb.taskmanager.model

/**
 * Sesion 03: Programacion Orientada a Objetos.
 *
 * Clase que encapsula la logica de negocio para administrar tareas. Durante
 * esta sesion guarda las tareas en una lista mutable en memoria; en sesiones
 * posteriores (23) su implementacion interna cambiara a usar Room, pero el
 * nombre de la clase y su rol dentro del proyecto se mantienen igual durante
 * todo el curso.
 */
class GestorDeTareas {

    private val tareas: MutableList<Tarea> = mutableListOf()
    private var siguienteId: Int = 1

    /** Devuelve una copia inmutable de la lista actual de tareas. */
    fun obtenerTareas(): List<Tarea> = tareas.toList()

    /**
     * Agrega una tarea nueva a partir de su titulo. El id se genera de forma
     * automatica e incremental para que quien llama no tenga que preocuparse
     * por colisiones de id.
     */
    fun agregarTarea(titulo: String): Tarea {
        val tareaNueva = Tarea(id = siguienteId, titulo = titulo)
        tareas.add(tareaNueva)
        siguienteId++
        return tareaNueva
    }

    /**
     * Elimina la tarea con el [id] indicado, si existe. Usa null safety: si
     * no se encuentra ninguna tarea con ese id, simplemente no hace nada.
     */
    fun eliminarTarea(id: Int) {
        tareas.removeAll { it.id == id }
    }

    /**
     * Alterna el estado `completada` de la tarea con el [id] indicado.
     * Busca la tarea con `find` (que retorna `Tarea?`) y usa el operador
     * de llamada segura `?.let` para modificarla solo si existe.
     */
    fun alternarCompletada(id: Int) {
        val tareaEncontrada: Tarea? = tareas.find { it.id == id }
        tareaEncontrada?.let { tarea ->
            val indice = tareas.indexOf(tarea)
            tareas[indice] = tarea.copy(completada = !tarea.completada)
        }
    }
}
