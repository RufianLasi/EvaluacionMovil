package com.tacticaldev.trazabilidad.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tacticaldev.trazabilidad.ui.viewmodel.ViewModelVisita

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(
    viewModel: ViewModelVisita,
    alNavegarAFormulario: () -> Unit,
    alNavegarAHistorial: () -> Unit,
    alCerrarSesion: () -> Unit
) {
    val totalVisitas by viewModel.totalVisitas.collectAsState()
    val fallasCriticas by viewModel.fallasCriticas.collectAsState()
    val rutOperario = viewModel.rutOperarioActivo

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TacticalDEV - Control Vitrinas") },
                actions = {
                    IconButton(onClick = alCerrarSesion) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar Sesión"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Panel de Resumen", style = MaterialTheme.typography.titleMedium)
                    if (rutOperario.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Operario: $rutOperario",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Total Visitas Registradas: $totalVisitas", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "Alertas Críticas: $fallasCriticas",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = alNavegarAFormulario,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nueva Visita Técnica")
            }

            OutlinedButton(
                onClick = alNavegarAHistorial,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(Icons.Default.History, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver Historial de Visitas")
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}