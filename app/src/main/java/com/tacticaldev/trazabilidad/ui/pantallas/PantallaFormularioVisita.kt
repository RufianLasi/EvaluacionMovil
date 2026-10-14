package com.tacticaldev.trazabilidad.ui.pantallas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tacticaldev.trazabilidad.ui.viewmodel.ViewModelVisita
import com.tacticaldev.trazabilidad.utilidades.UtilidadesCamara

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormularioVisita(
    viewModel: ViewModelVisita,
    alVolver: () -> Unit
) {
    val estado by viewModel.estadoFormulario.collectAsState()
    val contexto = LocalContext.current
    var uriFotoTemporal by remember { mutableStateOf<Uri?>(null) }

    // Lista prediseñada de tiendas / locales
    val listaTiendas = listOf(
        "Tienda Central",
        "Tienda Mall Plaza Norte",
        "Tienda Costanera Center",
        "Tienda Marina Arauco",
        "Tienda Concepción Centro"
    )
    var menuTiendasDesplegado by remember { mutableStateOf(false) }

    val lanzadorCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito && uriFotoTemporal != null) {
            viewModel.alCapturarFoto(uriFotoTemporal.toString())
        }
    }

    val lanzadorPermisosGeo = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        if (permisos.values.any { it }) {
            viewModel.capturarUbicacion()
        }
    }

    val lanzadorPermisosCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            val uri = UtilidadesCamara.crearArchivoImagen(contexto)
            uriFotoTemporal = uri
            lanzadorCamara.launch(uri)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Visita") },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Animación: Banner de éxito si se guardó la visita
            AnimatedVisibility(
                visible = estado.esGuardadoExitoso,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("¡Visita registrada exitosamente!")
                    }
                }
            }

            // Código de Activo con error animado
            OutlinedTextField(
                value = estado.codigoActivo,
                onValueChange = { viewModel.alCambiarCodigoActivo(it) },
                label = { Text("Código de Activo (ej: VTR-884)") },
                isError = estado.errorCodigoActivo != null,
                supportingText = {
                    AnimatedVisibility(
                        visible = estado.errorCodigoActivo != null,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        estado.errorCodigoActivo?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Menú desplegable para Nombre de Tienda con error animado
            ExposedDropdownMenuBox(
                expanded = menuTiendasDesplegado,
                onExpandedChange = { menuTiendasDesplegado = !menuTiendasDesplegado },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = estado.nombreTienda,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Nombre Punto de Venta / Tienda") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuTiendasDesplegado)
                    },
                    isError = estado.errorNombreTienda != null,
                    supportingText = {
                        AnimatedVisibility(
                            visible = estado.errorNombreTienda != null,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            estado.errorNombreTienda?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = menuTiendasDesplegado,
                    onDismissRequest = { menuTiendasDesplegado = false }
                ) {
                    listaTiendas.forEach { tienda ->
                        DropdownMenuItem(
                            text = { Text(tienda) },
                            onClick = {
                                viewModel.alCambiarNombreTienda(tienda)
                                menuTiendasDesplegado = false
                            }
                        )
                    }
                }
            }

            Text("Tipo de Visita", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Mantenimiento Preventivo", "Inspección").forEach { tipo ->
                    FilterChip(
                        selected = estado.tipoVisita == tipo,
                        onClick = { viewModel.alSeleccionarTipoVisita(tipo) },
                        label = { Text(tipo) }
                    )
                }
            }

            Text("Estado del Equipo", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Bueno", "Requiere Mantención", "Falla Crítica").forEach { est ->
                    FilterChip(
                        selected = estado.estadoEquipo == est,
                        onClick = { viewModel.alSeleccionarEstadoEquipo(est) },
                        label = { Text(est) }
                    )
                }
            }

            // Observaciones con error animado
            OutlinedTextField(
                value = estado.observaciones,
                onValueChange = { viewModel.alCambiarObservaciones(it) },
                label = { Text("Observaciones Técnicas") },
                isError = estado.errorObservaciones != null,
                supportingText = {
                    AnimatedVisibility(
                        visible = estado.errorObservaciones != null,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        estado.errorObservaciones?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Recurso Nativo 1: Cámara con animación
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        lanzadorPermisosCamara.launch(android.Manifest.permission.CAMERA)
                    }) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Capturar Foto Evidencia")
                    }

                    // Despliegue animado de la foto capturada
                    AnimatedVisibility(
                        visible = estado.uriFoto != null,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        estado.uriFoto?.let { uriStr ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Spacer(modifier = Modifier.height(8.dp))
                                AsyncImage(
                                    model = uriStr,
                                    contentDescription = "Foto capturada",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                )
                            }
                        }
                    }

                    // Mensaje de error animado en foto
                    AnimatedVisibility(
                        visible = estado.errorFoto != null,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        estado.errorFoto?.let { err ->
                            Text(
                                err,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            // Recurso Nativo 2: GPS con animación
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            lanzadorPermisosGeo.launch(
                                arrayOf(
                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        },
                        enabled = !estado.cargandoUbicacion
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (estado.cargandoUbicacion) "Obteniendo GPS..." else "Verificar Ubicación GPS")
                    }

                    // Despliegue animado de coordenadas GPS
                    AnimatedVisibility(
                        visible = estado.latitud != null && estado.longitud != null,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        if (estado.latitud != null && estado.longitud != null) {
                            Text(
                                "Lat: ${estado.latitud}, Long: ${estado.longitud}",
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }

                    // Mensaje de error animado en GPS
                    AnimatedVisibility(
                        visible = estado.errorUbicacion != null,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        estado.errorUbicacion?.let { err ->
                            Text(
                                err,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.enviarFormulario(alExito = alVolver)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Guardar Registro")
            }
        }
    }
}