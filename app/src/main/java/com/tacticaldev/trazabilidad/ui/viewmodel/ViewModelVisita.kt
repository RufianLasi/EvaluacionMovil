package com.tacticaldev.trazabilidad.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tacticaldev.trazabilidad.datos.local.BaseDatosTactical
import com.tacticaldev.trazabilidad.datos.local.entidad.EntidadVisita
import com.tacticaldev.trazabilidad.datos.repositorio.RepositorioVisita
import com.tacticaldev.trazabilidad.dominio.validacion.ValidadorFormularioVisita
import com.tacticaldev.trazabilidad.utilidades.AyudanteUbicacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelVisita(aplicacion: Application) : AndroidViewModel(aplicacion) {

    private val repositorio: RepositorioVisita
    private val ayudanteUbicacion: AyudanteUbicacion

    private val _estadoFormulario = MutableStateFlow(EstadoFormularioVisita())
    val estadoFormulario: StateFlow<EstadoFormularioVisita> = _estadoFormulario.asStateFlow()

    val todasLasVisitas = MutableStateFlow<List<EntidadVisita>>(emptyList())
    val totalVisitas = MutableStateFlow(0)
    val fallasCriticas = MutableStateFlow(0)

    // RUT del técnico en la sesión actual
    var rutOperarioActivo: String = ""
        private set

    fun establecerRutOperario(rut: String) {
        rutOperarioActivo = rut
    }

    fun cerrarSesion() {
        rutOperarioActivo = ""
        _estadoFormulario.value = EstadoFormularioVisita()
    }

    init {
        val dao = BaseDatosTactical.obtenerBaseDatos(aplicacion).daoVisita()
        repositorio = RepositorioVisita(dao)
        ayudanteUbicacion = AyudanteUbicacion(aplicacion)

        viewModelScope.launch {
            repositorio.todasLasVisitas.collect { lista -> todasLasVisitas.value = lista }
        }
        viewModelScope.launch {
            repositorio.cantidadTotal.collect { total -> totalVisitas.value = total }
        }
        viewModelScope.launch {
            repositorio.cantidadFallas.collect { fallas -> fallasCriticas.value = fallas }
        }
    }

    fun alCambiarCodigoActivo(codigo: String) {
        val resultado = ValidadorFormularioVisita.validarCodigoActivo(codigo)
        _estadoFormulario.update { it.copy(codigoActivo = codigo, errorCodigoActivo = resultado.mensajeError) }
    }

    fun alCambiarNombreTienda(nombre: String) {
        val resultado = ValidadorFormularioVisita.validarNombreTienda(nombre)
        _estadoFormulario.update { it.copy(nombreTienda = nombre, errorNombreTienda = resultado.mensajeError) }
    }

    fun alCambiarObservaciones(observacion: String) {
        val resultado = ValidadorFormularioVisita.validarObservaciones(observacion)
        _estadoFormulario.update { it.copy(observaciones = observacion, errorObservaciones = resultado.mensajeError) }
    }

    fun alSeleccionarTipoVisita(tipo: String) {
        _estadoFormulario.update { it.copy(tipoVisita = tipo) }
    }

    fun alSeleccionarEstadoEquipo(estado: String) {
        _estadoFormulario.update { it.copy(estadoEquipo = estado) }
    }

    fun alCapturarFoto(rutaUri: String) {
        val resultado = ValidadorFormularioVisita.validarFoto(rutaUri)
        _estadoFormulario.update { it.copy(uriFoto = rutaUri, errorFoto = resultado.mensajeError) }
    }

    fun capturarUbicacion() {
        _estadoFormulario.update { it.copy(cargandoUbicacion = true, errorUbicacion = null) }
        ayudanteUbicacion.obtenerUbicacionActual(
            alExito = { ubicacion ->
                _estadoFormulario.update {
                    it.copy(
                        latitud = ubicacion.latitude,
                        longitud = ubicacion.longitude,
                        cargandoUbicacion = false,
                        errorUbicacion = null
                    )
                }
            },
            alError = { error ->
                _estadoFormulario.update {
                    it.copy(
                        cargandoUbicacion = false,
                        errorUbicacion = error.message ?: "Error al obtener ubicación GPS"
                    )
                }
            }
        )
    }

    fun enviarFormulario(alExito: () -> Unit) {
        val estado = _estadoFormulario.value
        val resCodigo = ValidadorFormularioVisita.validarCodigoActivo(estado.codigoActivo)
        val resTienda = ValidadorFormularioVisita.validarNombreTienda(estado.nombreTienda)
        val resObs = ValidadorFormularioVisita.validarObservaciones(estado.observaciones)
        val resFoto = ValidadorFormularioVisita.validarFoto(estado.uriFoto)
        val resUbicacion = ValidadorFormularioVisita.validarUbicacion(estado.latitud, estado.longitud)

        val tieneError = listOf(resCodigo, resTienda, resObs, resFoto, resUbicacion).any { !it.esValido }

        if (tieneError) {
            _estadoFormulario.update {
                it.copy(
                    errorCodigoActivo = resCodigo.mensajeError,
                    errorNombreTienda = resTienda.mensajeError,
                    errorObservaciones = resObs.mensajeError,
                    errorFoto = resFoto.mensajeError,
                    errorUbicacion = resUbicacion.mensajeError
                )
            }
            return
        }

        viewModelScope.launch {
            val nuevaVisita = EntidadVisita(
                rutOperario = rutOperarioActivo,
                codigoActivo = estado.codigoActivo,
                nombreTienda = estado.nombreTienda,
                tipoVisita = estado.tipoVisita,
                estadoEquipo = estado.estadoEquipo,
                observaciones = estado.observaciones,
                rutaFoto = estado.uriFoto!!,
                latitud = estado.latitud!!,
                longitud = estado.longitud!!
            )
            repositorio.guardarVisita(nuevaVisita)
            _estadoFormulario.update { EstadoFormularioVisita(esGuardadoExitoso = true) }
            alExito()
        }
    }
}