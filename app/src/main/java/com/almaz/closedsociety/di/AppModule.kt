package com.almaz.closedsociety.di

import com.almaz.closedsociety.data.repository.ContactRepository
import com.almaz.closedsociety.data.repository.ParseContactRepository
import com.almaz.closedsociety.data.repository.ParseUserRepository
import com.almaz.closedsociety.data.repository.UserRepository
import com.almaz.closedsociety.data.security.ContactManager
import com.almaz.closedsociety.data.security.SignatureManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    // Менеджер подписей
    single { SignatureManager(androidContext()) }

    // Репозиторий пользователей (Back4App)
    single<UserRepository> { ParseUserRepository() }

    // Репозиторий контактов (Back4App)
    single<ContactRepository> { ParseContactRepository() }

    // Менеджер контактов
    single { ContactManager(androidContext()) }
}