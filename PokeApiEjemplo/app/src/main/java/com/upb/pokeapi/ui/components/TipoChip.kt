package com.upb.pokeapi.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.upb.pokeapi.util.capitalizar

private val tiposEnEspanol = mapOf(
    "normal" to "Normal", "fire" to "Fuego", "water" to "Agua", "grass" to "Planta",
    "electric" to "Eléctrico", "ice" to "Hielo", "fighting" to "Lucha", "poison" to "Veneno",
    "ground" to "Tierra", "flying" to "Volador", "psychic" to "Psíquico", "bug" to "Bicho",
    "rock" to "Roca", "ghost" to "Fantasma", "dragon" to "Dragón", "dark" to "Siniestro",
    "steel" to "Acero", "fairy" to "Hada"
)

private val coloresPorTipo = mapOf(
    "normal" to Color(0xFFA8A77A), "fire" to Color(0xFFEE8130), "water" to Color(0xFF6390F0),
    "grass" to Color(0xFF7AC74C), "electric" to Color(0xFFF7D02C), "ice" to Color(0xFF96D9D6),
    "fighting" to Color(0xFFC22E28), "poison" to Color(0xFFA33EA1), "ground" to Color(0xFFE2BF65),
    "flying" to Color(0xFFA98FF3), "psychic" to Color(0xFFF95587), "bug" to Color(0xFFA6B91A),
    "rock" to Color(0xFFB6A136), "ghost" to Color(0xFF735797), "dragon" to Color(0xFF6F35FC),
    "dark" to Color(0xFF705746), "steel" to Color(0xFFB7B7CE), "fairy" to Color(0xFFD685AD)
)

fun traducirTipo(tipo: String): String = tiposEnEspanol[tipo] ?: capitalizar(tipo)

fun colorDelTipo(tipo: String): Color = coloresPorTipo[tipo] ?: Color.Gray

/** Etiqueta redondeada con el color del tipo ("Fuego" en naranja, "Agua" en azul, ...). */
@Composable
fun TipoChip(tipo: String, modifier: Modifier = Modifier) {
    val color = colorDelTipo(tipo)
    // Texto negro sobre colores claros (ej. Electrico) y blanco sobre colores oscuros.
    val colorTexto = if (color.luminance() > 0.5f) Color.Black else Color.White

    Surface(
        shape = RoundedCornerShape(50),
        color = color,
        modifier = modifier
    ) {
        Text(
            text = traducirTipo(tipo),
            color = colorTexto,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
        )
    }
}
