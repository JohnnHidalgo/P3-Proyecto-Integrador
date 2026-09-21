package com.upb.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.upb.taskmanager.navigation.AppNavigation
import com.upb.taskmanager.ui.theme.TaskManagerTheme

/**
 * Sesion 01: configuracion del entorno y primer proyecto con Jetpack Compose.
 *
 * Este proyecto ("Task Manager UPB") es el proyecto integrador de la materia
 * Programacion III y va creciendo sesion a sesion. Cada commit del repositorio
 * corresponde a los contenidos vistos en una sesion del silabo.
 *
 * Sesion 04: la pantalla de bienvenida inicial se reemplaza por la primera
 * version de la lista de tareas.
 *
 * Sesion 06: el `MaterialTheme` generico se reemplaza por [TaskManagerTheme],
 * el tema Material 3 propio de la app (colores y tipografia personalizados).
 *
 * Sesion 08: se agrega Navigation Compose; la pantalla unica se reemplaza por
 * [AppNavigation], que administra el grafo de navegacion entre la lista de
 * tareas y el detalle de una tarea.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskManagerTheme {
                Surface {
                    AppNavigation()
                }
            }
        }
    }
}
