////package com.example.rasago.theme.navigation
////
////import LoginScreen
////import MenuScreen
////import androidx.compose.runtime.Composable
////import androidx.compose.runtime.getValue
////import androidx.compose.runtime.mutableStateOf
////import androidx.compose.runtime.remember
////import androidx.compose.runtime.setValue
////import androidx.hilt.navigation.compose.hiltViewModel
////import androidx.navigation.NavType
////import androidx.navigation.compose.NavHost
////import androidx.navigation.compose.composable
////import androidx.navigation.compose.navigation
////import androidx.navigation.compose.rememberNavController
////import androidx.navigation.navArgument
////import com.example.rasago.order.OrderViewModel
////import com.example.rasago.theme.order.OrderManagementScreen
////import com.example.rasago.ui.theme.menu.MenuViewModel
////
////
////@Composable
////fun AppNavigation(
////    menuViewModel: MenuViewModel = hiltViewModel(),
////    orderViewModel: OrderViewModel = hiltViewModel()
////) {
////    val navController = rememberNavController()
////
////    // track user role selection in local state
////    var isStaff by remember { mutableStateOf(false) }
////
////    NavHost(
////        navController = navController,
////        startDestination = "login"
////    ) {
////        // --- Login Flow ---
////        navigation(startDestination = "login", route = "login_flow") {
////            composable("login") {
////                LoginScreen(
////                    onLoginAsCustomer = {
////                        isStaff = false
////                        navController.navigate("customer_app") {
////                            popUpTo("login") { inclusive = true } // optional: remove login from back stack
////                        }
////                    },
////                    onLoginAsStaff = {
////                        isStaff = true
////                        navController.navigate("staff_app") {
////                            popUpTo("login") { inclusive = true }
////                        }
////                    }
////                )
////            }
////        }
////
////        // --- Customer App Flow ---
////        navigation(startDestination = "menu", route = "customer_app") {
////            composable("menu") {
////                MenuScreen(
////                    navController = navController,
////                    menuViewModel = menuViewModel,
////                    orderViewModel = orderViewModel
////                )
////            }
////
////            composable(
////                route = "foodDetail/{menuItemId}",
////                arguments = listOf(navArgument("menuItemId") { type = NavType.IntType })
////            ) { backStackEntry ->
////                val menuItemId = backStackEntry.arguments?.getInt("menuItemId") ?: 0
//////                FoodDetailScreen(
//////                    navController = navController,
//////                    menuItemId = menuItemId,
//////                    viewModel = menuViewModel
//////                )
////            }
////
////            composable("cart") {
//////                OrderSummaryScreen(navController = navController, viewModel = menuViewModel)
////            }
////        }
////
////        // --- Staff App Flow ---
////        navigation(startDestination = "order_management", route = "staff_app") {
////            composable("order_management") {
////                OrderManagementScreen(
////                    orderViewModel = orderViewModel,
////                    onBackClick = { navController.popBackStack() }
////                )
////            }
////        }
////    }
////}
//package com.example.rasago.theme.navigation
//
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.rasago.theme.menu.LoginScreen
//import com.example.rasago.theme.menu.MenuScreen
//import com.example.rasago.theme.order.OrderHistoryScreen
//import com.example.rasago.theme.profile.ProfileScreen
//import com.example.rasago.theme.profile.StaffProfileScreen
//
//@Composable
//fun AppNavigation() {
//    val navController = rememberNavController()
//
//    NavHost(
//        navController = navController,
//        startDestination = "login"
//    ) {
//        composable("login") {
//            LoginScreen(
//                onLoginAsCustomer = {
//                    navController.navigate("menu") {
//                        popUpTo("login") { inclusive = true }
//                    }
//                },
//                onLoginAsStaff = {
//                    navController.navigate("staff_menu") {
//                        popUpTo("login") { inclusive = true }
//                    }
//                }
//            )
//        }
//
//        composable("menu") {
//            MenuScreen(
//                onNavigateToOrders = { navController.navigate("orders") },
//                onNavigateToProfile = { navController.navigate("profile") },
//                onLogout = {
//                    navController.navigate("login") {
//                        popUpTo("menu") { inclusive = true }
//                    }
//                }
//            )
//        }
//
//        composable("staff_menu") {
//            MenuScreen(
//                isStaff = true,
//                onNavigateToStaffProfile = { navController.navigate("staff_profile")},
//                onLogout = {
//                    navController.navigate("login") {
//                        popUpTo("staff_menu") { inclusive = true }
//                    }
//                }
//            )
//        }
//
//        composable("orders") {
//            OrderHistoryScreen(onBackClick = { navController.popBackStack() })
//        }
//
//        composable("profile") {
//            ProfileScreen(onBackClick = { navController.popBackStack() })
//        }
//
//        composable("staff_profile") {
//            StaffProfileScreen(onBackClick = { navController.popBackStack() })
//        }
//    }
//}
package com.example.rasago.theme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rasago.order.OrderViewModel
import com.example.rasago.theme.menu.AddMenuItemScreen
import com.example.rasago.theme.menu.EditMenuItemScreen
import com.example.rasago.theme.menu.FoodDetailScreen
import com.example.rasago.theme.menu.LoginScreen
import com.example.rasago.theme.menu.MenuScreen
import com.example.rasago.theme.order.OrderConfirmationScreen
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
                cartItemCount = orderState.orderItems.size, // Pass the live cart count
                onNavigateToOrders = { navController.navigate("order_summary") },
                onNavigateToProfile = { navController.navigate("profile") },
                onLogout = {
                    navController.navigate("login") { popUpTo("menu") { inclusive = true } }
                },
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
                cartItemCount = 0,
                onNavigateToStaffProfile = { navController.navigate("staff_profile") },
                onLogout = {
                    navController.navigate("login") { popUpTo("staff_menu") { inclusive = true } }
                },
                onFoodItemClicked = { menuItem ->
                    menuViewModel.selectMenuItem(menuItem.id)
                    navController.navigate("foodDetail/${menuItem.id}")
                },
                onAddMenuItemClicked = {
                    navController.navigate("add_menu_item")
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
                    navController.popBackStack() // Go back to the detail screen
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("orders") {
            OrderHistoryScreen(onBackClick = { navController.popBackStack() })
        }

        composable("profile") {
            ProfileScreen(onBackClick = { navController.popBackStack() })
        }

        composable("staff_profile") {
            StaffProfileScreen(onBackClick = { navController.popBackStack() })
        }

        composable(
            route = "foodDetail/{menuItemId}/{isStaff}",
            arguments = listOf(
                navArgument("menuItemId") { type = NavType.IntType },
                navArgument("isStaff") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val isStaff = backStackEntry.arguments?.getBoolean("isStaff") ?: false
            FoodDetailScreen(
                menuViewModel = menuViewModel,
                isStaff = isStaff,
                onBackClick = { navController.popBackStack() },
                onAddToCart = {
                    selectedMenuItem?.let { item ->
                        orderViewModel.addItemToOrder(item)
                        navController.popBackStack()
                    }
                },
                onEditClick = {
                    navController.navigate("edit_menu_item/${it.id}")
                },
                onDeleteClick = {
                    menuViewModel.deleteMenuItem(it)
                    navController.popBackStack()
                }
            )
        }

        composable("order_summary") {
            OrderSummaryScreen(
                orderUiState = orderState, // Corrected parameter name
                onNextButtonClicked = { navController.navigate("order_confirmation") },
                onCancelButtonClicked = { navController.popBackStack() },
                onFoodItemClicked = {
                    // Navigate to the detail screen for the clicked item
                    menuViewModel.selectMenuItem(it.id)
                    navController.navigate("foodDetail/${it.id}")
                }
            )
        }

        composable("order_confirmation") {
            OrderConfirmationScreen(
                orderState = orderState, // Pass the live order state to the screen
                onBackButtonClicked = { navController.popBackStack() },
                onConfirmButtonClicked = {
                    // When the user confirms, clear the cart...
                    orderViewModel.clearOrder()
                    // ...and navigate back to the main menu, clearing the back stack.
                    navController.navigate("menu") {
                        popUpTo("menu") { inclusive = true }
                    }
                }
            )
        }
    }
}


