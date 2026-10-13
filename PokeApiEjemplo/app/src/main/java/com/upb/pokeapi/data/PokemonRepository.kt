package com.upb.pokeapi.data

import com.upb.pokeapi.data.remote.PokeApiService
import com.upb.pokeapi.data.remote.RetrofitClient
import com.upb.pokeapi.model.PokemonDetalle
import com.upb.pokeapi.model.PokemonResumen
import com.upb.pokeapi.util.Resultado
import java.io.IOException
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

/**
 * Capa de datos (Modelo en MVVM): unico punto de acceso a la API para el resto de la app.
 *
 * - Llama a Retrofit.
 * - Convierte los DTOs en modelos del dominio.
 * - Traduce las excepciones en un [Resultado.Error] con un mensaje entendible.
 */
class PokemonRepository(
    private val api: PokeApiService = RetrofitClient.api
) {

    suspend fun obtenerPokemones(limite: Int, desplazamiento: Int): Resultado<List<PokemonResumen>> =
        llamarApi { api.obtenerPokemones(limite, desplazamiento).results.map { it.aResumen() } }

    suspend fun obtenerPokemon(nombre: String): Resultado<PokemonDetalle> =
        llamarApi { api.obtenerPokemon(nombre).aDetalle() }

    private suspend fun <T> llamarApi(llamada: suspend () -> T): Resultado<T> =
        try {
            Resultado.Exito(llamada())
        } catch (e: CancellationException) {
            // Si la corrutina se cancela (p. ej. el usuario sale de la pantalla) NO es un error.
            throw e
        } catch (e: HttpException) {
            Resultado.Error("El servidor respondió con un error (código ${e.code()}).")
        } catch (e: IOException) {
            Resultado.Error("No hay conexión a internet. Revisa tu red e inténtalo de nuevo.")
        } catch (e: Exception) {
            Resultado.Error("Ocurrió un error inesperado: ${e.localizedMessage ?: "desconocido"}")
        }
}
