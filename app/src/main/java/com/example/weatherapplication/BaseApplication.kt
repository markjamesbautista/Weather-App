package com.example.weatherapplication

import android.app.Application
import com.example.weatherapplication.modules.repositoryModule
import com.example.weatherapplication.modules.serviceModule
import com.example.weatherapplication.modules.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BaseApplication : Application() {
    private var koinApp: KoinApplication? = null
    override fun onCreate() {
        super.onCreate()
        koinApp ?: let {
            koinApp = startKoin {
                // TODO: upgrade koin to latest version and remove Level.NONE
                androidLogger(Level.NONE)
                androidContext(this@BaseApplication)
                modules(modules)
            }
        }
    }

    private val modules = listOf(viewModelModule, serviceModule, repositoryModule)

}