package com.upb.taskmanager.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upb.taskmanager.model.GestorDeTareas
import com.upb.taskmanager.model.Tarea
import com.upb.taskmanager.ui.theme.TaskManagerTheme

/**
 * Sesion 07: layouts y estado - LazyColumn y "state hoisting".
 *
 * [TareaListScreen] es el composable "con estado" (stateful): crea y
 * recuerda el [GestorDeTareas], guarda el texto del formulario y expone ese
 * estado hacia abajo. Toda la parte visual sin estado propio vive en
 * [TareaListContent], que solo recibe datos y callbacks (state hoisting):
 * esto la hace mas facil de reutilizar y de previsualizar.
 *
 * Sesion 08: se agrega el parametro opcional `onTareaClick`, que permite a
 * quien use esta pantalla navegar a un detalle (ver [com.upb.taskmanager.navigation.AppNavigation]).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaListScreen(onTareaClick: (Int) -> Unit = {}) {
    val gestorDeTareas = remember { GestorDeTareas() }
    var tareas by remember { mutableStateOf(gestorDeTareas.obtenerTareas()) }
    var textoNuevaTarea by remember { mutableStateOf("") }

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
        onTareaClick = onTareaClick
    )
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
    onTareaClick: (Int) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Task Manager UPB") })
        }
    ) { paddingDelScaffold ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingDelScaffold)
                .padding(16.dp),
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
