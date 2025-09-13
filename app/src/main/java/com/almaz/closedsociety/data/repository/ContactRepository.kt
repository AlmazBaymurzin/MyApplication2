package com.almaz.closedsociety.data.repository

import com.almaz.closedsociety.data.model.Contact

interface ContactRepository {
    suspend fun addContact(userUuid: String, contactUuid: String, publicNickname: String)
    suspend fun getContacts(userUuid: String): List<Contact>
    suspend fun verifyContact(userUuid: String, contactUuid: String)
    suspend fun deleteContact(userUuid: String, contactUuid: String)
}