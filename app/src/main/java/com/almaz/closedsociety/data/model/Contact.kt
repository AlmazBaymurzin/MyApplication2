package com.almaz.closedsociety.data.model

import java.util.Date

data class Contact(
    val uuid: String = "",
    val publicNickname: String = "",
    var localName: String = "", // Локальное имя (только на этом устройстве)
    val addedAt: Date = Date(),
    val isVerified: Boolean = false // Подтвержден ли контакт
)