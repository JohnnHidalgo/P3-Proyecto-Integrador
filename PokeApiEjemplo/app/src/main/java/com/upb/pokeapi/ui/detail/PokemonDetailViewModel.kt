package com.upb.pokeapi.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upb.pokeapi.data.PokemonRepository
import com.upb.pokeapi.model.PokemonDetalle
import com.upb.pokeapi.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PokemonDetailUiState(
    val detalle: PokemonDetalle? = null,
    val cargando: Boolean = true,
    val error: String? = null
)

/**
 * ViewModel del detalle. El nombre del pokemon llega como argumento de navegacion
 * y Navigation Compose lo deja disponible en el [SavedStateHandle].
 */
class PokemonDetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val repositorio = PokemonRepository()

    private val nombre: String = checkNotNull(savedStateHandle.get<String>(ARG_NOMBRE))

    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        _uiState.update { it.copy(cargando = true, error = null) }

        viewModelScope.launch {
            when (val resultado = repositorio.obtenerPokemon(nombre)) {
                is Resultado.Exito -> _uiState.update {
                    it.copy(detalle = resultado.datos, cargando = false)
                }
                is Resultado.Error -> _uiState.update {
                    it.copy(cargando = false, error = resultado.mensaje)
                }
            }
        }
    }

    companion object {
        const val ARG_NOMBRE = "nombre"
    }
}
