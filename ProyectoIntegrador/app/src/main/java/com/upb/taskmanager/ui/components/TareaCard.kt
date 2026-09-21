package com.upb.taskmanager.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upb.taskmanager.model.Tarea
import com.upb.taskmanager.ui.theme.TaskManagerTheme

/**
 * Sesion 25: refactorizacion - composables reutilizables.
 *
 * Tarjeta que muestra una [Tarea] con su checkbox de completada. Antes vivia
 * como un composable privado dentro de `TareaListScreen.kt`
 * (`TareaCardBasica`); se extrae a `ui/components` para poder reutilizarla
 * tambien en [com.upb.taskmanager.ui.screens.TareaDetailScreen], que antes
 * mostraba la misma informacion con un layout de `Text` separado y
 * ligeramente distinto.
 */
@Composable
fun TareaCard(
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

@Preview(showBackground = true)
@Composable
fun TareaCardPreview() {
    TaskManagerTheme {
        TareaCard(
            tarea = Tarea(id = 1, titulo = "Extraer composables reutilizables", completada = false),
            onCambiarCompletada = {}
        )
    }
}
