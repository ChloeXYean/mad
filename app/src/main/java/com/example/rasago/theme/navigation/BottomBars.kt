package com.example.rasago.theme.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class NavItem(
    val title: String,
    val icon: ImageVector
)
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