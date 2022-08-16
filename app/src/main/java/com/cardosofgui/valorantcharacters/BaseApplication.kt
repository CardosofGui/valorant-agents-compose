package com.cardosofgui.valorantcharacters

import android.app.Application
import com.cardosofgui.valorantcharacters.framework.modules.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        baseContext.cacheDir.deleteRecursively()

        startKoin {
            androidContext(this@BaseApplication)
        }

        AppModule.load()
    }
}