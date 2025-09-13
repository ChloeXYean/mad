package com.example.rasago


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rasago.ui.theme.RasagoApp


// 食物数据类
data class Food(
    val name: String,
    val price: Double,
    val imageRes: Int,
    val category: String,
    val isRecommended: Boolean = false
)

// 导航项数据类
data class NavItem(
    val title: String,
    val icon: ImageVector
)

// 订单数据类
data class Order(
    val id: Int,
    val tableNumber: Int,
    val items: List<Food>,
    val status: OrderStatus,
    val timestamp: String
)

// 订单状态枚举
enum class OrderStatus {
    NEW, PREPARING, READY, COMPLETED, CANCELLED
}

// 主屏幕
@Composable
fun MenuScreen() {
    // 状态管理
    var cartItemCount by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedNavItem by remember { mutableStateOf("Menu") } // 记录选中的导航项
    var isStaff by remember { mutableStateOf(false) } // false=顾客，true=员工

    // 根据选中的导航项和用户类型显示不同屏幕
    when {
        isStaff && selectedNavItem == "Staff" -> {
            StaffMenuScreen(
                onBackToMenu = { selectedNavItem = "Menu" },
                onLogout = {
                    isStaff = false
                    selectedNavItem = "Menu"
                }
            )
        }
        selectedNavItem == "Log Out" -> {
            // 切换用户类型或退出登录
            LoginScreen(
                isStaff = isStaff,
                onLoginAsStaff = { isStaff = true; selectedNavItem = "Menu" },
                onLoginAsCustomer = { isStaff = false; selectedNavItem = "Menu" }
            )
        }
        else -> {
            // 顾客菜单屏幕
            CustomerMenuContent(
                cartItemCount = cartItemCount,
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                selectedCategory = selectedCategory,
                onCategorySelect = { selectedCategory = it },
                onAddToCart = { cartItemCount++ },
                selectedNavItem = selectedNavItem,
                onNavItemSelect = { selectedNavItem = it }
            )
        }
    }
}

// 顾客菜单内容
@Composable
fun CustomerMenuContent(
    cartItemCount: Int,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    onAddToCart: () -> Unit,
    selectedNavItem: String,
    onNavItemSelect: (String) -> Unit
) {
    // 食物列表数据
    val foodList = remember {
        listOf(
            Food("Nasi Lemak", 8.5, R.drawable.rice_nasilemak, "Rice", true),
            Food("Asam Laksa", 10.0, R.drawable.noodle_asamlaksa, "Noodles", true),
            Food("Curry Mee", 9.5, R.drawable.noodle_currymee, "Noodles"),
            Food("Otak Otak", 7.0, R.drawable.side_otakotak, "Side Dishes"),
            Food("Chicken Rice", 9.0, R.drawable.rice_chickenrice, "Rice"),
            Food("Char Kuey Teow", 11.0, R.drawable.noodle_charkueyteow, "Noodles"),
            Food("Satay", 12.0, R.drawable.side_chickensatay, "Side Dishes"),
            Food("Keropok Lekor", 4.0, R.drawable.side_lekor, "Side Dishes"),
            Food("Cendol", 5.0, R.drawable.dessert_cendol, "Desserts"),
            Food("Ais Kacang", 5.0, R.drawable.dessert_abc, "Desserts"),
            Food("Teh C", 3.5, R.drawable.drink_tehtarik, "Drinks"),
            Food("Milo Ais", 4.0, R.drawable.drink_miloais, "Drinks")
        )
    }

    // 根据分类和搜索文本筛选食物
    val filteredFoods = foodList.filter { food ->
        (selectedCategory == "All" || food.category == selectedCategory) &&
                (searchText.isEmpty() || food.name.contains(searchText, ignoreCase = true))
    }

    // 主布局结构
    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部横幅
        Banner(modifier = Modifier.height(180.dp))

        // 搜索栏
        SearchBar(
            searchText = searchText,
            onSearchTextChange = onSearchTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // 分类筛选按钮（水平滚动）
        CategoryScrollableButtons(
            categories = listOf("All", "Rice", "Noodles", "Drinks", "Side Dishes", "Desserts"),
            selectedCategory = selectedCategory,
            onCategorySelect = onCategorySelect,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        // 推荐标题
        Text(
            text = " Recommended for you",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        // 双列食物网格
        FoodGrid(
            foodList = filteredFoods,
            onItemClick = { food ->
                // 点击食物图片进入详情页
                println("进入详情页: ${food.name}")
            },
            modifier = Modifier.weight(1f)
        )

        // 购物车悬浮按钮（添加物品后显示）
        if (cartItemCount > 0) {
            CartFloatingView(
                count = cartItemCount,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 80.dp)
            )
        }

        // 底部导航栏
        CustomerBottomNavigationBar(
            cartItemCount = cartItemCount,
            selectedNavItem = selectedNavItem,
            onNavItemSelect = onNavItemSelect
        )
    }
}

// 员工菜单屏幕
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffMenuScreen(
    onBackToMenu: () -> Unit,
    onLogout: () -> Unit
) {
    // 模拟订单数据
    val orders = remember {
        listOf(
            Order(
                id = 101,
                tableNumber = 3,
                items = listOf(
                    Food("Nasi Lemak", 8.5, R.drawable.rice_nasilemak, "Rice", true),
                    Food("Teh C", 3.5, R.drawable.drink_tehtarik, "Drinks")
                ),
                status = OrderStatus.NEW,
                timestamp = "10:30 AM"
            ),
            Order(
                id = 102,
                tableNumber = 5,
                items = listOf(
                    Food("Asam Laksa", 10.0, R.drawable.noodle_asamlaksa, "Noodles", true),
                    Food("Otak Otak", 7.0, R.drawable.side_otakotak, "Side Dishes"),
                    Food("Milo Ais", 4.0, R.drawable.drink_miloais, "Drinks")
                ),
                status = OrderStatus.PREPARING,
                timestamp = "10:25 AM"
            ),
            Order(
                id = 103,
                tableNumber = 2,
                items = listOf(
                    Food("Char Kuey Teow", 11.0, R.drawable.noodle_charkueyteow, "Noodles"),
                    Food("Cendol", 5.0, R.drawable.dessert_cendol, "Desserts")
                ),
                status = OrderStatus.READY,
                timestamp = "10:15 AM"
            )
        )
    }

    var selectedOrder by remember { mutableStateOf<Order?>(null) }
    var expandedOrderId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Staff Profile",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // 订单统计卡片
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    OrderStatCard(
                        title = "New",
                        count = orders.count { it.status == OrderStatus.NEW },
                        color = Color(0xFFFF5252),
                        modifier = Modifier.weight(1f)
                    )
                    OrderStatCard(
                        title = "Preparing",
                        count = orders.count { it.status == OrderStatus.PREPARING },
                        color = Color(0xFFFFC107),
                        modifier = Modifier.weight(1f)
                    )
                    OrderStatCard(
                        title = "Ready",
                        count = orders.count { it.status == OrderStatus.READY },
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                }

                // 订单列表标题
                Text(
                    "Recent Orders",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // 订单列表
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(orders) { order ->
                        OrderItem(
                            order = order,
                            isExpanded = expandedOrderId == order.id,
                            onExpandToggle = {
                                expandedOrderId = if (expandedOrderId == order.id) null else order.id
                            },
                            onStatusChange = { newStatus ->
                                // 这里会更新订单状态
                                println("Order ${order.id} status changed to $newStatus")
                            }
                        )
                    }
                }
            }
        }
    )
}

