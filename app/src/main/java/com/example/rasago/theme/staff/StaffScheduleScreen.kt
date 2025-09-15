package com.example.rasago.theme.staff

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.R
import com.example.rasago.data.entity.StaffEntity
import com.example.rasago.data.model.Order
import com.example.rasago.data.repository.UserRepository
import com.example.rasago.theme.utils.RoleDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// -------------------- Data Models --------------------
data class Staff(
    val name: String,
    val role: StaffRole,
    val status: StaffStatus,
    val jobTime: String
)

enum class StaffRole(val displayName: String) {
    CASHIER("Cashier"),
    KITCHEN("Kitchen"),
    MANAGER("Manager")
}

enum class StaffStatus(val displayName: String, val color: Color) {
    WORKING("Working", Color(0xFF00B14F)),
    ON_BREAK("On Break", Color(0xFFFF9800)),
    OFF_DAY("Off Day", Color(0xFFE53935))
}


// -------------------- MainActivity --------------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavHost(navController = navController)
                    }
                }
            }
        }
    }
}

// -------------------- Navigation --------------------
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "mainMenu") {
        composable("mainMenu") {
            MainMenuScreen()
        }
        composable("staffSchedule") {
            StaffScheduleScreen()
        }
        composable("staffOrders") {
            StaffOrderManagementScreen()
        }
    }
}

