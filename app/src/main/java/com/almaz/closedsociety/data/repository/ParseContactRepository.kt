package com.almaz.closedsociety.data.repository

import com.parse.ParseObject
import com.parse.ParseQuery
import com.almaz.closedsociety.data.model.Contact
import java.util.Date
import javax.inject.Inject

class ParseContactRepository @Inject constructor() : ContactRepository {

    override suspend fun addContact(userUuid: String, contactUuid: String, publicNickname: String) {
        try {
            val contact = ParseObject("Contact")
            contact.put("userUuid", userUuid)
            contact.put("contactUuid", contactUuid)
            contact.put("publicNickname", publicNickname)
            contact.put("addedAt", Date())
            contact.put("isVerified", false)

            contact.saveInBackground().await() // Исправлено
            println("✅ Контакт добавлен в Back4App: $userUuid -> $contactUuid")

        } catch (e: Exception) {
            println("❌ Ошибка добавления контакта: ${e.message}")
            throw Exception("Ошибка добавления контакта: ${e.message}")
        }
    }

    override suspend fun getContacts(userUuid: String): List<Contact> {
        return try {
            val query = ParseQuery<ParseObject>("Contact")
            query.whereEqualTo("userUuid", userUuid)
            val results = query.findInBackground().await() // Исправлено

            results.map { obj ->
                Contact(
                    uuid = obj.getString("contactUuid") ?: "",
                    publicNickname = obj.getString("publicNickname") ?: "",
                    addedAt = obj.getDate("addedAt") ?: Date(),
                    isVerified = obj.getBoolean("isVerified") ?: false
                )
            }
        } catch (e: Exception) {
            println("⚠️ Ошибка получения контактов: ${e.message}")
            emptyList()
        }
    }

    override suspend fun verifyContact(userUuid: String, contactUuid: String) {
        try {
            val query = ParseQuery<ParseObject>("Contact")
            query.whereEqualTo("userUuid", userUuid)
            query.whereEqualTo("contactUuid", contactUuid)
            val contact = query.firstInBackground().await() // Исправлено

            contact.put("isVerified", true)
            contact.saveInBackground().await() // Исправлено
            println("✅ Контакт подтвержден: $userUuid -> $contactUuid")

        } catch (e: Exception) {
            println("❌ Ошибка подтверждения контакта: ${e.message}")
            throw Exception("Ошибка подтверждения контакта: ${e.message}")
        }
    }

    override suspend fun deleteContact(userUuid: String, contactUuid: String) {
        try {
            val query = ParseQuery<ParseObject>("Contact")
            query.whereEqualTo("userUuid", userUuid)
            query.whereEqualTo("contactUuid", contactUuid)
            val contact = query.firstInBackground().await() // Исправлено

            contact.deleteInBackground().await() // Исправлено
            println("✅ Контакт удален: $userUuid -> $contactUuid")

        } catch (e: Exception) {
            println("❌ Ошибка удаления контакта: ${e.message}")
            throw Exception("Ошибка удаления контакта: ${e.message}")
        }
    }
}