// 订单统计卡片
@Composable
fun OrderStatCard(
    title: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                title,
                color = Color.White,
                fontSize = 14.sp
            )
            Text(
                count.toString(),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// 订单项组件
@Composable
fun OrderItem(
    order: Order,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
    onStatusChange: (OrderStatus) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // 订单头部 - 可点击展开/折叠
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandToggle() }
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Order #${order.id}",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Table ${order.tableNumber} • ${order.timestamp}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 状态标签
                    Box(
                        modifier = Modifier
                            .background(
                                when (order.status) {
                                    OrderStatus.NEW -> Color(0xFFFFEBEE)
                                    OrderStatus.PREPARING -> Color(0xFFFFF8E1)
                                    OrderStatus.READY -> Color(0xE8F5E9)
                                    OrderStatus.COMPLETED -> Color(0xE0F7FA)
                                    OrderStatus.CANCELLED -> Color(0xFFEEEEEE)
                                },
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            order.status.name,
                            fontSize = 12.sp,
                            color = when (order.status) {
                                OrderStatus.NEW -> Color(0xFFD32F2F)
                                OrderStatus.PREPARING -> Color(0xFFF57C00)
                                OrderStatus.READY -> Color(0xFF388E3C)
                                OrderStatus.COMPLETED -> Color(0xFF0097A7)
                                OrderStatus.CANCELLED -> Color(0xFF757575)
                            }
                        )
                    }

                    // 展开/折叠图标
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // 展开状态时显示的内容
            if (isExpanded) {
                Divider()

                // 订单项列表
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Items:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))

                    order.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item.name)
                            Text("RM ${String.format("%.2f", item.price)}")
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 订单总价
                    val total = order.items.sumOf { it.price }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total:", fontWeight = FontWeight.Bold)
                        Text("RM ${String.format("%.2f", total)}", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 状态更新按钮
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 根据当前状态显示适当的按钮
                        when (order.status) {
                            OrderStatus.NEW -> {
                                Button(
                                    onClick = { onStatusChange(OrderStatus.PREPARING) },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Start Preparing")
                                }
                                Button(
                                    onClick = { onStatusChange(OrderStatus.CANCELLED) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Cancel")
                                }
                            }
                            OrderStatus.PREPARING -> {
                                Button(
                                    onClick = { onStatusChange(OrderStatus.READY) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Mark as Ready")
                                }
                            }
                            OrderStatus.READY -> {
                                Button(
                                    onClick = { onStatusChange(OrderStatus.COMPLETED) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Mark as Completed")
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

// 登录/切换用户类型屏幕
@Composable
fun LoginScreen(
    isStaff: Boolean,
    onLoginAsStaff: () -> Unit,
    onLoginAsCustomer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Select User Type",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = onLoginAsCustomer,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isStaff) Color(0xFF00796B) else Color(0xFFE0E0E0),
                contentColor = if (!isStaff) Color.White else Color.Black
            )
        ) {
            Text("Login as Customer", fontSize = 18.sp)
        }

        Button(
            onClick = onLoginAsStaff,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isStaff) Color(0xFF00796B) else Color(0xFFE0E0E0),
                contentColor = if (isStaff) Color.White else Color.Black
            )
        ) {
            Text("Login as Staff", fontSize = 18.sp)
        }
    }
}

// 顶部横幅
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

// 搜索栏
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

// 可滚动的分类按钮
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
                            Color(0xFF4CAF50) else Color(0xFFE0E0E0), // 选中为深绿，未选中为浅灰
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

// 食物网格组件
@Composable
fun FoodGrid(
    foodList: List<Food>,
    onItemClick: (Food) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp), // 增加间距使布局更清晰
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

// 菜单项卡片 - 图片和信息完全分离
@Composable
fun MenuItemCard(
    food: Food,
    onItemClick: (Food) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(food) } // 点击整个项进入详情页
    ) {
        // 食物图片（只有图片有背景效果）
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray) // 图片区域背景
        ) {
            Image(
                painter = painterResource(food.imageRes),
                contentDescription = food.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (food.isRecommended) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                        .background(Color(0xFFE65100), RoundedCornerShape(20.dp)) // 深橘色推荐标签
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

        // 食物名称和价格（无背景，直接显示在浅灰背景上）
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
            color = Color.Black, // 黑色价格
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
    }
}

