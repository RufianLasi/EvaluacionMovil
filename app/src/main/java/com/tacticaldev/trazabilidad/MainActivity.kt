package com.tacticaldev.trazabilidad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.tacticaldev.trazabilidad.ui.navegacion.GrafoNavegacion
import com.tacticaldev.trazabilidad.ui.theme.TacticalDEVTrazabilidadTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TacticalDEVTrazabilidadTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GrafoNavegacion()
                }
            }
        }
    }
}