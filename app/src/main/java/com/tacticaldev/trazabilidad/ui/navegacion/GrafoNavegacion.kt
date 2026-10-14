package com.tacticaldev.trazabilidad.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tacticaldev.trazabilidad.ui.pantallas.LoginScreen
import com.tacticaldev.trazabilidad.ui.pantallas.PantallaFormularioVisita
import com.tacticaldev.trazabilidad.ui.pantallas.PantallaHistorialVisitas
import com.tacticaldev.trazabilidad.ui.pantallas.PantallaInicio
import com.tacticaldev.trazabilidad.ui.viewmodel.ViewModelVisita

object Destinos {
    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val FORMULARIO = "formulario"
    const val HISTORIAL = "historial"
}

@Composable
fun GrafoNavegacion(viewModel: ViewModelVisita = viewModel()) {
    val controladorNavegacion = rememberNavController()

    NavHost(
        navController = controladorNavegacion,
        startDestination = Destinos.LOGIN
    ) {
        // 1. Pantalla Inicial: Login
        composable(Destinos.LOGIN) {
            LoginScreen(
                onLoginExitoso = { rutAutenticado ->
                    viewModel.establecerRutOperario(rutAutenticado)

                    controladorNavegacion.navigate(Destinos.INICIO) {
                        popUpTo(Destinos.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // 2. Pantalla Principal / Menú
        composable(Destinos.INICIO) {
            PantallaInicio(
                viewModel = viewModel,
                alNavegarAFormulario = { controladorNavegacion.navigate(Destinos.FORMULARIO) },
                alNavegarAHistorial = { controladorNavegacion.navigate(Destinos.HISTORIAL) },
                alCerrarSesion = {
                    viewModel.cerrarSesion()
                    controladorNavegacion.navigate(Destinos.LOGIN) {
                        popUpTo(0) { inclusive = true } // Elimina todo el historial para evitar volver al presionar 'Atrás'
                    }
                }
            )
        }

        // 3. Pantalla Formulario de Registro
        composable(Destinos.FORMULARIO) {
            PantallaFormularioVisita(
                viewModel = viewModel,
                alVolver = { controladorNavegacion.popBackStack() }
            )
        }

        // 4. Pantalla Historial de Visitas
        composable(Destinos.HISTORIAL) {
            PantallaHistorialVisitas(
                viewModel = viewModel,
                alVolver = { controladorNavegacion.popBackStack() }
            )
        }
    }
}