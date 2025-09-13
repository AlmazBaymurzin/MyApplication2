package com.almaz.closedsociety.data.security

import android.content.Context
import android.content.SharedPreferences
import com.almaz.closedsociety.data.model.Contact
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ContactManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("contacts_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Сохранение локального имени контакта
    fun saveLocalContactName(contactUuid: String, localName: String) {
        prefs.edit().putString("contact_name_$contactUuid", localName).apply()
    }

    // Получение локального имени контакта
    fun getLocalContactName(contactUuid: String): String? {
        return prefs.getString("contact_name_$contactUuid", null)
    }

    // Сохранение списка контактов
    fun saveContacts(contacts: List<Contact>) {
        val json = gson.toJson(contacts)
        prefs.edit().putString("user_contacts", json).apply()
    }

    // Загрузка списка контактов
    fun loadContacts(): List<Contact> {
        val json = prefs.getString("user_contacts", null)
        return if (json != null) {
            val type = object : TypeToken<List<Contact>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}