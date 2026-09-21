package com.upb.taskmanager.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Sesion 20: Room - persistencia local.
 *
 * DAO (Data Access Object) de la tabla "tareas". Room genera automaticamente
 * la implementacion de esta interfaz a partir de las anotaciones.
 *
 * Sesion 21: se completa el CRUD (Create, Read, Update, Delete) agregando
 * `insertar`, `actualizar` y `eliminar`. Todas son funciones `suspend`
 * porque Room ejecuta el acceso a la base de datos fuera del hilo principal.
 */
@Dao
interface TareaDao {

    @Query("SELECT * FROM tareas ORDER BY id ASC")
    suspend fun obtenerTodas(): List<TareaEntity>

    /** Inserta una tarea nueva. Devuelve el id autogenerado por SQLite. */
    @Insert
    suspend fun insertar(tarea: TareaEntity): Long

    /** Actualiza una tarea existente (Room la identifica por su `id`). */
    @Update
    suspend fun actualizar(tarea: TareaEntity)

    /** Elimina una tarea existente. */
    @Delete
    suspend fun eliminar(tarea: TareaEntity)
}
