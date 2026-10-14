package com.tacticaldev.trazabilidad.datos.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tacticaldev.trazabilidad.datos.local.dao.DaoVisita
import com.tacticaldev.trazabilidad.datos.local.entidad.EntidadVisita

@Database(entities = [EntidadVisita::class], version = 1, exportSchema = false)
abstract class BaseDatosTactical : RoomDatabase() {
    abstract fun daoVisita(): DaoVisita

    companion object {
        @Volatile
        private var INSTANCIA: BaseDatosTactical? = null

        fun obtenerBaseDatos(contexto: Context): BaseDatosTactical {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    contexto.applicationContext,
                    BaseDatosTactical::class.java,
                    "base_datos_tacticaldev"
                ).build()
                INSTANCIA = instancia
                instancia
            }
        }
    }
}