// 购物车悬浮提示
@Composable
fun CartFloatingView(
    count: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF00CED1), // 青色
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 8.dp,
        modifier = modifier
            .clickable {
                // 点击进入购物车页面的逻辑
            }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Order $count item${if (count > 1) "s" else ""}",
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

// 顾客底部导航栏
@Composable
fun CustomerBottomNavigationBar(
    cartItemCount: Int,
    selectedNavItem: String,
    onNavItemSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.height(64.dp),
        containerColor = Color.White,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val navItems = listOf(
            NavItem("Menu", Icons.Default.Home),
            NavItem("Orders", Icons.Default.ShoppingCart),
            NavItem("Log Out", Icons.Default.Logout),
            NavItem("Profile", Icons.Default.Person)
        )

        navItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    // 订单项显示购物车数量徽章
                    if (item.title == "Orders") {
                        BadgedBox(
                            badge = {
                                if (cartItemCount > 0) {
                                    Badge {
                                        Text(cartItemCount.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(
                                item.icon,
                                contentDescription = item.title,
                                tint = if (item.title == selectedNavItem) Color(0xFF00796B) else Color.Black
                            )
                        }
                    } else {
                        Icon(
                            item.icon,
                            contentDescription = item.title,
                            tint = if (item.title == selectedNavItem) Color(0xFF4CAF50) else Color.Black
                        )
                    }
                },
                label = {
                    Text(
                        item.title,
                        color = if (item.title == selectedNavItem) Color(0xFF00796B) else Color.Black
                    )
                },
                selected = item.title == selectedNavItem,
                onClick = { onNavItemSelect(item.title) }
            )
        }
    }
}

// 员工底部导航栏
@Composable
fun StaffBottomNavigationBar(
    selectedNavItem: String,
    onNavItemSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.height(64.dp),
        containerColor = Color.White,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val navItems = listOf(
            NavItem("Menu", Icons.Default.Home),
            NavItem("Staff", Icons.Default.Person),  // 员工专属页面
            NavItem("Log Out", Icons.Default.Logout)
        )

        navItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        tint = if (item.title == selectedNavItem) Color(0xFF00796B) else Color.Black
                    )
                },
                label = {
                    Text(
                        item.title,
                        color = if (item.title == selectedNavItem) Color(0xFF00796B) else Color.Black
                    )
                },
                selected = item.title == selectedNavItem,
                onClick = { onNavItemSelect(item.title) }
            )
        }
    }
}

// 预览
@Preview(showBackground = true, name = "Restaurant Screen Preview")
@Composable
fun RestaurantScreenPreview() {
    RasagoApp {
        MenuScreen()
    }
}

