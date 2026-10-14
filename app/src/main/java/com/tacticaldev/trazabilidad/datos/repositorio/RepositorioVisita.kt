package com.tacticaldev.trazabilidad.datos.repositorio

import com.tacticaldev.trazabilidad.datos.local.dao.DaoVisita
import com.tacticaldev.trazabilidad.datos.local.entidad.EntidadVisita
import kotlinx.coroutines.flow.Flow

class RepositorioVisita(private val daoVisita: DaoVisita) {
    val todasLasVisitas: Flow<List<EntidadVisita>> = daoVisita.obtenerTodasLasVisitas()
    val cantidadTotal: Flow<Int> = daoVisita.obtenerTotalVisitas()
    val cantidadFallas: Flow<Int> = daoVisita.obtenerTotalFallasCriticas()

    suspend fun guardarVisita(visita: EntidadVisita) {
        daoVisita.insertarVisita(visita)
    }
}