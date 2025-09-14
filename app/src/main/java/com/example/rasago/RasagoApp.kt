package com.example.rasago

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The main Application class for the app.
 * The @HiltAndroidApp annotation enables dependency injection.
 */
@HiltAndroidApp
class RasagoApp : Application()

