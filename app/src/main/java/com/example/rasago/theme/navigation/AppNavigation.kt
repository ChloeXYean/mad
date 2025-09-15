package com.example.rasago.theme.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rasago.order.OrderHistoryViewModel
import com.example.rasago.order.OrderViewModel
import com.example.rasago.theme.auth.LoginScreen as AuthLoginScreen
import com.example.rasago.theme.auth.RegisterScreen
import com.example.rasago.theme.menu.AddMenuItemScreen
import com.example.rasago.theme.menu.EditMenuItemScreen
import com.example.rasago.theme.menu.FoodDetailScreen
import com.example.rasago.theme.menu.MenuManagementScreen
import com.example.rasago.theme.menu.MenuScreen
import com.example.rasago.theme.order.FoodStatusScreen
import com.example.rasago.theme.order.OrderHistoryScreen
import com.example.rasago.theme.order.OrderSummaryScreen
import com.example.rasago.theme.payment.OrderConfirmationScreen
import com.example.rasago.theme.profile.ProfileScreen
import com.example.rasago.theme.profile.StaffProfileScreen
import com.example.rasago.theme.utils.RoleDetector
import com.example.rasago.ui.theme.menu.MenuViewModel

@Composable
fun AppNavigation(
    menuViewModel: MenuViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val menuItems by menuViewModel.menuItems.collectAsState()
    val orderState by orderViewModel.uiState.collectAsState()

    // Hoisted state for payment method, shared between OrderSummary and OrderConfirmation
    var selectedPaymentMethod by remember { mutableStateOf(0) }
    val paymentMethodName = when (selectedPaymentMethod) {
        0 -> "QR Scan"
        1 -> "Cash"
        2 -> "Card"
        else -> "QR Scan"
    }

    NavHost(
        navController = navController,
        startDestination = "auth_flow"
    ) {
        // --- Authentication Flow ---
        composable("auth_flow") {
            AuthLoginScreen(
                navController = navController,
                onLoginSuccess = { isStaff ->
                    val destination = if (isStaff) "staff_menu" else "menu"
                    navController.navigate(destination) {
                        popUpTo("auth_flow") { inclusive = true }
                    }
                }
            )
        }
        composable("register") {
            RegisterScreen(
                navController = navController,
                onRegisterSuccess = { navController.popBackStack() }
            )
        }

        // --- Main App Flow ---
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

        composable(
            route = "foodDetail/{menuItemId}?editMode={editMode}&cartItemIndex={cartItemIndex}",
            arguments = listOf(
                navArgument("menuItemId") { type = NavType.IntType },
                navArgument("editMode") { type = NavType.BoolType; defaultValue = false },
                navArgument("cartItemIndex") { type = NavType.IntType; defaultValue = -1 }
            )
        ) { backStackEntry ->
            val menuItemId = backStackEntry.arguments?.getInt("menuItemId") ?: 0
            val editMode = backStackEntry.arguments?.getBoolean("editMode") ?: false
            val cartItemIndex = backStackEntry.arguments?.getInt("cartItemIndex") ?: -1

            // Ensure the correct menu item is selected before showing the screen
            LaunchedEffect(menuItemId) {
                menuViewModel.selectMenuItem(menuItemId)
            }

            FoodDetailScreen(
                menuViewModel = menuViewModel,
                orderViewModel = orderViewModel,
                onBackClick = { navController.popBackStack() },
                editMode = editMode,
                existingCartItem = if (editMode && cartItemIndex != -1) orderState.orderItems.getOrNull(cartItemIndex) else null,
                cartItemIndex = cartItemIndex
            )
        }

        composable("order_summary") {
            OrderSummaryScreen(
                orderViewModel = orderViewModel,
                onNavigateToOrderConfirmation = { navController.navigate("order_confirmation") },
                onNavigateBack = { navController.popBackStack() },
                onAddItemClick = { navController.navigate("menu") },
                onEditItem = { cartItem, index ->
                    navController.navigate("foodDetail/${cartItem.menuItem.id}?editMode=true&cartItemIndex=$index")
                },
                selectedPaymentMethod = selectedPaymentMethod,
                onPaymentMethodSelect = { selectedPaymentMethod = it }
            )
        }

        composable("order_confirmation") {
            OrderConfirmationScreen(
                cartItems = orderState.orderItems,
                paymentMethod = paymentMethodName,
                onContinueClick = {
                    orderViewModel.saveOrder(
                        customerId = 1, // Placeholder for logged-in user ID
                        paymentMethod = paymentMethodName
                    )
                    orderViewModel.clearOrder()
                    navController.navigate("menu") {
                        popUpTo("menu") { inclusive = true }
                    }
                },
                onChangePaymentClick = { navController.popBackStack() },
                onCancelClick = { navController.popBackStack() }
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
                onBackClick = { navController.popBackStack() },
                // This role would come from the logged-in user's profile
                role = RoleDetector.ROLE_KITCHEN
            )
        }

        composable("profile") {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("auth_flow") {
                        popUpTo("menu") { inclusive = true }
                    }
                }
            )
        }

        composable("staff_profile") {
            StaffProfileScreen(
                onBackClick = { navController.popBackStack() },
                onManageMenuClicked = { navController.navigate("menu_management") },
                onLogout = {
                    navController.navigate("auth_flow") {
                        popUpTo("staff_menu") { inclusive = true }
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
                onDeleteItemClicked = { menuViewModel.deleteMenuItem(it) },
                onBackClick = { navController.popBackStack() }
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
                menuItem = menuViewModel.selectedMenuItem.collectAsState().value,
                onUpdateItemClicked = { id, name, desc, price, cat, uri ->
                    menuViewModel.updateMenuItem(id, name, desc, price, cat, uri)
                    navController.popBackStack()
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

