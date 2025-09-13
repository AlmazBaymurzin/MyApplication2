package com.almaz.closedsociety.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.almaz.closedsociety.data.security.ContactManager
import com.almaz.closedsociety.presentation.auth.AuthScreen
import com.almaz.closedsociety.presentation.auth.AuthViewModel
import com.almaz.closedsociety.presentation.auth.SignupScreen
import com.almaz.closedsociety.presentation.contacts.ContactsScreen
import com.almaz.closedsociety.presentation.qr.QRScannerScreen
import com.almaz.closedsociety.presentation.screens.MainScreen

@Composable
fun AppNavGraph(
    authViewModel: AuthViewModel,
    contactManager: ContactManager,
    startDestination: String = "auth"
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Экран аутентификации
        composable("auth") {
            AuthScreen(
                viewModel = authViewModel,
                onNavigateToSignup = {
                    navController.navigate("signup")
                },
                onNavigateToQrScanner = {
                    navController.navigate("qr-scanner")
                },
                onAuthSuccess = {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        // Экран регистрации
        composable("signup") {
            SignupScreen(
                viewModel = authViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSignupComplete = {
                    navController.navigate("auth") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }

        // Главный экран
        composable("main") {
            MainScreen(
                onNavigateToContacts = {
                    navController.navigate("contacts")
                },
                onNavigateToQrScanner = {
                    navController.navigate("qr-scanner")
                }
            )
        }

        // Экран контактов
        composable("contacts") {
            ContactsScreen(
                navController = navController,
                contactManager = contactManager,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // Сканер QR-кодов
        composable("qr-scanner") {
            QRScannerScreen(
                onQrCodeScanned = { qrData ->
                    // Обработка отсканированного QR-кода
                    handleScannedContact(qrData, contactManager)
                    navController.popBackStack()
                    // Можно перейти на экран подтверждения
                    // navController.navigate("confirm-contact/$qrData")
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // TODO: Добавить другие экраны:
        // composable("chat/{contactUuid}") { ... }
        // composable("settings") { ... }
        // composable("confirm-contact/{uuid}") { ... }
    }
}

// Функция обработки отсканированного контакта
private fun handleScannedContact(qrData: String, contactManager: ContactManager) {
    // Временная реализация - просто сохраняем в локальное хранилище
    try {
        // Предполагаем, что QR-код содержит UUID в формате: "uuid:1234-5678-90ab-cdef"
        val uuid = if (qrData.startsWith("uuid:")) {
            qrData.substringAfter("uuid:")
        } else {
            qrData // Если нет префикса, используем как есть
        }

        // Проверяем валидность UUID
        if (isValidUuid(uuid)) {
            // Сохраняем во временное хранилище
            contactManager.saveLocalContactName(uuid, "Новый контакт")
            println("Контакт добавлен: $uuid")
        } else {
            println("Неверный формат UUID: $qrData")
        }
    } catch (e: Exception) {
        println("Ошибка обработки QR-кода: ${e.message}")
    }
}

// Проверка валидности UUID
private fun isValidUuid(uuid: String): Boolean {
    val uuidRegex = Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    return uuidRegex.matches(uuid)
}