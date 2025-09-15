package com.example.rasago.theme.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rasago.theme.menu.LoginScreen
import com.example.rasago.theme.menu.MenuScreen
import com.example.rasago.theme.order.OrderConfirmationScreen
import com.example.rasago.theme.order.OrderManagementScreen
import com.example.rasago.theme.order.OrderSummaryScreen
import com.example.rasago.ui.theme.menu.MenuViewModel
import com.example.rasago.ui.theme.order.OrderViewModel
import com.example.rasago.theme.profile.Staff
import com.example.rasago.theme.profile.StaffMain
import com.example.rasago.theme.profile.StaffStatus
import com.example.rasago.theme.order.OrderHistoryScreen

@Composable
fun AppNavigation(
    menuViewModel: MenuViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    // track user role selection in local state
    var isStaff by remember { mutableStateOf(false) }

    // TEMPORARY
    val staffData = Staff(
        name = "Ali",
        role = "cashier",
        jobTime = "08:00 - 18:00",
        lastLogin = "2025-09-15 09:30",
        status = StaffStatus.WORKING
    )

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
        // --- 员工流程 ---
        navigation(startDestination = "staff_main", route = "staff_app") {
            composable("staff_main") {
                StaffMain(
                    staff = staffData,
                    onMenuClick = { navController.navigate("menu") },
                    onLogoutClick = {
                        navController.navigate("login_flow") {
                            popUpTo("staff_app") { inclusive = true }
                        }
                    },
                    navController = navController // 传递导航控制器
                )
            }
            composable("order_management") {
                OrderManagementScreen(
                    orderViewModel = orderViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("order_history") {
                OrderHistoryScreen(
                    isStaff = true,  // 明确标记为员工模式
                    cartItemCount = 0,  // 员工无需购物车计数
                    selectedNavItem = "Staff",  // 当前选中"Staff"项（与员工导航匹配）
                    onNavItemSelect = { selectedItem ->
                        // 根据底部导航选择处理跳转
                        when (selectedItem) {
                            "Menu" -> navController.navigate("menu")  // 跳转到菜单页面
                            "Staff" -> navController.navigate("staff_main")  // 跳转到员工主页
                            "Log Out" -> {
                                // 退出登录，回到登录页面
                                navController.navigate("login_flow") {
                                    popUpTo("staff_app") { inclusive = true }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

