package com.upb.pokeapi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.upb.pokeapi.ui.detail.PokemonDetailScreen
import com.upb.pokeapi.ui.detail.PokemonDetailViewModel
import com.upb.pokeapi.ui.list.PokemonListScreen

private const val RUTA_LISTA = "lista"
private const val RUTA_DETALLE = "detalle"

/**
 * Grafo de navegacion: "lista" (inicio) -> "detalle/{nombre}".
 * Cada destino obtiene su propio ViewModel; el argumento `nombre` llega solo al
 * SavedStateHandle de PokemonDetailViewModel.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = RUTA_LISTA) {
        composable(RUTA_LISTA) {
            PokemonListScreen(
                onPokemonClick = { nombre -> navController.navigate("$RUTA_DETALLE/$nombre") }
            )
        }
        composable(
            route = "$RUTA_DETALLE/{${PokemonDetailViewModel.ARG_NOMBRE}}",
            arguments = listOf(
                navArgument(PokemonDetailViewModel.ARG_NOMBRE) { type = NavType.StringType }
            )
        ) {
            PokemonDetailScreen(onVolver = { navController.popBackStack() })
        }
    }
}
