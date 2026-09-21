package com.upb.taskmanager.data.local

import androidx.room.Dao
import androidx.room.Query

/**
 * Sesion 20: Room - persistencia local.
 *
 * DAO (Data Access Object) de la tabla "tareas". Room genera automaticamente
 * la implementacion de esta interfaz a partir de las anotaciones. Por ahora
 * solo expone la consulta de lectura; las operaciones de escritura (Insert,
 * Update, Delete) se agregan en la Sesion 21.
 */
@Dao
interface TareaDao {

    @Query("SELECT * FROM tareas ORDER BY id ASC")
    suspend fun obtenerTodas(): List<TareaEntity>
}
