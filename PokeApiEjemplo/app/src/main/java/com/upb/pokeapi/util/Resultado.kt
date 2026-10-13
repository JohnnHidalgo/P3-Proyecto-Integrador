package com.upb.pokeapi.util

/**
 * Envuelve el resultado de una operacion que puede fallar (por ejemplo, una llamada de red).
 *
 * El repositorio nunca deja escapar excepciones hacia el ViewModel: siempre devuelve
 * [Exito] con los datos o [Error] con un mensaje listo para mostrar al usuario.
 */
sealed class Resultado<out T> {
    data class Exito<out T>(val datos: T) : Resultado<T>()
    data class Error(val mensaje: String) : Resultado<Nothing>()
}
