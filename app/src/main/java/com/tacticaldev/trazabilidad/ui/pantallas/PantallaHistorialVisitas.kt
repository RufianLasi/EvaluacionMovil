package com.tacticaldev.trazabilidad.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tacticaldev.trazabilidad.ui.viewmodel.ViewModelVisita

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHistorialVisitas(
    viewModel: ViewModelVisita,
    alVolver: () -> Unit
) {
    val visitas by viewModel.todasLasVisitas.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Visitas") },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (visitas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No hay registros de visitas guardados.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(visitas) { visita ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${visita.codigoActivo} - ${visita.nombreTienda}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Tipo: ${visita.tipoVisita}")
                            Text("Estado: ${visita.estadoEquipo}")
                            Text("Obs: ${visita.observaciones}")
                            Text("GPS: ${visita.latitud}, ${visita.longitud}")

                            if (visita.rutaFoto.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                AsyncImage(
                                    model = visita.rutaFoto,
                                    contentDescription = "Evidencia foto",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(140.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}