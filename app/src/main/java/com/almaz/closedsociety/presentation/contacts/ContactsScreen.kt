package com.almaz.closedsociety.presentation.contacts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.almaz.closedsociety.data.model.Contact
import androidx.compose.material.icons.filled.Message
import com.almaz.closedsociety.data.security.ContactManager // Добавить этот импорт

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    navController: NavController,
    contactManager: ContactManager,
    onBack: () -> Unit // Добавить параметр для навигации назад
) {
    var contacts by remember { mutableStateOf(contactManager.loadContacts()) }
    var selectedContact by remember { mutableStateOf<Contact?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои контакты") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("qr-scanner") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить контакт")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (contacts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Контактов пока нет\nНажмите + чтобы добавить",
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn {
                    items(contacts) { contact ->
                        ContactItem(
                            contact = contact,
                            onEdit = { selectedContact = contact },
                            onMessage = {
                                navController.navigate("chat/${contact.uuid}")
                            }
                        )
                    }
                }
            }
        }
    }

    // Диалог редактирования имени
    selectedContact?.let { contact ->
        EditContactDialog(
            contact = contact,
            onDismiss = { selectedContact = null },
            onSave = { newName ->
                contactManager.saveLocalContactName(contact.uuid, newName)
                contacts = contactManager.loadContacts()
                selectedContact = null
            }
        )
    }
}

@Composable
fun EditContactDialog(
    contact: Contact,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var localName by remember { mutableStateOf(contact.localName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать имя") },
        text = {
            TextField(
                value = localName,
                onValueChange = { localName = it },
                label = { Text("Локальное имя") }
            )
        },
        confirmButton = {
            Button(
                onClick = { onSave(localName) }
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Отмена")
            }
        }
    )
}

@Composable
fun ContactItem(
    contact: Contact,
    onEdit: () -> Unit,
    onMessage: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Контакт",
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contact.localName.ifEmpty { contact.publicNickname },
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = if (contact.isVerified) "✓ Подтвержден" else "Ожидает подтверждения",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (contact.isVerified) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Редактировать")
            }

            IconButton(onClick = onMessage) {
                Icon(Icons.Default.Message, contentDescription = "Написать")
            }
        }
    }
}