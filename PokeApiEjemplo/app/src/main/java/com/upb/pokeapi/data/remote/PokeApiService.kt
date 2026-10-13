package com.upb.pokeapi.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Define los endpoints de la API. Retrofit genera la implementacion en tiempo de ejecucion.
 * Al ser funciones `suspend`, se ejecutan fuera del hilo principal dentro de una corrutina.
 *
 * Documentacion de la API: https://pokeapi.co/docs/v2
 */
interface PokeApiService {

    /** Lista paginada: `limite` elementos a partir de la posicion `desplazamiento`. */
    @GET("pokemon")
    suspend fun obtenerPokemones(
        @Query("limit") limite: Int,
        @Query("offset") desplazamiento: Int
    ): PokemonListResponse

    /** Detalle de un pokemon por nombre (o por numero). */
    @GET("pokemon/{nombre}")
    suspend fun obtenerPokemon(
        @Path("nombre") nombre: String
    ): PokemonDetailDto
}
