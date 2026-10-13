package com.upb.pokeapi.model

/*
 * Modelo del dominio: lo que la app entiende por "pokemon". Es independiente del JSON de la
 * API (DTOs) y de la interfaz (Compose). Si la API cambia, solo se ajusta el mapeo.
 */

/** Version liviana para la lista (la API de listado solo entrega nombre y url). */
data class PokemonResumen(
    val id: Int,
    val nombre: String,
    val imagenUrl: String
)

/** Version completa para la pantalla de detalle. */
data class PokemonDetalle(
    val id: Int,
    val nombre: String,
    val imagenUrl: String,
    val alturaMetros: Double,
    val pesoKilos: Double,
    val tipos: List<String>,
    val estadisticas: List<Estadistica>
)

data class Estadistica(
    val nombre: String,
    val valor: Int
)
