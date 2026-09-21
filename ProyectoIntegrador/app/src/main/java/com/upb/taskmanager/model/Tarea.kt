package com.upb.taskmanager.model

/**
 * Sesion 03: Programacion Orientada a Objetos.
 *
 * Modelo de dominio principal del proyecto integrador. Se define como
 * `data class` porque solo representa datos (Kotlin genera automaticamente
 * `equals`, `hashCode`, `toString` y `copy`).
 *
 * En sesiones posteriores este modelo va a crecer (por ejemplo con
 * `descripcion` y `prioridad`), pero mantiene siempre estos tres campos para
 * no romper el codigo de sesiones anteriores.
 */
data class Tarea(
    val id: Int,
    val titulo: String,
    val completada: Boolean = false
)
