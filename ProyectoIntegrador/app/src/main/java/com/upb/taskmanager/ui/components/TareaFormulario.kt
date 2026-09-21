package com.upb.taskmanager.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upb.taskmanager.ui.theme.TaskManagerTheme

/**
 * Sesion 25: refactorizacion - composables reutilizables.
 *
 * Formulario para agregar una tarea nueva (campo de texto + boton). Antes
 * este `Row` estaba escrito directamente dentro de `TareaListContent`; se
 * extrae aqui como su propio composable para que cualquier pantalla que
 * necesite un formulario de "agregar tarea" (por ejemplo, una futura
 * pantalla de edicion) lo reutilice en vez de volver a escribirlo.
 */
@Composable
fun TareaFormulario(
    valor: String,
    onValorCambiado: (String) -> Unit,
    onAgregar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = valor,
            onValueChange = onValorCambiado,
            modifier = Modifier.weight(1f),
            label = { Text("Nueva tarea") }
        )
        Button(onClick = onAgregar) {
            Text("Agregar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TareaFormularioPreview() {
    TaskManagerTheme {
        TareaFormulario(valor = "", onValorCambiado = {}, onAgregar = {})
    }
}
