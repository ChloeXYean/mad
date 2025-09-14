package com.example.rasago.theme.navigation

import androidx.compose.runtime.*
import androidx.    hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rasago.theme.menu.LoginScreen
import com.example.rasago.theme.menu.MenuScreen
import com.example.rasago.theme.order.OrderManagementScreen
import com.example.rasago.theme.order.OrderSummaryScreen
import com.example.rasago.ui.theme.menu.MenuViewModel
import com.example.rasago.ui.theme.order.OrderViewModel

@Composable
fun AppNavigation(
    menuViewModel: MenuViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    // track user role selection in local state
    var isStaff by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = "login_flow"
    ) {
        // --- Login Flow ---
        navigation(startDestination = "login", route = "login_flow") {
            composable("login") {
                LoginScreen(
                    isStaff = isStaff,
                    onLoginAsCustomer = {
                        isStaff = false
                        navController.navigate("customer_app") {

                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onLoginAsStaff = {
                        isStaff = true
                        navController.navigate("staff_app") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
        }

        // --- Customer App Flow ---
        navigation(startDestination = "menu", route = "customer_app") {
            composable("menu") {
                MenuScreen(
                    navController = navController,
                    menuViewModel = menuViewModel

                )
            }

            composable(
                route = "foodDetail/{menuItemId}",
                arguments = listOf(navArgument("menuItemId") { type = NavType.IntType })
            ) { backStackEntry ->
                val menuItemId = backStackEntry.arguments?.getInt("menuItemId") ?: 0

                // Uncomment when FoodDetailScreen is ready
                // FoodDetailScreen(
                //     navController = navController,
                //     menuItemId = menuItemId,
                //     viewModel = menuViewModel
                // )
            }

            composable("cart") {
                OrderSummaryScreen(
                    onBackClick = { navController.popBackStack() },
                    onAddItemClick = {
                        // TODO: handle add item
                    },
                    onQuantityChange = { itemIndex, change ->
                        // TODO: handle quantity change
                    },
                    selectedPaymentMethod = 0, // TODO: hook into state
                    onPaymentMethodSelect = { methodIndex ->
                        // TODO: handle payment method select
                    },
                    onPlaceOrderClick = {
                        // TODO: place order logic
                        navController.popBackStack()
                    }
                )

            }
        }

        // --- Staff App Flow ---
        navigation(startDestination = "order_management", route = "staff_app") {
            composable("order_management") {
                OrderManagementScreen(
                    orderViewModel = orderViewModel,

                    onViewStatusClick = { order ->
                        // TODO: handle order status click
                    },

                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
