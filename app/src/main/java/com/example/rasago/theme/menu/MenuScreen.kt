import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rasago.ui.theme.RasagoApp
import com.example.rasago.R
import com.example.rasago.theme.navigation.CustomerBottomNavigationBar
import com.example.rasago.theme.navigation.StaffBottomNavigationBar
import com.example.rasago.data.model.StaffProfile

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

// 主菜单活动
class MainMenu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 接收从员工页面传递的"需要显示登录页"参数
        val showLogin = intent.getBooleanExtra("showLogin", false)

        setContent {
            RasagoApp {
                Surface(color = Color(0xFFF5F5F5)) {
                    MenuScreen(initialShowLogin = showLogin)
                }
            }
        }
    }
}

@Composable
fun MenuScreen(initialShowLogin: Boolean = false) {
    // 状态管理
    var cartItemCount by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedNavItem by remember { mutableStateOf("Menu") }
    var isStaff by remember { mutableStateOf(false) }
    // 关键：根据参数决定是否初始显示登录页
    var showLogin by remember { mutableStateOf(initialShowLogin) }

    val context = LocalContext.current

    when {
        // 显示登录选择页面（角色选择）
        showLogin || selectedNavItem == "Log Out" -> {
            LoginScreen(
                isStaff = isStaff,
                onLoginAsStaff = {
                    isStaff = true
                    showLogin = false
                    selectedNavItem = "Menu"
                },
                onLoginAsCustomer = {
                    isStaff = false
                    showLogin = false
                    selectedNavItem = "Menu"
                }
            )
        }
        // 员工点击Staff导航项时跳转员工页面
        isStaff && selectedNavItem == "Staff" -> {
            LaunchedEffect(Unit) {
                val intent = Intent(context, StaffProfile::class.java).apply {
                    putExtra("isStaff", isStaff)
                }
                context.startActivity(intent)
            }
        }
        else -> {
            CustomerMenuContent(
                cartItemCount = cartItemCount,
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                selectedCategory = selectedCategory,
                onCategorySelect = { selectedCategory = it },
                onAddToCart = { cartItemCount++ },
                selectedNavItem = selectedNavItem,
                onNavItemSelect = { selectedNavItem = it },
                isStaff = isStaff
            )
        }
    }
}

// 顾客菜单内容（修改版，支持动态切换导航栏）
@Composable
fun CustomerMenuContent(
    cartItemCount: Int,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    onAddToCart: () -> Unit,
    selectedNavItem: String,
    onNavItemSelect: (String) -> Unit,
    isStaff: Boolean // 新增：接收员工状态
) {
    // 食物列表数据（保持不变）
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

    // 根据分类和搜索文本筛选食物（保持不变）
    val filteredFoods = foodList.filter { food ->
        (selectedCategory == "All" || food.category == selectedCategory) &&
                (searchText.isEmpty() || food.name.contains(searchText, ignoreCase = true))
    }

    // 主布局结构（保持不变）
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

        // 底部导航栏（核心修改：根据员工状态切换）
        if (isStaff) {
            StaffBottomNavigationBar(
                selectedNavItem = selectedNavItem,
                onNavItemSelect = onNavItemSelect
            )
        } else {
            CustomerBottomNavigationBar(
                cartItemCount = cartItemCount,
                selectedNavItem = selectedNavItem,
                onNavItemSelect = onNavItemSelect
            )
        }
    }
}


// 在 MainMenu.kt 中确保登录选择界面逻辑正确
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

        // 顾客登录按钮
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

        // 员工登录按钮
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
