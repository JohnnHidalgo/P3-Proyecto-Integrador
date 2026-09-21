package com.upb.taskmanager.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.upb.taskmanager.model.Tarea
import com.upb.taskmanager.ui.theme.TaskManagerTheme

/**
 * Sesion 08: Navigation Compose.
 *
 * Pantalla de detalle de una [Tarea]. Recibe la tarea ya resuelta (buscada a
 * partir del argumento de navegacion `tareaId`, ver
 * [com.upb.taskmanager.navigation.AppNavigation]) y un callback [onVolver]
 * para regresar a la pantalla anterior.
 *
 * [tarea] es nullable porque, en teoria, se podria navegar con un id que ya
 * no existe (por ejemplo si la tarea fue eliminada); en ese caso se muestra
 * un mensaje en vez de fallar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaDetailScreen(tarea: Tarea?, onVolver: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de la tarea") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Text("<")
                    }
                }
            )
        }
    ) { paddingDelScaffold ->
        // Sesion 17: modifier compartido con TareaListContent (ver PantallaComun.kt).
        Column(
            modifier = Modifier.contenidoDePantalla(paddingDelScaffold),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (tarea == null) {
                Text(
                    text = "No se encontro la tarea solicitada.",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(text = "ID: ${tarea.id}", style = MaterialTheme.typography.bodyMedium)
                Text(text = tarea.titulo, style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = if (tarea.completada) "Estado: completada" else "Estado: pendiente",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TareaDetailScreenPreview() {
    TaskManagerTheme {
        TareaDetailScreen(
            tarea = Tarea(id = 1, titulo = "Configurar Navigation Compose", completada = false),
            onVolver = {}
        )
    }
}
