package com.almaz.closedsociety

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.almaz.closedsociety.data.repository.ParseUserRepository
import com.almaz.closedsociety.data.security.SignatureManager
import com.almaz.closedsociety.presentation.theme.ClosedSocietyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ClosedSocietyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent()
                }
            }
        }
    }
}

@Composable
fun AppContent() {
    // Временное решение без Koin
    val context = LocalContext.current
    val signatureManager = remember { SignatureManager(context) }
    val userRepository = remember { ParseUserRepository() }

    var isTestingConnection by remember { mutableStateOf(false) }
    var connectionResult by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isTestingConnection) {
            CircularProgressIndicator()
            Text("Тестируем подключение к Back4App...", modifier = Modifier.padding(8.dp))
        } else {
            connectionResult?.let { result ->
                Text(
                    text = result,
                    color = if (result.startsWith("✅")) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Button(
                onClick = {
                    isTestingConnection = true
                    connectionResult = null
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Тест подключения к Back4App")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Начать работу с приложением")
            }
        }
    }

    // Запуск теста подключения
    LaunchedEffect(isTestingConnection) {
        if (isTestingConnection) {
            try {
                val success = userRepository.testConnection()
                connectionResult = if (success) {
                    "✅ Подключение к Back4App успешно!"
                } else {
                    "❌ Ошибка подключения"
                }
            } catch (e: Exception) {
                connectionResult = "❌ Ошибка: ${e.message}"
            } finally {
                isTestingConnection = false
            }
        }
    }
}