package com.tacticaldev.trazabilidad.datos.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tacticaldev.trazabilidad.datos.local.entidad.EntidadVisita
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoVisita {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarVisita(visita: EntidadVisita)

    @Query("SELECT * FROM visitas ORDER BY marcaTiempo DESC")
    fun obtenerTodasLasVisitas(): Flow<List<EntidadVisita>>

    @Query("SELECT COUNT(*) FROM visitas")
    fun obtenerTotalVisitas(): Flow<Int>

    @Query("SELECT COUNT(*) FROM visitas WHERE estadoEquipo = 'Falla Crítica'")
    fun obtenerTotalFallasCriticas(): Flow<Int>
}