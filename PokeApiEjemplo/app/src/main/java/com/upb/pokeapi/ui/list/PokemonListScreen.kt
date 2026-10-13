package com.upb.pokeapi.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upb.pokeapi.model.PokemonResumen
import com.upb.pokeapi.ui.components.MensajeDeError
import com.upb.pokeapi.ui.components.PokemonCard
import com.upb.pokeapi.ui.theme.PokedexTheme

/**
 * Pantalla "con estado": obtiene el ViewModel, se suscribe a su `uiState` con
 * `collectAsState()` y delega el dibujo en [PokemonListContent], que es "sin estado".
 *
 * @param onPokemonClick recibe el NOMBRE del pokemon tocado (la navegacion la decide quien nos llama).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    onPokemonClick: (String) -> Unit,
    viewModel: PokemonListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pokédex") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingInterno ->
        PokemonListContent(
            uiState = uiState,
            onPokemonClick = onPokemonClick,
            onCargarMas = viewModel::cargarMas,
            modifier = Modifier.padding(paddingInterno)
        )
    }
}

/** Dibuja la lista segun el estado recibido. No conoce al ViewModel: facil de previsualizar. */
@Composable
fun PokemonListContent(
    uiState: PokemonListUiState,
    onPokemonClick: (String) -> Unit,
    onCargarMas: () -> Unit,
    modifier: Modifier = Modifier
) {
    val error = uiState.error

    when {
        // Primera carga: todavia no hay nada que mostrar.
        uiState.pokemones.isEmpty() && uiState.cargando -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        // La primera carga fallo.
        uiState.pokemones.isEmpty() && error != null -> {
            MensajeDeError(
                mensaje = error,
                onReintentar = onCargarMas,
                modifier = modifier.fillMaxSize()
            )
        }
        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.pokemones, key = { it.id }) { pokemon ->
                    PokemonCard(
                        pokemon = pokemon,
                        onClick = { onPokemonClick(pokemon.nombre) }
                    )
                }
                // Ultima "celda" ocupando todo el ancho: cargando, error o boton "Cargar mas".
                item(span = { GridItemSpan(maxLineSpan) }) {
                    PiePaginacion(uiState = uiState, onCargarMas = onCargarMas)
                }
            }
        }
    }
}

@Composable
private fun PiePaginacion(
    uiState: PokemonListUiState,
    onCargarMas: () -> Unit
) {
    val error = uiState.error

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.cargando -> CircularProgressIndicator()
            error != null -> MensajeDeError(mensaje = error, onReintentar = onCargarMas)
            uiState.hayMas -> Button(onClick = onCargarMas) { Text("Cargar más") }
            else -> Unit
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonListContentPreview() {
    val base = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
    PokedexTheme {
        PokemonListContent(
            uiState = PokemonListUiState(
                pokemones = listOf(
                    PokemonResumen(1, "bulbasaur", "${base}1.png"),
                    PokemonResumen(4, "charmander", "${base}4.png"),
                    PokemonResumen(7, "squirtle", "${base}7.png"),
                    PokemonResumen(25, "pikachu", "${base}25.png")
                )
            ),
            onPokemonClick = {},
            onCargarMas = {}
        )
    }
}
