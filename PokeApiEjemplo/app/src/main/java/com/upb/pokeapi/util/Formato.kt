package com.upb.pokeapi.util

/** 25 -> "#025" */
fun formatearNumero(id: Int): String = "#" + id.toString().padStart(3, '0')

/** "mr-mime" -> "Mr Mime" */
fun capitalizar(texto: String): String =
    texto.replace('-', ' ')
        .split(' ')
        .joinToString(" ") { palabra -> palabra.replaceFirstChar { it.uppercase() } }

/** La API devuelve los nombres de las estadisticas en ingles ("special-attack"). */
fun traducirEstadistica(nombre: String): String = when (nombre) {
    "hp" -> "PS"
    "attack" -> "Ataque"
    "defense" -> "Defensa"
    "special-attack" -> "Ataque esp."
    "special-defense" -> "Defensa esp."
    "speed" -> "Velocidad"
    else -> capitalizar(nombre)
}
