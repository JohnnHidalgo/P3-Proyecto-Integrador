package com.upb.taskmanager.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Sesion 17: integracion/optimizacion - limpieza de codigo duplicado.
 *
 * Tanto [TareaListContent] como [TareaDetailScreen] envolvian su contenido
 * con el mismo patron repetido: ocupar toda la pantalla, aplicar el padding
 * que entrega el `Scaffold` y despues un padding interno fijo de 16dp. Se
 * extrae aqui como una funcion de extension de [Modifier] para no repetirlo
 * en cada pantalla nueva.
 */
fun Modifier.contenidoDePantalla(paddingDelScaffold: PaddingValues): Modifier =
    this
        .fillMaxSize()
        .padding(paddingDelScaffold)
        .padding(16.dp)
