package com.upb.taskmanager.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Sesion 06: Material Design 3.
 *
 * Esquemas de color propios de la app, en version clara y oscura. En
 * dispositivos con Android 12+ se puede usar color dinamico (basado en el
 * fondo de pantalla del usuario); en versiones anteriores se usa siempre la
 * paleta propia definida en Color.kt.
 */
private val EsquemaClaro = lightColorScheme(
    primary = VerdePrimario,
    onPrimary = FondoClaro,
    secondary = VerdeSecundario,
    onSecondary = FondoClaro,
    tertiary = NaranjaTerciario,
    onTertiary = FondoClaro,
    background = FondoClaro,
    onBackground = TextoSobreClaro,
    surface = SuperficieClara,
    onSurface = TextoSobreClaro,
    error = ColorError
)

private val EsquemaOscuro = darkColorScheme(
    primary = VerdePrimarioClaro,
    onPrimary = VerdePrimarioOscuro,
    secondary = VerdeSecundarioClaro,
    onSecondary = VerdeSecundarioOscuro,
    tertiary = NaranjaTerciarioClaro,
    onTertiary = NaranjaTerciarioOscuro,
    background = FondoOscuro,
    onBackground = TextoSobreOscuro,
    surface = SuperficieOscura,
    onSurface = TextoSobreOscuro,
    error = ColorErrorOscuro
)

/**
 * Tema Material 3 de "Task Manager UPB". Reemplaza el uso generico de
 * `MaterialTheme { ... }` en toda la app.
 *
 * @param useDarkTheme si se debe usar el esquema oscuro; por defecto sigue la
 *   configuracion del sistema.
 * @param colorDinamico si se debe usar color dinamico de Material You en
 *   dispositivos compatibles (Android 12+).
 */
@Composable
fun TaskManagerTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    colorDinamico: Boolean = true,
    content: @Composable () -> Unit
) {
    val esquemaDeColor = when {
        colorDinamico && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val contexto = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(contexto) else dynamicLightColorScheme(contexto)
        }
        useDarkTheme -> EsquemaOscuro
        else -> EsquemaClaro
    }

    val vista = LocalView.current
    if (!vista.isInEditMode) {
        SideEffect {
            val ventana = (vista.context as Activity).window
            ventana.statusBarColor = esquemaDeColor.primary.toArgb()
            WindowCompat.getInsetsController(ventana, vista).isAppearanceLightStatusBars = !useDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = esquemaDeColor,
        typography = TipografiaTaskManager,
        content = content
    )
}
