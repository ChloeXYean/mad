package com.example.rasago.theme.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.rasago.ui.theme.RasagoApp
import dagger.hilt.android.AndroidEntryPoint
import com.example.rasago.theme.navigation.AppNavigation

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RasagoApp {
                AppNavigation()
            }
        }
    }
}
