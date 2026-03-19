package com.phantomclone

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main Application class for PhantomClone.
 *
 * Annotated with [HiltAndroidApp] to enable Hilt dependency injection
 * across the entire application.
 */
@HiltAndroidApp
class PhantomCloneApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Application initialization happens here.
        // Hilt handles all DI setup automatically via @HiltAndroidApp.
    }
}
