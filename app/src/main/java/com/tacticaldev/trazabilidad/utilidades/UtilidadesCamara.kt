package com.tacticaldev.trazabilidad.utilidades

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object UtilidadesCamara {
    fun crearArchivoImagen(contexto: Context): Uri {
        val marcaFormateada = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val nombreArchivo = "FOTO_" + marcaFormateada + "_"
        val directorioAlmacenamiento = contexto.getExternalFilesDir("Pictures")
        val archivoImagen = File.createTempFile(nombreArchivo, ".jpg", directorioAlmacenamiento)

        return FileProvider.getUriForFile(
            contexto,
            "${contexto.packageName}.fileprovider",
            archivoImagen
        )
    }
}