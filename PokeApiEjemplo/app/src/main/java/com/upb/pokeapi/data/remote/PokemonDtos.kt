package com.upb.pokeapi.data.remote

import com.google.gson.annotations.SerializedName

/*
 * DTOs (Data Transfer Objects): clases que reflejan EXACTAMENTE la forma del JSON que
 * devuelve la API. Solo declaramos los campos que la app necesita; Gson ignora el resto.
 * Los nombres estan en ingles porque son los de la API; el modelo del dominio
 * (model/Pokemon.kt) los traduce a nuestro propio vocabulario.
 */

// GET https://pokeapi.co/api/v2/pokemon?limit=20&offset=0
data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val results: List<PokemonItemDto>
)

data class PokemonItemDto(
    val name: String,
    val url: String // ej: "https://pokeapi.co/api/v2/pokemon/25/"
)

// GET https://pokeapi.co/api/v2/pokemon/pikachu
data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val height: Int, // decimetros
    val weight: Int, // hectogramos
    val types: List<TypeSlotDto>,
    val stats: List<StatSlotDto>
)

data class TypeSlotDto(
    val slot: Int,
    val type: NamedResourceDto
)

data class StatSlotDto(
    @SerializedName("base_stat") val baseStat: Int,
    val stat: NamedResourceDto
)

data class NamedResourceDto(
    val name: String,
    val url: String
)
