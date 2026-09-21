package com.upb.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.upb.taskmanager.ui.screens.TareaListScreen

/**
 * Sesion 01: configuracion del entorno y primer proyecto con Jetpack Compose.
 *
 * Este proyecto ("Task Manager UPB") es el proyecto integrador de la materia
 * Programacion III y va creciendo sesion a sesion. Cada commit del repositorio
 * corresponde a los contenidos vistos en una sesion del silabo.
 *
 * Sesion 04: la pantalla de bienvenida inicial se reemplaza por la primera
 * version de la lista de tareas ([TareaListScreen]).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    TareaListScreen()
                }
            }
        }
    }
}
