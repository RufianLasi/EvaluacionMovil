package com.tacticaldev.trazabilidad.dominio.validacion

data class ResultadoValidacion(
    val esValido: Boolean,
    val mensajeError: String? = null
)

object ValidadorFormularioVisita {
    fun validarCodigoActivo(codigo: String): ResultadoValidacion {
        if (codigo.isBlank()) {
            return ResultadoValidacion(false, "El código del activo es obligatorio")
        }
        if (!codigo.matches(Regex("^[A-Z]{3}-\\d{3,6}$"))) {
            return ResultadoValidacion(false, "Formato inválido. Ejemplo esperado: VTR-1020")
        }
        return ResultadoValidacion(true)
    }

    fun validarNombreTienda(tienda: String): ResultadoValidacion {
        if (tienda.isBlank()) {
            return ResultadoValidacion(false, "El nombre del punto de venta es obligatorio")
        }
        if (tienda.length < 3) {
            return ResultadoValidacion(false, "Ingrese al menos 3 caracteres")
        }
        return ResultadoValidacion(true)
    }

    fun validarObservaciones(observacion: String): ResultadoValidacion {
        if (observacion.isBlank()) {
            return ResultadoValidacion(false, "Debe agregar una observación técnica")
        }
        return ResultadoValidacion(true)
    }

    fun validarFoto(uriFoto: String?): ResultadoValidacion {
        if (uriFoto.isNullOrEmpty()) {
            return ResultadoValidacion(false, "Es obligatoria la evidencia fotográfica del activo")
        }
        return ResultadoValidacion(true)
    }

    fun validarUbicacion(latitud: Double?, longitud: Double?): ResultadoValidacion {
        if (latitud == null || longitud == null || (latitud == 0.0 && longitud == 0.0)) {
            return ResultadoValidacion(false, "Se requiere verificar la ubicación GPS en terreno")
        }
        return ResultadoValidacion(true)
    }
}