package com.example.rasago.theme.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rasago.data.entity.StaffEntity
import com.example.rasago.theme.utils.RoleDetector
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffMainScreen(
    staff: StaffEntity?,
    onNavigateToMenu: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToStaffSchedule: () -> Unit,
    onNavigateToMenuManagement: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit,
    viewModel: StaffMainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(staff) {
        viewModel.setStaff(staff)
    }

    Scaffold(
        bottomBar = {
            StaffBottomNavigationBar(
                onHomeClick = { /* Already home */ },
                onProfileClick = onNavigateToProfile,
                onLogoutClick = onLogout
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            HeaderCard(staff = uiState.staff, status = uiState.status, onStatusChange = viewModel::updateStatus)
            Spacer(modifier = Modifier.height(24.dp))
            DashboardGrid(
                staffRole = staff?.role ?: "",
                onNavigateToMenu = onNavigateToMenu,
                onNavigateToOrders = onNavigateToOrders,
                onNavigateToStaffSchedule = onNavigateToStaffSchedule,
                onNavigateToMenuManagement = onNavigateToMenuManagement
            )
        }
    }
}

@Composable
private fun HeaderCard(staff: StaffEntity?, status: String, onStatusChange: (String) -> Unit) {
    val currentTime = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date()) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Welcome back,", style = MaterialTheme.typography.titleMedium)
            Text(staff?.name ?: "Staff", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Role: ${staff?.role?.replaceFirstChar { it.titlecase() } ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Status:", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                Button(
                    onClick = { onStatusChange("Working") },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (status.equals("active", true) || status.equals("Working", true)) Color(0xFF4CAF50) else Color.LightGray,
                        contentColor = if (status.equals("active", true) || status.equals("Working", true)) Color.White else Color.Black
                    )
                ) {
                    Text("Working")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { onStatusChange("On Break") },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (status == "On Break") Color(0xFFF8BE0B) else Color.LightGray,
                        contentColor = if (status == "On Break") Color.White else Color.Black
                    )
                ) {
                    Text("On Break")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(currentTime, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

@Composable
private fun DashboardGrid(
    staffRole: String,
    onNavigateToMenu: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToStaffSchedule: () -> Unit,
    onNavigateToMenuManagement: () -> Unit,
) {
    val allItems = listOf(
        DashboardItem("Orders\nManagement", Icons.Default.ReceiptLong, onNavigateToOrders, RoleDetector.canHandleOrders(staffRole)),
        DashboardItem("Staff\nSchedule", Icons.Default.People, onNavigateToStaffSchedule, RoleDetector.hasManagementPermission(staffRole)),
        DashboardItem("Menu", Icons.Default.RestaurantMenu, onNavigateToMenu, true), // All staff can view menu
        DashboardItem("Menu\nManagement", Icons.Default.MenuBook, onNavigateToMenuManagement, RoleDetector.canManageMenu(staffRole))
    )

    // Filter items based on the staff's role and permissions
    val accessibleItems = allItems.filter { it.hasPermission }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(accessibleItems) { item ->
            DashboardCard(item = item)
        }
    }
}

@Composable
private fun DashboardCard(item: DashboardItem) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = item.onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = item.icon, contentDescription = item.title, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = item.title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun StaffBottomNavigationBar(onHomeClick: () -> Unit, onProfileClick: () -> Unit, onLogoutClick: () -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onProfileClick,
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Logout, "Log Out") },
            label = { Text("Log Out") },
            selected = false,
            onClick = onLogoutClick
        )
    }
}

private data class DashboardItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val hasPermission: Boolean = true
)

