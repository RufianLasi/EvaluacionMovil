package com.tacticaldev.trazabilidad.utilidades

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class AyudanteUbicacion(private val contexto: Context) {
    private val clienteUbicacion = LocationServices.getFusedLocationProviderClient(contexto)

    @SuppressLint("MissingPermission")
    fun obtenerUbicacionActual(alExito: (Location) -> Unit, alError: (Exception) -> Unit) {
        val fuenteCancelacion = CancellationTokenSource()
        clienteUbicacion.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            fuenteCancelacion.token
        ).addOnSuccessListener { ubicacion ->
            if (ubicacion != null) {
                alExito(ubicacion)
            } else {
                alError(Exception("No se pudo obtener la ubicación GPS actual"))
            }
        }.addOnFailureListener { excepcion ->
            alError(excepcion)
        }
    }
}