// -------------------- Bottom Navigation --------------------
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Main Menu", "mainMenu", Icons.Default.Home),
        BottomNavItem("Staff", "staffSchedule", Icons.Default.People),
        BottomNavItem("Orders", "staffOrders", Icons.Default.Edit)
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentDestination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(val label: String, val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

// -------------------- Main Menu Page --------------------
@Composable
fun MainMenuScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)),
        contentAlignment = Alignment.Center
    ) {
        Text("🍔 Main Menu Page", fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}

// -------------------- Staff Schedule Page --------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffScheduleScreen(
    navController: NavController? = null,
    viewModel: StaffScheduleViewModel = hiltViewModel()
) {
    var selectedDate by remember { mutableStateOf("2025-01-15") }
    var selectedRole by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val staffList by viewModel.staffList.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .padding(16.dp)
    ) {
        // Top bar
        Row(
            verticalAlignment = Alignment.CenterVertically, 
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { navController?.navigateUp() }
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Staff Schedule", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
            

            Row {
                IconButton(
                    onClick = { viewModel.checkDatabaseData() }
                ) {
                    Icon(
                        Icons.Default.People,
                        contentDescription = "Reload",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                // 刷新按钮
                IconButton(
                    onClick = { viewModel.reloadStaffList() }
                ) {
                    Icon(
                        Icons.Default.Edit, // 使用编辑图标作为刷新
                        contentDescription = "Refresh",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Color.LightGray, thickness = 2.dp, modifier = Modifier.padding(vertical = 10.dp))
        // 🔍 Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by name or role...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(12.dp))

        // 📅 日期 & Role 筛选
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 日期选择器
            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Select Date",
                            fontSize = 12.sp,
                            color = Color(0xFF6C757D)
                        )
                        Text(
                            text = selectedDate,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = "Select Date",
                        tint = Color(0xFF6C757D),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // 角色筛选器
            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Filter",
                                fontSize = 12.sp,
                                color = Color(0xFF6C757D)
                            )
                            Text(
                                text = when (selectedRole) {
                                    null -> "All"
                                    "active" -> "Active"
                                    "inactive" -> "Inactive"
                                    else -> RoleDetector.getRoleDisplayName(selectedRole ?: "")
                                },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }
                        Icon(
                            Icons.Default.People,
                            contentDescription = "Filter Role",
                            tint = Color(0xFF6C757D),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("All") },
                            onClick = {
                                selectedRole = null
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Active") },
                            onClick = {
                                selectedRole = "active"
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Inactive") },
                            onClick = {
                                selectedRole = "inactive"
                                expanded = false
                            }
                        )
                        listOf("cashier", "kitchen", "manager").forEach { role ->
                            DropdownMenuItem(
                                text = { Text(RoleDetector.getRoleDisplayName(role)) },
                                onClick = {
                                    selectedRole = role
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Text(
                    text = uiState.error ?: "",
                    color = Color(0xFFD32F2F),
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else if (uiState.message != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8))
            ) {
                Text(
                    text = uiState.message ?: "",
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Text(
            text = "Total staff: ${staffList.size}",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // 🔹 Staff List 可滚
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val filteredList = staffList.filter { staffEntity: StaffEntity ->
                val matchesRole = when (selectedRole) {
                    null -> true // All Roles
                    "active" -> staffEntity.status == "active"
                    "inactive" -> staffEntity.status == "inactive"
                    else -> staffEntity.role == (selectedRole ?: "")
                }
                val matchesSearch = searchQuery.isBlank() ||
                        staffEntity.name.contains(searchQuery, ignoreCase = true) ||
                        RoleDetector.getRoleDisplayName(staffEntity.role).contains(searchQuery, ignoreCase = true)
                
                matchesRole && matchesSearch
            }

            if (filteredList.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Text(
                            text = if (staffList.isEmpty()) "No staff found in database" else "No staff match the current filter",
                            modifier = Modifier.padding(16.dp),
                            color = Color.Gray
                        )
                    }
                }
            } else {
                items(filteredList) { staffEntity: StaffEntity ->
                    RealStaffItem(
                        staff = staffEntity,
                        onStatusChange = { newStatus ->
                            viewModel.updateStaffWorkStatus(staffEntity.email, newStatus)
                        },
                        onTimeChange = { startTime, endTime ->
                            viewModel.updateStaffWorkTime(staffEntity.email, startTime, endTime)
                        }
                    )
                }
            }
        }
        
        if (showDatePicker) {
            DatePickerDialog(
                selectedDate = selectedDate,
                onDateSelected = { newDate ->
                    selectedDate = newDate
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

// -------------------- Real Staff Item --------------------
@Composable
fun RealStaffItem(
    staff: StaffEntity,
    onStatusChange: (String) -> Unit,
    onTimeChange: (String, String) -> Unit
) {
    var statusMenuExpanded by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf("09:00") }
    var endTime by remember { mutableStateOf("18:00") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Name, Role, Status
            Column {
                Text(staff.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    RoleDetector.getRoleDisplayName(staff.role),
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(8.dp))

                // Current status + Edit button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val statusColor = if (staff.status == "active") Color(0xFF4CAF50) else Color(0xFF9E9E9E)
                    val statusText = if (staff.status == "active") "Active" else "Inactive"

                    Box(
                        modifier = Modifier
                            .background(
                                color = statusColor.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = statusText,
                            color = statusColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = { statusMenuExpanded = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.People,
                            contentDescription = "Edit Status",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = statusMenuExpanded,
                        onDismissRequest = { statusMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Active") },
                            onClick = {
                                onStatusChange("active")
                                statusMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Inactive") },
                            onClick = {
                                onStatusChange("inactive")
                                statusMenuExpanded = false
                            }
                        )
                    }
                }
            }

            // Right side: Email, Phone and Work Time
            Column(horizontalAlignment = Alignment.End) {
                Text("Email", fontSize = 12.sp, color = Color.Gray)
                Text(staff.email, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Phone", fontSize = 12.sp, color = Color.Gray)
                Text(staff.phone ?: "N/A", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                
                // 工作时间编辑
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Job Time", fontSize = 12.sp, color = Color.Gray)
                    IconButton(
                        onClick = { showTimeDialog = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.AccessTime,
                            contentDescription = "Edit Work Time",
                            tint = Color(0xFF1976D2),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
    
    // 时间编辑对话框
    if (showTimeDialog) {
        TimeEditDialog(
            startTime = startTime,
            endTime = endTime,
            onStartTimeChange = { startTime = it },
            onEndTimeChange = { endTime = it },
            onDismiss = { showTimeDialog = false },
            onConfirm = {
                onTimeChange(startTime, endTime)
                showTimeDialog = false
            }
        )
    }
}

// -------------------- Staff Item (Legacy) --------------------
@Composable
fun StaffItem(
    staff: Staff,
    onStatusChange: (StaffStatus) -> Unit,
    onTimeChange: (String) -> Unit
) {
    var statusMenuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔹 左边：姓名、职位、状态
            Column {
                Text(staff.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(staff.role.displayName, fontSize = 14.sp, color = Color.Gray)

                Spacer(Modifier.height(8.dp))

                // 当前状态 + 编辑按钮
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 状态 Button 样式
                    Box(
                        modifier = Modifier
                            .background(
                                color = staff.status.color.copy(alpha = 0.15f), // 背景淡色
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = staff.status.displayName,
                            color = staff.status.color,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = { statusMenuExpanded = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.edit_icon),
                            contentDescription = "Edit Status",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = statusMenuExpanded,
                        onDismissRequest = { statusMenuExpanded = false }
                    ) {
                        StaffStatus.values().forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.displayName) },
                                onClick = {
                                    onStatusChange(status)
                                    statusMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // 🔹 右边：时间段输入
            var expanded by remember { mutableStateOf(false) }
            var startTime by remember { mutableStateOf("09:00") }
            var endTime by remember { mutableStateOf("17:00") }

            Column(horizontalAlignment = Alignment.End) {
                Text("Work Hours", fontSize = 12.sp, color = Color.Gray)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { startTime = it; onTimeChange("$startTime - $endTime") },
                        modifier = Modifier.width(70.dp),
                        textStyle = TextStyle(fontSize = 14.sp),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Text("-", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { endTime = it; onTimeChange("$startTime - $endTime") },
                        modifier = Modifier.width(70.dp),
                        textStyle = TextStyle(fontSize = 14.sp),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
        }
    }
}

// -------------------- Preview --------------------

// -------------------- Time Edit Dialog --------------------
@Composable
fun TimeEditDialog(
    startTime: String,
    endTime: String,
    onStartTimeChange: (String) -> Unit,
    onEndTimeChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "编辑工作时间",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "设置员工的工作时间",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "开始时间",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        OutlinedTextField(
                            value = startTime,
                            onValueChange = onStartTimeChange,
                            placeholder = { Text("09:00") },
                            modifier = Modifier.width(120.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                    
                    Text(
                        text = "至",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Column {
                        Text(
                            text = "结束时间",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        OutlinedTextField(
                            value = endTime,
                            onValueChange = onEndTimeChange,
                            placeholder = { Text("17:00") },
                            modifier = Modifier.width(120.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF1976D2)
                )
            ) {
                Text("确认", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Gray
                )
            ) {
                Text("取消")
            }
        }
    )
}

// -------------------- Date Picker Dialog --------------------
@Composable
fun DatePickerDialog(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var tempDate by remember { mutableStateOf(selectedDate) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "选择日期",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "选择要查看的排班日期",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                OutlinedTextField(
                    value = tempDate,
                    onValueChange = { tempDate = it },
                    label = { Text("日期 (YYYY-MM-DD)") },
                    placeholder = { Text("2025-01-15") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                
                Text(
                    text = "示例格式: 2025-01-15",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onDateSelected(tempDate) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF1976D2)
                )
            ) {
                Text("确认", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Gray
                )
            ) {
                Text("取消")
            }
        }
    )
}

// -------------------- Staff Order Management Screen --------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffOrderManagementScreen(
    navController: NavController? = null,
    viewModel: StaffOrderViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }
    var statusExpanded by remember { mutableStateOf(false) }
    
    val orders by viewModel.allOrders.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    
    val statusOptions = listOf("All", "Preparing", "Ready", "Done", "Cancelled")
    
    val filteredOrders = orders.filter { order: Order ->
        val matchesSearch = searchQuery.isEmpty() || order.orderNo.contains(searchQuery, ignoreCase = true)
        val matchesStatus = selectedStatus == "All" || order.foodStatus.equals(selectedStatus, ignoreCase = true)
        matchesSearch && matchesStatus
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .padding(16.dp)
    ) {
        // Top bar
        Row(
            verticalAlignment = Alignment.CenterVertically, 
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { navController?.navigateUp() }
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Order Management", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
            
            // 刷新按钮
            IconButton(
                onClick = { viewModel.loadOrders() }
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Refresh",
                    tint = Color(0xFF1976D2),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Color.LightGray, thickness = 2.dp, modifier = Modifier.padding(vertical = 10.dp))

        // 搜索栏
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by order number...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(12.dp))

        // 状态筛选器
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = it }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Status: $selectedStatus",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = "Filter Status",
                        tint = Color(0xFF6C757D),
                        modifier = Modifier.size(20.dp)
                    )
                }
                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    statusOptions.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status) },
                            onClick = {
                                selectedStatus = status
                                statusExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // 显示加载状态和错误信息
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Text(
                    text = uiState.error ?: "",
                    color = Color(0xFFD32F2F),
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else if (uiState.message != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8))
            ) {
                Text(
                    text = uiState.message ?: "",
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // 显示订单数量信息
        Text(
            text = "Total orders: ${orders.size}",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // 订单列表
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (filteredOrders.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Text(
                            text = if (orders.isEmpty()) "No orders found" else "No orders match the current filter",
                            modifier = Modifier.padding(16.dp),
                            color = Color.Gray
                        )
                    }
                }
            } else {
                items(filteredOrders) { order: Order ->
                    StaffOrderItem(
                        order = order,
                        onStatusUpdate = { newStatus ->
                            viewModel.updateOrderStatus(order.orderId, newStatus)
                        }
                    )
                }
            }
        }
    }
}

// -------------------- Staff Order Item --------------------
@Composable
fun StaffOrderItem(
    order: Order,
    onStatusUpdate: (String) -> Unit
) {
    var statusMenuExpanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 订单头部信息
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Order #${order.orderNo}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = order.orderTime,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                
                // 状态标签和更新按钮
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val statusColor = when (order.foodStatus.lowercase()) {
                        "preparing" -> Color(0xFFFF9800)
                        "ready" -> Color(0xFF4CAF50)
                        "done" -> Color(0xFF2196F3)
                        "cancelled" -> Color(0xFFF44336)
                        else -> Color(0xFF9E9E9E)
                    }
                    
                    Box(
                        modifier = Modifier
                            .background(
                                color = statusColor.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = order.foodStatus,
                            color = statusColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    IconButton(
                        onClick = { statusMenuExpanded = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Update Status",
                            tint = Color(0xFF1976D2),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    DropdownMenu(
                        expanded = statusMenuExpanded,
                        onDismissRequest = { statusMenuExpanded = false }
                    ) {
                        listOf("Preparing", "Ready", "Done", "Cancelled").forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status) },
                                onClick = {
                                    onStatusUpdate(status)
                                    statusMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 订单详情
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Type", fontSize = 12.sp, color = Color.Gray)
                    Text(order.orderType, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                Column {
                    Text("Payment", fontSize = 12.sp, color = Color.Gray)
                    Text(order.paymentMethod, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                Column {
                    Text("Total", fontSize = 12.sp, color = Color.Gray)
                    Text("RM ${String.format("%.2f", order.totalPayment)}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStaffSchedule() {
    StaffScheduleScreen()
}