package com.example.rasago.theme.menu

import OrderViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.rasago.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rasago.data.model.MenuItem
import com.example.rasago.data.model.OrderItem
import com.example.rasago.theme.navigation.CustomerBottomNavigationBar
import com.example.rasago.theme.navigation.StaffBottomNavigationBar
import com.example.rasago.theme.profile.UserRole
import com.example.rasago.ui.theme.menu.MenuViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MenuScreen(
    navController: NavController,
        menuViewModel: MenuViewModel = hiltViewModel(),
        orderViewModel: OrderViewModel = hiltViewModel()
) {
    var role by remember { mutableStateOf(UserRole.NONE) }
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedNavItem by remember { mutableStateOf("Menu") }

    val showLogin = role == UserRole.NONE
    val isStaff = role != UserRole.NONE && role != UserRole.CUSTOMER

    val menuItems = menuViewModel.menuItems

    val cartItems = orderViewModel.cartItems

    when {
        showLogin -> {
            LoginScreen(
                onLoginAsCustomer = { role = UserRole.CUSTOMER },
                onLoginAsStaff = { role = UserRole.MANAGER }
            )
        }

        else -> {
            CustomerMenuContent(
                cartItemCount = cartItems.size,
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                selectedCategory = selectedCategory,
                onCategorySelect = { selectedCategory = it },
                onAddToCart = { menuItem ->
                    val orderItem = OrderItem(
                        id = menuItem.id,
                        name = menuItem.name,
                        price = menuItem.price,
                        quantity = 1
                    )
                    orderViewModel.addToCart(orderItem)
                },
                selectedNavItem = selectedNavItem,
                onNavItemSelect = { selectedNavItem = it },
                isStaff = false, //TODO: Pass from login
                foodList = menuItems,
                onCartClick = { navController.navigate("cart") }
            )
        }
    }
}




// Customer menu content
@Composable
fun CustomerMenuContent(
    cartItemCount: Int,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    onAddToCart: (MenuItem) -> Unit,
    selectedNavItem: String,
    onNavItemSelect: (String) -> Unit,
    isStaff: Boolean,
    foodList: List<MenuItem>,
    onCartClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Placeholder for banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) { Text("Banner", color = Color.White) }

        // Search bar
        TextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            placeholder = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Category buttons
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "Rice", "Noodles", "Drinks", "Side Dishes", "Desserts").forEach { category ->
                Button(
                    onClick = { onCategorySelect(category) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (category == selectedCategory) Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                        contentColor = if (category == selectedCategory) Color.White else Color.Black
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) { Text(category) }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))


        // Food grid
        val filteredFoods = foodList.filter {
            (selectedCategory == "All" || it.category == selectedCategory) &&
                    (searchText.isEmpty() || it.name.contains(searchText, ignoreCase = true))
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(filteredFoods) { menuItem ->
                MenuItemCard(
                    food = menuItem,
                    onItemClick = { onAddToCart(menuItem) }
                )
            }
        }

        // Cart floating
        if (cartItemCount > 0) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), contentAlignment = Alignment.Center) {
                Text("Cart: $cartItemCount items", color = Color.White)
            }
        }

        // Bottom navigation
        if (isStaff) {
            StaffBottomNavigationBar(selectedNavItem = selectedNavItem, onNavItemSelect = onNavItemSelect)
        } else {
            CustomerBottomNavigationBar(selectedNavItem = selectedNavItem, onNavItemSelect = onNavItemSelect, cartItemCount = cartItemCount)
        }
    }
}

// Menu item card
@Composable
fun MenuItemCard(food: MenuItem, onItemClick: (MenuItem) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(food) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
        )
        Text(food.name, fontWeight = FontWeight.Bold, modifier = Modifier.padding(4.dp))
        Text("RM ${food.price}", modifier = Modifier.padding(4.dp))
    }
}

// Login screen
@Composable
fun LoginScreen(
    onLoginAsCustomer: () -> Unit,
    onLoginAsStaff: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onLoginAsCustomer, modifier = Modifier.padding(16.dp)) { Text("Login as Customer") }
        Button(onClick = onLoginAsStaff, modifier = Modifier.padding(16.dp)) { Text("Login as Staff") }
    }
}
