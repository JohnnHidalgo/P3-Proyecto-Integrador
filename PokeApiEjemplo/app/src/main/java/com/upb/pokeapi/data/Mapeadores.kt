package com.upb.pokeapi.data

import com.upb.pokeapi.data.remote.PokemonDetailDto
import com.upb.pokeapi.data.remote.PokemonItemDto
import com.upb.pokeapi.model.Estadistica
import com.upb.pokeapi.model.PokemonDetalle
import com.upb.pokeapi.model.PokemonResumen

private const val URL_IMAGENES =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

/** La API no entrega la imagen en el listado, pero el repositorio de sprites usa el id como nombre. */
private fun urlImagenPorId(id: Int): String = "$URL_IMAGENES$id.png"

/** El id viene al final de la url: "https://pokeapi.co/api/v2/pokemon/25/" -> 25 */
fun PokemonItemDto.aResumen(): PokemonResumen {
    val id = url.trimEnd('/').substringAfterLast('/').toInt()
    return PokemonResumen(id = id, nombre = name, imagenUrl = urlImagenPorId(id))
}

fun PokemonDetailDto.aDetalle(): PokemonDetalle = PokemonDetalle(
    id = id,
    nombre = name,
    imagenUrl = urlImagenPorId(id),
    alturaMetros = height / 10.0,
    pesoKilos = weight / 10.0,
    tipos = types.sortedBy { it.slot }.map { it.type.name },
    estadisticas = stats.map { Estadistica(nombre = it.stat.name, valor = it.baseStat) }
)
