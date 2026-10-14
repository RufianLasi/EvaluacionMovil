package com.tacticaldev.trazabilidad.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tacticaldev.trazabilidad.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginExitoso: (rutAutenticado: String) -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TacticalDEV",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Trazabilidad y Verificación de Visitas",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo RUT Operario
        OutlinedTextField(
            value = viewModel.rutInput,
            onValueChange = { viewModel.onRutChanged(it) },
            label = { Text("RUT del Técnico (ej: 12345678-9)") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            isError = viewModel.rutError != null,
            supportingText = {
                viewModel.rutError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Campo Contraseña
        OutlinedTextField(
            value = viewModel.contrasenaInput,
            onValueChange = { viewModel.onContrasenaChanged(it) },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = viewModel.contrasenaError != null,
            supportingText = {
                viewModel.contrasenaError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    onLoginExitoso(viewModel.rutInput.trim().uppercase())
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Iniciar Sesión")
        }
    }
}