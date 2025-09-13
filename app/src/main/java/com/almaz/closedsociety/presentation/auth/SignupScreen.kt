package com.almaz.closedsociety.presentation.auth

import androidx.compose.foundation.border // Добавьте этот импорт
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.almaz.closedsociety.presentation.components.SignatureCanvas

@Composable
fun SignupScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit,
    onSignupComplete: () -> Unit
) {
    var nickname by remember { mutableStateOf("") }
    var signatureBitmap by remember { mutableStateOf<ByteArray?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text("Публичный никнейм") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Используем улучшенный компонент подписи
        SignatureCanvas(
            onSignatureDrawn = { signatureData ->
                signatureBitmap = signatureData
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (nickname.isNotEmpty() && signatureBitmap != null && signatureBitmap!!.isNotEmpty()) {
                    viewModel.onSignupCompleted(nickname, signatureBitmap!!)
                    onSignupComplete()
                }
            },
            enabled = nickname.isNotEmpty() && signatureBitmap != null && signatureBitmap!!.isNotEmpty(),
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Завершить регистрацию")
        }

        Button(
            onClick = onNavigateBack,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Назад")
        }
    }
}