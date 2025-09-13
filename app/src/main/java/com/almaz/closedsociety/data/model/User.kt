package com.almaz.closedsociety.data.model

import java.util.Date

data class User(
    val uuid: String = "",
    val publicNickname: String = "",
    val contacts: List<String> = emptyList(),
    val fcmToken: String = "",
    val createdAt: Date = Date() // Используем обычный Date вместо Firebase Timestamp
)