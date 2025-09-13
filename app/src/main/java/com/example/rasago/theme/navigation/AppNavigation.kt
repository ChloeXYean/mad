package com.example.rasago.theme.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rasago.theme.menu.LoginScreen
import com.example.rasago.theme.menu.MenuScreen
import com.example.rasago.theme.order.OrderSummaryScreen
import com.example.rasago.ui.theme.menu.MenuViewModel
import com.example.rasago.ui.theme.order.OrderViewModel

@Composable
fun AppNavigation(
    menuViewModel: MenuViewModel = viewModel(),
    orderViewModel: OrderViewModel = viewModel()
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login"
    ){
        composable("login"){ //TODO: Need to check if staff or guest
            LoginScreen(false, onLoginAsStaff = { navController.navigate("menu")}, onLoginAsCustomer = { navController.navigate("menu")})
        }

        composable("menu"){
            MenuScreen(initialShowLogin = false, menuViewModel = menuViewModel, onNavigateToCart = { navController.navigate("cart")})
        }

        composable ("cart"){
            OrderSummaryScreen(
                onBackClick = { },
                onAddItemClick = { },
                onQuantityChange = { _, _ -> },
                selectedPaymentMethod = 0,
                onPaymentMethodSelect = { },
                onPlaceOrderClick = { })
        }
    }
}