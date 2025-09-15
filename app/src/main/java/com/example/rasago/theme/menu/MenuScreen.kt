package com.example.rasago.theme.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rasago.R
import com.example.rasago.data.model.MenuItem
import com.example.rasago.theme.navigation.CustomerBottomNavigationBar
import com.example.rasago.theme.navigation.StaffBottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    isStaff: Boolean = false,
    foodList: List<MenuItem>,
    cartItemCount: Int,
    onNavigateToCart: () -> Unit = {},
    onNavigateToOrders: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToStaffProfile: () -> Unit = {},
    onFoodItemClicked: (MenuItem) -> Unit,
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredFoods = foodList.filter { food ->
        (selectedCategory == "All" || food.category == selectedCategory) &&
                (searchText.isEmpty() || food.name.contains(searchText, ignoreCase = true))
    }

    Scaffold(
        bottomBar = {
            if (isStaff) {
                StaffBottomNavigationBar(
                    selectedNavItem = "Menu",
                    onNavItemSelect = { title ->
                        when (title) {
                            "Staff" -> onNavigateToStaffProfile()
                        }
                    }
                )
            } else {
                CustomerBottomNavigationBar(
                    cartItemCount = cartItemCount,
                    selectedNavItem = "Menu",
                    onNavItemSelect = { title ->
                        when (title) {
                            "Cart" -> onNavigateToCart()
                            "Orders" -> onNavigateToOrders()
                            "Profile" -> onNavigateToProfile()
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Banner(modifier = Modifier.height(180.dp))
                SearchBar(
                    searchText = searchText,
                    onSearchTextChange = { searchText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                CategoryScrollableButtons(
                    categories = listOf("All", "Rice", "Noodles", "Drinks", "Side Dishes", "Desserts"),
                    selectedCategory = selectedCategory,
                    onCategorySelect = { selectedCategory = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Text(
                    text = " Recommended for you",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                FoodGrid(
                    foodList = filteredFoods,
                    onItemClick = onFoodItemClicked,
                    modifier = Modifier.weight(1f)
                )
            }

            // Floating Cart View from friend's code
            if (cartItemCount > 0 && !isStaff) {
                CartFloatingView(
                    count = cartItemCount,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 80.dp), // Adjust padding to sit above the nav bar
                    onClick = onNavigateToCart
                )
            }
        }
    }
}

@Composable
fun Banner(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.banner),
            contentDescription = "Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            " Order Fast,\n Rasa Best!",
            color = Color.White,
            fontSize = 40.sp,
            lineHeight = 45.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }
}

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        placeholder = { Text(text = "Search") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .fillMaxWidth(),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
        )
    )
}

@Composable
fun CategoryScrollableButtons(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            categories.forEach { category ->
                Button(
                    onClick = { onCategorySelect(category) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (category == selectedCategory)
                            Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                        contentColor = if (category == selectedCategory) Color.White else Color.Black
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(text = category)
                }
            }
        }
    }
}

@Composable
fun FoodGrid(
    foodList: List<MenuItem>,
    onItemClick: (MenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(foodList) { food ->
            MenuItemCard(
                food = food,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
fun MenuItemCard(
    food: MenuItem,
    onItemClick: (MenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(food) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
        ) {
            // Intelligent image loading logic
            val imageModel = remember(food.photo) {
                food.photo.toIntOrNull() ?: food.photo
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageModel)
                    .crossfade(true)
                    .placeholder(R.drawable.ic_launcher_background) // Placeholder while loading
                    .error(R.drawable.ic_launcher_foreground) // Fallback image on error
                    .build(),
                contentDescription = food.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (food.isRecommended) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                        .background(Color(0xFFE65100), RoundedCornerShape(20.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Recommend",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = food.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Text(
            text = "RM ${String.format("%.2f", food.price)}",
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun CartFloatingView(
    count: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        color = Color(0xFF4CAF50),
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 8.dp,
        modifier = modifier
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "View Cart (${count} item${if (count > 1) "s" else ""})",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

