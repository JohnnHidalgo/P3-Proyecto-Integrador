package com.upb.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Sesion 01: configuracion del entorno y primer proyecto con Jetpack Compose.
 *
 * Este proyecto ("Task Manager UPB") es el proyecto integrador de la materia
 * Programacion III y va creciendo sesion a sesion. Cada commit del repositorio
 * corresponde a los contenidos vistos en una sesion del silabo.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    PantallaBienvenida()
                }
            }
        }
    }
}

@Composable
fun PantallaBienvenida() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Task Manager UPB",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Proyecto integrador - Programacion III",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaBienvenidaPreview() {
    MaterialTheme {
        PantallaBienvenida()
    }
}
