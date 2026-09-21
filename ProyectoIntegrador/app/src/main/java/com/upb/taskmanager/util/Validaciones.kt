package com.upb.taskmanager.util

import com.upb.taskmanager.model.Tarea

/**
 * Sesion 02: fundamentos de Kotlin (variables, tipos y funciones).
 *
 * Estas son las primeras funciones "puras" del proyecto integrador: no dependen
 * todavia de clases propias (eso llega en la Sesion 03 con `Tarea` y
 * `GestorDeTareas`), solo usan tipos basicos de Kotlin. Se organizan en el
 * paquete `util` porque son utilidades reutilizables desde cualquier capa.
 *
 * Sesion 03: una vez que existe el modelo `Tarea`, se agrega [contarPendientes]
 * para mostrar el uso de colecciones (`List`) y funciones de orden superior.
 */

/** Longitud minima que debe tener el titulo de una tarea para considerarse valido. */
const val LONGITUD_MINIMA_TITULO: Int = 3

/**
 * Indica si un titulo de tarea es valido: no puede estar vacio (ni ser solo
 * espacios en blanco) y debe tener al menos [LONGITUD_MINIMA_TITULO] caracteres.
 */
fun tituloValido(titulo: String): Boolean {
    val tituloSinEspacios = titulo.trim()
    return tituloSinEspacios.isNotEmpty() && tituloSinEspacios.length >= LONGITUD_MINIMA_TITULO
}

/**
 * Construye una descripcion legible para una tarea nueva, usando un parametro
 * con valor por defecto para la prioridad (demuestra parametros con nombre).
 */
fun crearDescripcionTarea(titulo: String, prioridad: String = "media"): String {
    return "Tarea: '$titulo' - Prioridad: $prioridad"
}

/**
 * Funcion de una sola expresion: indica si un numero entero es par.
 * Sirve como ejercicio introductorio de Kotlin (Bloque 6 de la Sesion 02).
 */
fun esNumeroPar(numero: Int): Boolean = numero % 2 == 0

/**
 * Calcula el promedio de tres notas. Usa `Double` para permitir decimales.
 */
fun calcularPromedio(a: Double, b: Double, c: Double): Double = (a + b + c) / 3.0

/**
 * Genera un saludo usando plantillas de texto (`string templates`) y un
 * parametro con valor por defecto para el nombre del curso.
 */
fun saludarEstudiante(nombre: String, curso: String = "Programacion III"): String {
    return "Hola $nombre, bienvenido/a a $curso"
}

/**
 * Cuenta cuantas tareas de la lista todavia no estan completadas.
 * Usa `count`, una funcion de orden superior sobre colecciones de Kotlin.
 */
fun contarPendientes(tareas: List<Tarea>): Int = tareas.count { !it.completada }
