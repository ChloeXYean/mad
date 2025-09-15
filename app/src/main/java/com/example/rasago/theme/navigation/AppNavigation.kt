package com.example.rasago.theme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rasago.order.OrderHistoryViewModel
import com.example.rasago.order.OrderViewModel
import com.example.rasago.theme.auth.AuthViewModel
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
import com.example.rasago.theme.payment.ReceiptScreen
import com.example.rasago.theme.payment.customer.CashPaymentScreen
import com.example.rasago.theme.payment.customer.DebitCreditCardPaymentScreen
import com.example.rasago.theme.profile.EditProfileScreen
import com.example.rasago.theme.profile.ProfileScreen
import com.example.rasago.theme.utils.RoleDetector
import com.example.rasago.ui.theme.menu.MenuViewModel
import com.example.rasago.theme.profile.Staff
import com.example.rasago.theme.profile.StaffMain
import com.example.rasago.theme.profile.StaffStatus
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AppNavigation(
    menuViewModel: MenuViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val menuItems by menuViewModel.menuItems.collectAsState()
    val orderState by orderViewModel.uiState.collectAsState()
    val loginState by authViewModel.loginState.collectAsState()

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
        startDestination = "auth_flow"
    ) {
        // --- Authentication Flow ---
        composable("auth_flow") {
            AuthLoginScreen(
                authViewModel = authViewModel,
                navController = navController
            )
            // Handle successful login navigation
            LaunchedEffect(loginState) {
                if (loginState.isLoginSuccess) {
                    val destination = if (loginState.isStaff) "staff_menu" else "menu"
                    navController.navigate(destination) {
                        popUpTo("auth_flow") { inclusive = true }
                    }
                }
            }
        }
        composable("register") {
            RegisterScreen(
                navController = navController,
                authViewModel = authViewModel,
                onRegisterSuccess = { navController.popBackStack() }
            )
        }

        // --- Customer App Flow ---
        composable("menu") {
            val historyViewModel: OrderHistoryViewModel = hiltViewModel()
            val navigateToOrderStatus by historyViewModel.navigateToOrderStatus.collectAsState()

            // This effect will trigger navigation when the state in the ViewModel changes.
            LaunchedEffect(navigateToOrderStatus) {
                navigateToOrderStatus?.let { orderId ->
                    if (orderId != -1) {
                        navController.navigate("food_status/$orderId")
                    } else {
                        // If no orders exist, navigate to the history screen which will show an empty state.
                        val customerId = loginState.customer?.customerId
                        navController.navigate("orders?customerId=${customerId ?: -1}")
                    }
                    historyViewModel.onNavigationComplete() // Reset the state
                }
            }

            var navigateToCart by remember { mutableStateOf(false) }
            var navigateToProfile by remember { mutableStateOf(false) }
            var navigateToFoodDetail by remember { mutableStateOf<Int?>(null) }
            
            LaunchedEffect(navigateToCart) {
                if (navigateToCart) {
                    navController.navigate("order_summary")
                    navigateToCart = false
                }
            }
            
            LaunchedEffect(navigateToProfile) {
                if (navigateToProfile) {
                    navController.navigate("profile")
                    navigateToProfile = false
                }
            }
            
            LaunchedEffect(navigateToFoodDetail) {
                navigateToFoodDetail?.let { menuItemId ->
                    navController.navigate("foodDetail/$menuItemId")
                    navigateToFoodDetail = null
                }
            }
            
            MenuScreen(
                foodList = menuItems,
                cartItemCount = orderState.orderItems.size,
                onNavigateToCart = { navigateToCart = true },
                onNavigateToOrders = {
                    navController.navigate("orders?customerId=${loginState.customer?.customerId ?: -1}")
                },
                onNavigateToProfile = { navigateToProfile = true },
                onFoodItemClicked = { menuItem ->
                    menuViewModel.selectMenuItem(menuItem.id)
                    navigateToFoodDetail = menuItem.id
                }
            )
        }

        // --- Staff App Flow ---
        composable("staff_menu") {
            var navigateToCart by remember { mutableStateOf(false) }
            var navigateToStaffProfile by remember { mutableStateOf(false) }
            var navigateToFoodDetail by remember { mutableStateOf<Int?>(null) }
            
            LaunchedEffect(navigateToCart) {
                if (navigateToCart) {
                    navController.navigate("order_summary")
                    navigateToCart = false
                }
            }
            
            LaunchedEffect(navigateToStaffProfile) {
                if (navigateToStaffProfile) {
                    navController.navigate("staff_profile")
                    navigateToStaffProfile = false
                }
            }
            
            LaunchedEffect(navigateToFoodDetail) {
                navigateToFoodDetail?.let { menuItemId ->
                    navController.navigate("foodDetail/$menuItemId")
                    navigateToFoodDetail = null
                }
            }
            
            MenuScreen(
                isStaff = true,
                foodList = menuItems,
                cartItemCount = orderState.orderItems.size,
                onNavigateToCart = { navigateToCart = true },
                onNavigateToStaffProfile = { navigateToStaffProfile = true },
                onFoodItemClicked = { menuItem ->
                    menuViewModel.selectMenuItem(menuItem.id)
                    navigateToFoodDetail = menuItem.id
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

            LaunchedEffect(menuItemId) {
                menuViewModel.selectMenuItem(menuItemId)
            }

            FoodDetailScreen(
                menuViewModel = menuViewModel,
                orderViewModel = orderViewModel,
                onBackClick = { navController.popBackStack() },
                editMode = editMode,
                existingCartItem = if (editMode && cartItemIndex != -1) orderState.orderItems.getOrNull(
                    cartItemIndex
                ) else null,
                cartItemIndex = cartItemIndex
            )
        }

        composable("order_summary") {
            var navigateToPayment by remember { mutableStateOf(false) }
            var navigateToMenu by remember { mutableStateOf(false) }
            var navigateToEditItem by remember { mutableStateOf<String?>(null) }
            
            LaunchedEffect(navigateToPayment) {
                if (navigateToPayment) {
                    when (orderState.paymentMethod) {
                        "Cash" -> navController.navigate("cash_payment")
                        "Card" -> navController.navigate("debit_payment")
                        // "QR Scan" will go to confirmation directly for now
                        else -> navController.navigate("order_confirmation")
                    }
                    navigateToPayment = false
                }
            }
            
            LaunchedEffect(navigateToMenu) {
                if (navigateToMenu) {
                    navController.navigate("menu")
                    navigateToMenu = false
                }
            }
            
            LaunchedEffect(navigateToEditItem) {
                navigateToEditItem?.let { editParams ->
                    navController.navigate("foodDetail/$editParams")
                    navigateToEditItem = null
                }
            }
            
            OrderSummaryScreen(
                orderViewModel = orderViewModel,
                onNavigateToPayment = { navigateToPayment = true },
                onNavigateBack = { navController.popBackStack() },
                onAddItemClick = { navigateToMenu = true },
                onEditItem = { cartItem, index ->
                    navigateToEditItem = "${cartItem.menuItem.id}?editMode=true&cartItemIndex=$index"
                }
            )
        }

        composable("cash_payment") {
            var navigateToConfirmation by remember { mutableStateOf(false) }
            
            LaunchedEffect(navigateToConfirmation) {
                if (navigateToConfirmation) {
                    navController.navigate("order_confirmation")
                    navigateToConfirmation = false
                }
            }
            
            CashPaymentScreen(
                orderViewModel = orderViewModel,
                onBackClick = { navController.popBackStack() },
                onPaymentSuccess = { navigateToConfirmation = true }
            )
        }

        composable("debit_payment") {
            var navigateToConfirmation by remember { mutableStateOf(false) }
            
            LaunchedEffect(navigateToConfirmation) {
                if (navigateToConfirmation) {
                    navController.navigate("order_confirmation")
                    navigateToConfirmation = false
                }
            }
            
            DebitCreditCardPaymentScreen(
                orderViewModel = orderViewModel,
                onBackClick = { navController.popBackStack() },
                onPaymentSuccess = { navigateToConfirmation = true }
            )
        }

        composable("order_confirmation") {
            var navigateToReceipt by remember { mutableStateOf<String?>(null) }
            var navigateToSummary by remember { mutableStateOf(false) }
            
            LaunchedEffect(navigateToReceipt) {
                navigateToReceipt?.let { orderNo ->
                    navController.navigate("receipt/$orderNo")
                    navigateToReceipt = null
                }
            }
            
            LaunchedEffect(navigateToSummary) {
                if (navigateToSummary) {
                    navController.navigate("order_summary")
                    navigateToSummary = false
                }
            }
            
            OrderConfirmationScreen(
                orderState = orderState,
                onContinueClick = {
                    val orderNo = "T${System.currentTimeMillis() % 1000}"
                    orderViewModel.saveOrder(
                        customerId = loginState.customer?.customerId ?: 1,
                    )
                    navigateToReceipt = orderNo
                },
                onChangePaymentClick = { navigateToSummary = true },
                onCancelClick = { navController.popBackStack() }
            )
        }

        composable(
            route = "receipt/{orderNo}?isHistorical={isHistorical}",
            arguments = listOf(
                navArgument("orderNo") { type = NavType.StringType },
                navArgument("isHistorical") { type = NavType.BoolType; defaultValue = false }
            )
        ) { backStackEntry ->
            val orderNo = backStackEntry.arguments?.getString("orderNo") ?: "Unknown"
            val isHistorical = backStackEntry.arguments?.getBoolean("isHistorical") ?: false
            val receiptItems = orderState.orderItems.map { cartItem ->
                val itemName = cartItem.menuItem.name
                val addOnText = if (cartItem.selectedAddOns.any { it.quantity > 0 }) {
                    " + " + cartItem.selectedAddOns.filter { it.quantity > 0 }
                        .joinToString(", ") { "${it.name} x${it.quantity}" }
                } else ""
                val fullItemName = "$itemName$addOnText"
                fullItemName to cartItem.calculateTotalPrice()
            }

            var navigateToMenu by remember { mutableStateOf(false) }
            
            LaunchedEffect(navigateToMenu) {
                if (navigateToMenu) {
                    orderViewModel.clearOrder()
                    navController.navigate("menu") { popUpTo("menu") { inclusive = true } }
                    navigateToMenu = false
                }
            }
            
            ReceiptScreen(
                orderNo = orderNo,
                orderTime = SimpleDateFormat(
                    "dd/MM/yyyy HH:mm",
                    Locale.getDefault()
                ).format(Date()),
                orderType = orderState.orderType,
                orderItems = receiptItems,
                subtotal = orderState.subtotal,
                serviceCharge = orderState.serviceCharge,
                tax = orderState.tax,
                takeAwayCharge = orderState.takeAwayCharge,
                paymentMethod = orderState.paymentMethod,
                onBackClick = {
                    if (isHistorical) {
                        navController.popBackStack()
                    } else {
                        navigateToMenu = true
                    }
                },
                onProceedClick = {
                    navigateToMenu = true
                }
            )
        }

        composable(
            "orders?customerId={customerId}",
            arguments = listOf(navArgument("customerId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) {
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
                role = if (loginState.isStaff) loginState.staff?.role
                    ?: "" else RoleDetector.ROLE_CUSTOMER,
                onViewReceipt = {
                    navController.navigate("receipt/${orderId}?isHistorical=true")
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                customer = loginState.customer,
                staff = null,
                onBackClick = { navController.popBackStack() },
                onEditProfileClick = { navController.navigate("edit_profile") },
                onManageMenuClicked = { /* Not applicable for customers */ },
                onNavigateToOrders = { /* Not applicable for customers */ },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("auth_flow") {
                        popUpTo("profile") { inclusive = true }
                    }
                }
            )
        }

        composable("staff_profile") {
            ProfileScreen(
                customer = null,
                staff = loginState.staff,
                onBackClick = { navController.popBackStack() },
                onEditProfileClick = { navController.navigate("edit_profile") },
                onManageMenuClicked = { navController.navigate("menu_management") },
                onNavigateToOrders = { navController.navigate("order_history") },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("auth_flow") {
                        popUpTo("staff_profile") { inclusive = true }
                    }
                }
            )
        }

        composable("edit_profile") {
            val currentCustomer = remember { loginState.customer }
            currentCustomer?.let { customer ->
                EditProfileScreen(
                    customer = customer,
                    authViewModel = authViewModel,
                    onBackClick = { navController.popBackStack() },
                    onUpdateSuccess = { navController.popBackStack() }
                )
            }
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
            // --- Staff App Flow ---
            // --- 员工流程 ---
            navigation(startDestination = "staff_main", route = "staff_app") {
                composable("staff_main") {
                    var navigateToMenu by remember { mutableStateOf(false) }
                    var navigateToLogout by remember { mutableStateOf(false) }
                    
                    LaunchedEffect(navigateToMenu) {
                        if (navigateToMenu) {
                            navController.navigate("menu")
                            navigateToMenu = false
                        }
                    }
                    
                    LaunchedEffect(navigateToLogout) {
                        if (navigateToLogout) {
                            navController.navigate("login_flow") {
                                popUpTo("staff_app") { inclusive = true }
                            }
                            navigateToLogout = false
                        }
                    }
                    
                    StaffMain(
                        staff = staffData,
                        onMenuClick = { navigateToMenu = true },
                        onLogoutClick = { navigateToLogout = true },
                        navController = navController // 传递导航控制器
                    )
                }

                composable("order_history") {
                    OrderHistoryScreen(
                        onBackClick = { navController.popBackStack() },
                        onViewOrder = { orderId ->
                            navController.navigate("food_status/$orderId")
                        }
                    )
                }
            }
        }
    }
}
