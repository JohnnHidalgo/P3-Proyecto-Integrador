package com.upb.taskmanager.util

/**
 * Sesion 16: validaciones y manejo de errores.
 *
 * Envoltorio generico para el resultado de una operacion que puede fallar
 * (por ejemplo, una llamada de red). En vez de lanzar excepciones hacia
 * arriba o devolver `null` sin explicar la causa, quien llama recibe siempre
 * un [Resultado.Exito] con los datos o un [Resultado.Error] con un mensaje
 * legible para mostrar en la UI.
 */
sealed class Resultado<out T> {
    data class Exito<out T>(val datos: T) : Resultado<T>()
    data class Error(val mensaje: String) : Resultado<Nothing>()
}
