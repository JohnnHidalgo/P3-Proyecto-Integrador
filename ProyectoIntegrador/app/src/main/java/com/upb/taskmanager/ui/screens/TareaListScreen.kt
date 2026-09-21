package com.upb.taskmanager.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upb.taskmanager.model.Tarea

/**
 * Sesion 04: introduccion a Jetpack Compose.
 *
 * Primera version de la pantalla de lista de tareas. Todavia es muy simple:
 * muestra una lista fija (hardcoded) de [Tarea] usando `Column` y `Text`,
 * sin `LazyColumn`, sin estado y sin interaccion. Esas mejoras llegan en las
 * sesiones 05 y 07.
 */
@Composable
fun TareaListScreen() {
    val tareasDeEjemplo = listOf(
        Tarea(id = 1, titulo = "Instalar Android Studio", completada = true),
        Tarea(id = 2, titulo = "Aprender Kotlin basico", completada = true),
        Tarea(id = 3, titulo = "Crear el modelo Tarea", completada = false),
        Tarea(id = 4, titulo = "Dibujar la primera pantalla con Compose", completada = false)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Task Manager UPB",
            style = MaterialTheme.typography.headlineSmall
        )
        for (tarea in tareasDeEjemplo) {
            val prefijo = if (tarea.completada) "[X]" else "[ ]"
            Text(text = "$prefijo ${tarea.titulo}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TareaListScreenPreview() {
    MaterialTheme {
        TareaListScreen()
    }
}
