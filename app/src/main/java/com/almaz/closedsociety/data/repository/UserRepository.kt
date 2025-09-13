package com.almaz.closedsociety.data.repository

import com.almaz.closedsociety.data.model.User

interface UserRepository {
    suspend fun createUser(user: User)
    suspend fun getUser(uuid: String): User?
    suspend fun updateUserContacts(uuid: String, contacts: List<String>)
}