package com.tacticaldev.trazabilidad.datos.local.entidad

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visitas")
data class EntidadVisita(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rutOperario: String = "",   // Asignar "" por defecto
    val codigoActivo: String,
    val nombreTienda: String,
    val tipoVisita: String,
    val estadoEquipo: String,
    val observaciones: String,
    val rutaFoto: String,
    val latitud: Double,
    val longitud: Double,
    val marcaTiempo: Long = System.currentTimeMillis(),
    val nombreOperario: String = "Operario Técnico"
)