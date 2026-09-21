package com.upb.taskmanager.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Sesion 20: Room - persistencia local.
 *
 * Entidad de Room que representa una fila de la tabla "tareas" en la base de
 * datos local (SQLite) del dispositivo. Se mantiene separada del modelo de
 * dominio [com.upb.taskmanager.model.Tarea] a proposito: la entidad conoce
 * detalles de persistencia (anotaciones de Room, autogeneracion del id) que
 * el resto de la app no necesita conocer.
 */
@Entity(tableName = "tareas")
data class TareaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val completada: Boolean = false
)
