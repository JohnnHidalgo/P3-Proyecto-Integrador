package com.upb.pokeapi.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.upb.pokeapi.model.PokemonResumen
import com.upb.pokeapi.util.capitalizar
import com.upb.pokeapi.util.formatearNumero

/** Tarjeta de un pokemon en la cuadricula. `AsyncImage` (Coil) descarga la imagen por URL. */
@Composable
fun PokemonCard(
    pokemon: PokemonResumen,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = pokemon.imagenUrl,
                contentDescription = capitalizar(pokemon.nombre),
                modifier = Modifier.size(120.dp)
            )
            Text(
                text = formatearNumero(pokemon.id),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = capitalizar(pokemon.nombre),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
