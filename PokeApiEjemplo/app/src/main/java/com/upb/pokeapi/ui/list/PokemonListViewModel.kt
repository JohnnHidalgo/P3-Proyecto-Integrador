package com.upb.pokeapi.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upb.pokeapi.data.PokemonRepository
import com.upb.pokeapi.model.PokemonResumen
import com.upb.pokeapi.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Todo lo que la pantalla de lista necesita para dibujarse, en un solo objeto inmutable. */
data class PokemonListUiState(
    val pokemones: List<PokemonResumen> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null,
    val hayMas: Boolean = true
)

/**
 * ViewModel de la lista: guarda el estado, pide los datos al repositorio y expone un
 * [StateFlow] de solo lectura. No conoce a Compose: la Vista solo observa [uiState].
 */
class PokemonListViewModel : ViewModel() {

    private val repositorio = PokemonRepository()

    private val _uiState = MutableStateFlow(PokemonListUiState())
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    init {
        cargarMas()
    }

    /** Carga la siguiente pagina. Tambien sirve como "Reintentar" tras un error. */
    fun cargarMas() {
        val estadoActual = _uiState.value
        if (estadoActual.cargando || !estadoActual.hayMas) return

        _uiState.update { it.copy(cargando = true, error = null) }

        viewModelScope.launch {
            // El desplazamiento es la cantidad de pokemones que ya tenemos.
            val resultado = repositorio.obtenerPokemones(
                limite = TAMANO_PAGINA,
                desplazamiento = estadoActual.pokemones.size
            )
            when (resultado) {
                is Resultado.Exito -> _uiState.update {
                    it.copy(
                        pokemones = it.pokemones + resultado.datos,
                        cargando = false,
                        hayMas = resultado.datos.size == TAMANO_PAGINA
                    )
                }
                is Resultado.Error -> _uiState.update {
                    it.copy(cargando = false, error = resultado.mensaje)
                }
            }
        }
    }

    private companion object {
        const val TAMANO_PAGINA = 20
    }
}
