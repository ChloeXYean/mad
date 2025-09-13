package com.example.rasago.theme.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.rasago.ui.theme.RasagoApp
import com.example.rasago.ui.theme.menu.MenuViewModel
import com.example.rasago.ui.theme.order.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val menuViewModel: MenuViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RasagoApp {
                AppNavigation(
                    menuViewModel = menuViewModel,
                    orderViewModel = orderViewModel
                )

            }
        }
    }
}