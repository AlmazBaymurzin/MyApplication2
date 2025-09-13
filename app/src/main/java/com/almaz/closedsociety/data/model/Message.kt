package com.almaz.closedsociety.data.model

import java.util.Date

data class Message(
    val id: String = "",
    val senderUuid: String = "",
    val recipientUuid: String = "",
    val text: String = "",
    val timestamp: Date = Date(), // Используем обычный Date вместо Firebase Timestamp
    val type: String = "text",
    val status: String = "sent"
)