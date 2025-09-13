package com.almaz.closedsociety.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    onNavigateToContacts: () -> Unit,
    onNavigateToQrScanner: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Главный экран")

        Button(
            onClick = onNavigateToContacts,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("👥 Мои контакты")
        }

        Button(
            onClick = onNavigateToQrScanner,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("📷 Сканировать QR-код")
        }
    }
}