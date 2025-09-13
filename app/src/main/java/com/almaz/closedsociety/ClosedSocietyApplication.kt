package com.almaz.closedsociety

import android.app.Application
import com.parse.Parse
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.almaz.closedsociety.di.appModule

class ClosedSocietyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Инициализация Back4App с ВАШИМИ ключами
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("sb7KJjwDCEGuPnVeWTb0xwsQv5ypMOhsdPlPsrBk")
                .clientKey("q5RUq3TJRaCkX2e7L79vnWIbqi149mv6BKvooSwo")
                .server("https://parseapi.back4app.com/")
                .build()
        )

        // Включите логирование для отладки
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG)

        // Инициализация Koin
        startKoin {
            androidContext(this@ClosedSocietyApplication)
            modules(appModule)
        }
    }
}