package com.upb.pokeapi.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.upb.pokeapi.model.Estadistica
import com.upb.pokeapi.model.PokemonDetalle
import com.upb.pokeapi.ui.components.MensajeDeError
import com.upb.pokeapi.ui.components.TipoChip
import com.upb.pokeapi.ui.theme.PokedexTheme
import com.upb.pokeapi.util.capitalizar
import com.upb.pokeapi.util.formatearNumero
import com.upb.pokeapi.util.traducirEstadistica

/** Valor maximo habitual de una estadistica base; sirve para dibujar la barra de progreso. */
private const val MAXIMO_ESTADISTICA = 255f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    onVolver: () -> Unit,
    viewModel: PokemonDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val detalle = uiState.detalle
    val error = uiState.error

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(detalle?.let { capitalizar(it.nombre) } ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingInterno ->
        val modifier = Modifier.padding(paddingInterno)
        when {
            uiState.cargando -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                MensajeDeError(
                    mensaje = error,
                    onReintentar = viewModel::cargar,
                    modifier = modifier.fillMaxSize()
                )
            }
            detalle != null -> PokemonDetailContent(detalle = detalle, modifier = modifier)
            else -> Unit
        }
    }
}

@Composable
fun PokemonDetailContent(
    detalle: PokemonDetalle,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = detalle.imagenUrl,
            contentDescription = capitalizar(detalle.nombre),
            modifier = Modifier.size(240.dp)
        )
        Text(
            text = formatearNumero(detalle.id),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = capitalizar(detalle.nombre),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            detalle.tipos.forEach { tipo -> TipoChip(tipo = tipo) }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DatoFisico(titulo = "Altura", valor = "%.1f m".format(detalle.alturaMetros))
            DatoFisico(titulo = "Peso", valor = "%.1f kg".format(detalle.pesoKilos))
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Estadísticas base",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))
        detalle.estadisticas.forEach { estadistica ->
            FilaEstadistica(estadistica = estadistica)
        }
    }
}

@Composable
private fun DatoFisico(titulo: String, valor: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = valor,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = titulo,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FilaEstadistica(estadistica: Estadistica) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = traducirEstadistica(estadistica.nombre),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(104.dp)
        )
        Text(
            text = estadistica.valor.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(40.dp)
        )
        LinearProgressIndicator(
            progress = (estadistica.valor / MAXIMO_ESTADISTICA).coerceIn(0f, 1f),
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonDetailContentPreview() {
    PokedexTheme {
        PokemonDetailContent(
            detalle = PokemonDetalle(
                id = 25,
                nombre = "pikachu",
                imagenUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
                alturaMetros = 0.4,
                pesoKilos = 6.0,
                tipos = listOf("electric"),
                estadisticas = listOf(
                    Estadistica("hp", 35),
                    Estadistica("attack", 55),
                    Estadistica("defense", 40),
                    Estadistica("special-attack", 50),
                    Estadistica("special-defense", 50),
                    Estadistica("speed", 90)
                )
            )
        )
    }
}
