package com.almaz.closedsociety.data.repository

import com.parse.ParseObject
import com.parse.ParseQuery
import com.almaz.closedsociety.data.model.User
import java.util.Date
import javax.inject.Inject

class ParseUserRepository @Inject constructor() : UserRepository {

    suspend fun testConnection(): Boolean {
        return try {
            val testObject = ParseObject("TestConnection")
            testObject.put("testField", "testValue_${System.currentTimeMillis()}")
            testObject.put("timestamp", Date())
            testObject.saveInBackground().await() // Исправлено
            println("✅ Подключение к Back4App успешно!")
            true
        } catch (e: Exception) {
            println("❌ Ошибка подключения: ${e.message}")
            false
        }
    }

    override suspend fun createUser(user: User) {
        try {
            val parseUser = ParseObject("AppUser")
            parseUser.put("uuid", user.uuid)
            parseUser.put("publicNickname", user.publicNickname)
            parseUser.put("fcmToken", user.fcmToken)
            parseUser.put("createdAt", user.createdAt)
            parseUser.put("contacts", user.contacts)

            parseUser.saveInBackground().await() // Исправлено
            println("✅ Пользователь создан в Back4App: ${user.uuid}")

        } catch (e: Exception) {
            println("❌ Ошибка Back4App: ${e.message}")
            throw Exception("Ошибка создания пользователя: ${e.message}")
        }
    }

    override suspend fun getUser(uuid: String): User? {
        return try {
            val query = ParseQuery<ParseObject>("AppUser")
            query.whereEqualTo("uuid", uuid)
            val result = query.firstInBackground().await() // Исправлено

            User(
                uuid = result.getString("uuid") ?: "",
                publicNickname = result.getString("publicNickname") ?: "",
                contacts = result.getList<String>("contacts") ?: emptyList(),
                fcmToken = result.getString("fcmToken") ?: "",
                createdAt = result.getDate("createdAt") ?: Date()
            )
        } catch (e: Exception) {
            println("⚠️ Пользователь не найден в Back4App: ${e.message}")
            null
        }
    }

    override suspend fun updateUserContacts(uuid: String, contacts: List<String>) {
        try {
            val query = ParseQuery<ParseObject>("AppUser")
            query.whereEqualTo("uuid", uuid)
            val user = query.firstInBackground().await() // Исправлено

            user.put("contacts", contacts)
            user.saveInBackground().await() // Исправлено
            println("✅ Контакты обновлены в Back4App")

        } catch (e: Exception) {
            println("❌ Ошибка обновления контактов: ${e.message}")
            throw Exception("Ошибка обновления контактов: ${e.message}")
        }
    }
}