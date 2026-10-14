package com.tacticaldev.trazabilidad.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    var rutInput by mutableStateOf("")
        private set

    var contrasenaInput by mutableStateOf("")
        private set

    var rutError by mutableStateOf<String?>(null)
        private set

    var contrasenaError by mutableStateOf<String?>(null)
        private set

    fun onRutChanged(nuevoValor: String) {
        rutInput = nuevoValor
        rutError = if (nuevoValor.isBlank()) "El RUT es obligatorio" else null
    }

    fun onContrasenaChanged(nuevoValor: String) {
        contrasenaInput = nuevoValor
        contrasenaError = if (nuevoValor.isBlank()) "La contraseña es obligatoria" else null
    }

    fun validarFormulario(): Boolean {
        var esValido = true

        val rutLimpio = rutInput.trim().uppercase()
        // Valida formato RUT con guión (ej: 12345678-9 o 12.345.678-9)
        val regexRut = Regex("^[0-9]{1,2}\\.?[0-9]{3}\\.?[0-9]{3}-[0-9Kk]{1}$")

        if (rutLimpio.isBlank()) {
            rutError = "Ingresa tu RUT"
            esValido = false
        } else if (!rutLimpio.matches(regexRut)) {
            rutError = "Formato inválido. Ej: 12345678-9"
            esValido = false
        } else {
            rutError = null
        }

        if (contrasenaInput.isBlank()) {
            contrasenaError = "Ingresa tu contraseña"
            esValido = false
        } else if (contrasenaInput.length < 4) {
            contrasenaError = "Mínimo 4 caracteres"
            esValido = false
        } else {
            contrasenaError = null
        }

        return esValido
    }
}