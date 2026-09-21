package com.upb.taskmanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.upb.taskmanager.model.GestorDeTareas
import com.upb.taskmanager.ui.screens.TareaDetailScreen
import com.upb.taskmanager.ui.screens.TareaListContent

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
 * - "lista": la lista de tareas ([TareaListContent]).
 * - "detalle/{tareaId}": el detalle de una tarea especifica, donde `tareaId`
 *   es un argumento de navegacion de tipo entero.
 *
 * El [GestorDeTareas] se crea una sola vez aqui (en el nivel del grafo de
 * navegacion) para que tanto la lista como el detalle puedan ver las mismas
 * tareas. Esto es una solucion temporal: en la Sesion 12 este estado
 * compartido pasara a vivir en un `TareasViewModel`.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val gestorDeTareas = remember { GestorDeTareas() }
    var tareas by remember { mutableStateOf(gestorDeTareas.obtenerTareas()) }
    var textoNuevaTarea by remember { mutableStateOf("") }

    NavHost(navController = navController, startDestination = Rutas.LISTA) {
        composable(Rutas.LISTA) {
            TareaListContent(
                tareas = tareas,
                textoNuevaTarea = textoNuevaTarea,
                onTextoNuevaTareaCambiado = { textoNuevaTarea = it },
                onAgregarTarea = {
                    if (textoNuevaTarea.isNotBlank()) {
                        gestorDeTareas.agregarTarea(textoNuevaTarea)
                        tareas = gestorDeTareas.obtenerTareas()
                        textoNuevaTarea = ""
                    }
                },
                onCambiarCompletada = { id ->
                    gestorDeTareas.alternarCompletada(id)
                    tareas = gestorDeTareas.obtenerTareas()
                },
                onTareaClick = { id -> navController.navigate(Rutas.detalleDeTarea(id)) }
            )
        }
        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("tareaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tareaId = backStackEntry.arguments?.getInt("tareaId") ?: -1
            val tareaSeleccionada = tareas.find { it.id == tareaId }
            TareaDetailScreen(
                tarea = tareaSeleccionada,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
