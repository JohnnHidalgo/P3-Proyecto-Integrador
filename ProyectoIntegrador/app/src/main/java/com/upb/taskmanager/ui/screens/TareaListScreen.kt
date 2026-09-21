package com.upb.taskmanager.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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

/**
 * Sesion 05: componentes basicos de Compose (Scaffold, TextField, Button, Card).
 *
 * Esta pantalla ahora es interactiva: permite agregar tareas nuevas y las
 * muestra en una lista. El estado se mantiene con `remember` (todavia sin
 * ViewModel, eso llega en la Sesion 12); el [GestorDeTareas] vive mientras
 * la pantalla este en composicion.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaListScreen() {
    val gestorDeTareas = remember { GestorDeTareas() }
    var tareas by remember { mutableStateOf(gestorDeTareas.obtenerTareas()) }
    var textoNuevaTarea by remember { mutableStateOf("") }

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
                    onValueChange = { textoNuevaTarea = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Nueva tarea") }
                )
                Button(onClick = {
                    if (textoNuevaTarea.isNotBlank()) {
                        gestorDeTareas.agregarTarea(textoNuevaTarea)
                        tareas = gestorDeTareas.obtenerTareas()
                        textoNuevaTarea = ""
                    }
                }) {
                    Text("Agregar")
                }
            }

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (tarea in tareas) {
                    TareaCardBasica(
                        tarea = tarea,
                        onCambiarCompletada = {
                            gestorDeTareas.alternarCompletada(tarea.id)
                            tareas = gestorDeTareas.obtenerTareas()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TareaCardBasica(tarea: Tarea, onCambiarCompletada: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
fun TareaListScreenPreview() {
    MaterialTheme {
        TareaListScreen()
    }
}
