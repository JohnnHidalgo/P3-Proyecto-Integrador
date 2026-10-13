package com.upb.pokeapi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = Color(0xFFCC0000),
    onPrimary = Color.White,
    secondary = Color(0xFF3B4CCA),
    tertiary = Color(0xFFFFCB05),
    background = Color(0xFFFFFBFA),
    surface = Color(0xFFFFFBFA)
)

private val EsquemaOscuro = darkColorScheme(
    primary = Color(0xFFFF6B6B),
    onPrimary = Color(0xFF3B0000),
    secondary = Color(0xFF9DA8FF),
    tertiary = Color(0xFFFFCB05)
)

@Composable
fun PokedexTheme(
    modoOscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (modoOscuro) EsquemaOscuro else EsquemaClaro,
        content = content
    )
}
