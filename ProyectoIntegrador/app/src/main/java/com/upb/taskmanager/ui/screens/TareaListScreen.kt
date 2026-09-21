package com.upb.taskmanager.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upb.taskmanager.model.Tarea
import com.upb.taskmanager.ui.theme.TaskManagerTheme
import com.upb.taskmanager.viewmodel.TareasViewModel

/**
 * Sesion 07: layouts y estado - LazyColumn y "state hoisting".
 *
 * [TareaListScreen] es el composable "con estado" (stateful): mantiene el
 * texto del formulario y expone ese estado hacia abajo. Toda la parte visual
 * sin estado propio vive en [TareaListContent], que solo recibe datos y
 * callbacks (state hoisting): esto la hace mas facil de reutilizar y de
 * previsualizar.
 *
 * Sesion 08: se agrega el parametro opcional `onTareaClick`, que permite a
 * quien use esta pantalla navegar a un detalle (ver [com.upb.taskmanager.navigation.AppNavigation]).
 *
 * Sesion 12: en vez de crear y recordar un `GestorDeTareas` propio con
 * `remember`, la pantalla obtiene sus datos de un [TareasViewModel] (por
 * defecto, uno nuevo con `viewModel()`). Esto permite que el estado de las
 * tareas sobreviva a cambios de configuracion y se pueda compartir con otras
 * pantallas del mismo grafo de navegacion.
 *
 * Sesion 13: el ViewModel ahora expone un `StateFlow<TareasUiState>` en vez
 * de una lista suelta. La pantalla se suscribe con `collectAsState()`, que
 * convierte ese flujo en un `State` de Compose y recompone automaticamente
 * cada vez que llega un nuevo valor.
 *
 * Sesion 15: el ViewModel sincroniza tareas de ejemplo desde un repositorio
 * remoto al crearse (ver `TareasViewModel.cargarTareasRemotas`). Mientras esa
 * llamada de red esta en curso, `uiState.cargando` es `true` y esta pantalla
 * muestra un indicador de progreso.
 *
 * Sesion 16: `agregarTarea` ahora devuelve si la operacion tuvo exito; el
 * campo de texto solo se limpia cuando la tarea se agrego correctamente. Si
 * hay un `uiState.mensajeError` (por una validacion o una falla de red), esta
 * pantalla lo muestra en rojo debajo del formulario.
 *
 * Sesion 17: se agrega un filtro (todas / pendientes / completadas). La
 * lista filtrada se calcula con `derivedStateOf`, que solo vuelve a
 * ejecutar el filtro cuando `uiState.tareas` o `filtroSeleccionado` cambian
 * de verdad, en vez de recalcularse en cada recomposicion.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaListScreen(
    onTareaClick: (Int) -> Unit = {},
    tareasViewModel: TareasViewModel = viewModel()
) {
    var textoNuevaTarea by remember { mutableStateOf("") }
    var filtroSeleccionado by remember { mutableStateOf(FiltroTareas.TODAS) }
    val uiState by tareasViewModel.uiState.collectAsState()

    val tareasFiltradas by remember {
        derivedStateOf {
            when (filtroSeleccionado) {
                FiltroTareas.TODAS -> uiState.tareas
                FiltroTareas.PENDIENTES -> uiState.tareas.filter { !it.completada }
                FiltroTareas.COMPLETADAS -> uiState.tareas.filter { it.completada }
            }
        }
    }

    TareaListContent(
        tareas = tareasFiltradas,
        cargando = uiState.cargando,
        mensajeError = uiState.mensajeError,
        textoNuevaTarea = textoNuevaTarea,
        onTextoNuevaTareaCambiado = { textoNuevaTarea = it },
        onAgregarTarea = {
            val seAgrego = tareasViewModel.agregarTarea(textoNuevaTarea)
            if (seAgrego) {
                textoNuevaTarea = ""
            }
        },
        onCambiarCompletada = { id -> tareasViewModel.alternarCompletada(id) },
        onTareaClick = onTareaClick,
        filtroSeleccionado = filtroSeleccionado,
        onFiltroCambiado = { filtroSeleccionado = it }
    )
}

/** Filtros disponibles para la lista de tareas (Sesion 17). */
enum class FiltroTareas(val etiqueta: String) {
    TODAS("Todas"),
    PENDIENTES("Pendientes"),
    COMPLETADAS("Completadas")
}

/**
 * Composable sin estado propio (stateless): recibe todo lo que necesita
 * mostrar como parametros y notifica las interacciones del usuario mediante
 * callbacks (`onAgregarTarea`, `onCambiarCompletada`, `onTareaClick`), en vez
 * de manejar estado internamente.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaListContent(
    tareas: List<Tarea>,
    textoNuevaTarea: String,
    onTextoNuevaTareaCambiado: (String) -> Unit,
    onAgregarTarea: () -> Unit,
    onCambiarCompletada: (Int) -> Unit,
    onTareaClick: (Int) -> Unit = {},
    cargando: Boolean = false,
    mensajeError: String? = null,
    filtroSeleccionado: FiltroTareas = FiltroTareas.TODAS,
    onFiltroCambiado: (FiltroTareas) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Task Manager UPB") })
        }
    ) { paddingDelScaffold ->
        // Sesion 17: modifier compartido con TareaDetailScreen (ver PantallaComun.kt).
        Column(
            modifier = Modifier.contenidoDePantalla(paddingDelScaffold),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = textoNuevaTarea,
                    onValueChange = onTextoNuevaTareaCambiado,
                    modifier = Modifier.weight(1f),
                    label = { Text("Nueva tarea") }
                )
                Button(onClick = onAgregarTarea) {
                    Text("Agregar")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (filtro in FiltroTareas.values()) {
                    TextButton(onClick = { onFiltroCambiado(filtro) }) {
                        Text(
                            text = filtro.etiqueta,
                            style = if (filtro == filtroSeleccionado) {
                                MaterialTheme.typography.labelLarge
                            } else {
                                MaterialTheme.typography.bodyMedium
                            }
                        )
                    }
                }
            }

            if (mensajeError != null) {
                Text(
                    text = mensajeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (cargando) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tareas, key = { it.id }) { tarea ->
                    TareaCardBasica(
                        tarea = tarea,
                        onCambiarCompletada = { onCambiarCompletada(tarea.id) },
                        onClick = { onTareaClick(tarea.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TareaCardBasica(
    tarea: Tarea,
    onCambiarCompletada: () -> Unit,
    onClick: () -> Unit = {}
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = tarea.completada, onCheckedChange = { onCambiarCompletada() })
            Text(text = tarea.titulo)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TareaListContentPreview() {
    TaskManagerTheme {
        TareaListContent(
            tareas = listOf(
                Tarea(id = 1, titulo = "Repasar LazyColumn", completada = true),
                Tarea(id = 2, titulo = "Aplicar state hoisting", completada = false)
            ),
            textoNuevaTarea = "",
            onTextoNuevaTareaCambiado = {},
            onAgregarTarea = {},
            onCambiarCompletada = {}
        )
    }
}
