package com.upb.taskmanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.upb.taskmanager.ui.screens.TareaDetailScreen
import com.upb.taskmanager.ui.screens.TareaListScreen
import com.upb.taskmanager.viewmodel.TareasViewModel

/** Nombres de ruta usados por el grafo de navegacion de la app. */
object Rutas {
    const val LISTA = "lista"
    const val DETALLE = "detalle/{tareaId}"

    /** Construye la ruta concreta de detalle para una tarea con el [tareaId] dado. */
    fun detalleDeTarea(tareaId: Int): String = "detalle/$tareaId"
}

/**
 * Sesion 08: Navigation Compose.
 *
 * Grafo de navegacion principal de la app, con dos rutas:
 * - "lista": la lista de tareas ([TareaListScreen]).
 * - "detalle/{tareaId}": el detalle de una tarea especifica, donde `tareaId`
 *   es un argumento de navegacion de tipo entero.
 *
 * Sesion 12: el [TareasViewModel] se crea una sola vez aqui (a nivel del
 * grafo de navegacion) y se comparte explicitamente entre la ruta de lista y
 * la de detalle, para que ambas pantallas vean siempre las mismas tareas.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val tareasViewModel: TareasViewModel = viewModel()

    NavHost(navController = navController, startDestination = Rutas.LISTA) {
        composable(Rutas.LISTA) {
            TareaListScreen(
                onTareaClick = { id -> navController.navigate(Rutas.detalleDeTarea(id)) },
                tareasViewModel = tareasViewModel
            )
        }
        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("tareaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tareaId = backStackEntry.arguments?.getInt("tareaId") ?: -1
            val uiState by tareasViewModel.uiState.collectAsState()
            val tareaSeleccionada = uiState.tareas.find { it.id == tareaId }
            TareaDetailScreen(
                tarea = tareaSeleccionada,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
