package com.example.rasago.theme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rasago.order.OrderHistoryViewModel
import com.example.rasago.order.OrderViewModel
import com.example.rasago.theme.menu.AddMenuItemScreen
import com.example.rasago.theme.menu.EditMenuItemScreen
import com.example.rasago.theme.menu.FoodDetailScreen
import com.example.rasago.theme.menu.LoginScreen
import com.example.rasago.theme.menu.MenuManagementScreen
import com.example.rasago.theme.menu.MenuScreen
import com.example.rasago.theme.order.OrderConfirmationScreen
import com.example.rasago.theme.order.FoodStatusScreen
import com.example.rasago.theme.order.OrderHistoryScreen
import com.example.rasago.theme.order.OrderSummaryScreen
import com.example.rasago.theme.profile.ProfileScreen
import com.example.rasago.theme.profile.StaffProfileScreen
import com.example.rasago.ui.theme.menu.MenuViewModel

@Composable
fun AppNavigation(
    menuViewModel: MenuViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val menuItems by menuViewModel.menuItems.collectAsState()
    val selectedMenuItem by menuViewModel.selectedMenuItem.collectAsState()
    val orderState by orderViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginAsCustomer = {
                    navController.navigate("menu") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onLoginAsStaff = {
                    navController.navigate("staff_menu") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("menu") {
            MenuScreen(
                foodList = menuItems,
                cartItemCount = orderState.orderItems.size,
                onNavigateToCart = { navController.navigate("order_summary") },
                onNavigateToOrders = { navController.navigate("orders") },
                onNavigateToProfile = { navController.navigate("profile") },
                onFoodItemClicked = { menuItem ->
                    menuViewModel.selectMenuItem(menuItem.id)
                    navController.navigate("foodDetail/${menuItem.id}")
                }
            )
        }

        composable("staff_menu") {
            MenuScreen(
                isStaff = true,
                foodList = menuItems,
                cartItemCount = orderState.orderItems.size,
                onNavigateToCart = { navController.navigate("order_summary") },
                onNavigateToStaffProfile = { navController.navigate("staff_profile") },
                onFoodItemClicked = { menuItem ->
                    menuViewModel.selectMenuItem(menuItem.id)
                    navController.navigate("foodDetail/${menuItem.id}")
                }
            )
        }

        composable("add_menu_item") {
            AddMenuItemScreen(
                onAddItemClicked = { name, description, price, category, imageUri ->
                    menuViewModel.addMenuItem(name, description, price, category, imageUri)
                    navController.popBackStack()
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = "edit_menu_item/{menuItemId}",
            arguments = listOf(navArgument("menuItemId") { type = NavType.IntType })
        ) {
            EditMenuItemScreen(
                menuItem = selectedMenuItem,
                onUpdateItemClicked = { id, name, description, price, category, imageUri ->
                    menuViewModel.updateMenuItem(id, name, description, price, category, imageUri)
                    navController.popBackStack()
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("orders") {
            OrderHistoryScreen(
                onBackClick = { navController.popBackStack() },
                onViewOrder = { orderId ->
                    navController.navigate("food_status/$orderId")
                }
            )
        }

        composable(
            route = "food_status/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
            val historyViewModel: OrderHistoryViewModel = hiltViewModel()

            LaunchedEffect(orderId) {
                historyViewModel.loadOrderDetails(orderId)
            }

            FoodStatusScreen(
                historyViewModel = historyViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("profile") {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("staff_profile") {
            StaffProfileScreen(
                onBackClick = { navController.popBackStack() },
                onManageMenuClicked = { navController.navigate("menu_management") },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("menu_management") {
            MenuManagementScreen(
                menuItems = menuItems,
                onAddItemClicked = { navController.navigate("add_menu_item") },
                onEditItemClicked = {
                    menuViewModel.selectMenuItem(it.id)
                    navController.navigate("edit_menu_item/${it.id}")
                },
                onDeleteItemClicked = {
                    menuViewModel.deleteMenuItem(it)
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = "foodDetail/{menuItemId}",
            arguments = listOf(navArgument("menuItemId") { type = NavType.IntType })
        ) {
            FoodDetailScreen(
                menuViewModel = menuViewModel,
                onBackClick = { navController.popBackStack() },
                onAddToCart = {
                    selectedMenuItem?.let { item ->
                        orderViewModel.addItemToOrder(item)
                        navController.popBackStack()
                    }
                }
            )
        }

        composable("order_summary") {
            OrderSummaryScreen(
                orderUiState = orderState,
                onNextButtonClicked = { navController.navigate("order_confirmation") },
                onCancelButtonClicked = { navController.popBackStack() },
                onAddItemClick = { navController.popBackStack() },
                onIncreaseItem = { orderViewModel.increaseItemQuantity(it) },
                onDecreaseItem = { orderViewModel.decreaseItemQuantity(it) },
                onRemoveItem = { orderViewModel.removeItemFromOrder(it) }
            )
        }

        composable("order_confirmation") {
            OrderConfirmationScreen(
                orderState = orderState,
                onBackButtonClicked = { navController.popBackStack() },
                onConfirmButtonClicked = {
                    orderViewModel.saveOrder(
                        orderType = "Dine-In",
                        paymentMethod = "Cash",
                        customerId = 1
                    )
                    orderViewModel.clearOrder()
                    navController.navigate("menu") {
                        popUpTo("menu") { inclusive = true }
                    }
                }
            )
        }
    }
}

