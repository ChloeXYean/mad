package com.example.rasago.theme.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
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

// Customer Bottom Navigation Bar
@Composable
fun CustomerBottomNavigationBar(
    cartItemCount: Int,
    selectedNavItem: String,
    onNavItemSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.height(64.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        // "Log Out" has been removed and "Cart" and "Orders" are now separate
        val navItems = listOf(
            NavItem("Menu", Icons.Default.Home),
            NavItem("Cart", Icons.Default.ShoppingCart),
            NavItem("Orders", Icons.Default.ListAlt),
            NavItem("Profile", Icons.Default.Person)
        )

        navItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    if (item.title == "Cart") {
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
                                tint = if (item.title == selectedNavItem) Color(0xFF00796B) else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    } else {
                        Icon(
                            item.icon,
                            contentDescription = item.title,
                            tint = if (item.title == selectedNavItem) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                label = {
                    Text(
                        item.title,
                        color = if (item.title == selectedNavItem) Color(0xFF00796B) else MaterialTheme.colorScheme.onBackground
                    )
                },
                selected = item.title == selectedNavItem,
                onClick = { onNavItemSelect(item.title) }
            )
        }
    }
}

// Staff Bottom Navigation Bar
@Composable
fun StaffBottomNavigationBar(
    cartItemCount: Int = 0,
    selectedNavItem: String,
    onNavItemSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.height(64.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        // "Log Out" has been removed from this list
        val navItems = listOf(
            NavItem("Menu", Icons.Default.Home),
            NavItem("Cart", Icons.Default.ShoppingCart),
            NavItem("Staff", Icons.Default.Person)
        )

        navItems.forEach { item ->
            NavigationBarItem(
                icon = {

                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        tint = if (item.title == selectedNavItem) Color(0xFF00796B) else MaterialTheme.colorScheme.onBackground
                    )

                    if (item.title == "Cart") {
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
                            tint = if (item.title == selectedNavItem) Color(0xFF00796B) else Color.Black
                        )
                    }

                },
                label = {
                    Text(
                        item.title,
                        color = if (item.title == selectedNavItem) Color(0xFF00796B) else MaterialTheme.colorScheme.onBackground
                    )
                },
                selected = item.title == selectedNavItem,
                onClick = { onNavItemSelect(item.title) }
            )
        }
    }
}

