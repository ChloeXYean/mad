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
import com.example.rasago.R

// -------------------- Data Models --------------------
data class Staff(
    val name: String,
    val role: StaffRole,
    val status: StaffStatus,
    val jobTime: String
)

enum class StaffRole(val displayName: String) {
    //("Waiter"),
    CASHIER("Cashier"),
    CHEF("Chef")
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
    }
}

// -------------------- Bottom Navigation --------------------
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Main Menu", "mainMenu", Icons.Default.Home),
        BottomNavItem("Staff", "staffSchedule", Icons.Default.People)
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
fun StaffScheduleScreen(navController: NavController? = null) {
    var selectedDate by remember { mutableStateOf("2025-09-13") }
    var selectedRole by remember { mutableStateOf<StaffRole?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    var staffList by remember {
        mutableStateOf(
            listOf(
                Staff("Ali", StaffRole.CASHIER, StaffStatus.WORKING, "9:00 - 17:00"),
                Staff("Siti", StaffRole.CASHIER, StaffStatus.ON_BREAK, "10:00 - 18:00"),
                Staff("John", StaffRole.CHEF, StaffStatus.OFF_DAY, "")
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .padding(16.dp)
    ) {
        // Top bar
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = selectedDate, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                IconButton(onClick = { /* TODO DatePicker */ }) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                }
            }

            // ✅ 美化后的 Role Dropdown
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedRole?.displayName ?: "All Roles",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("All Roles") },
                        onClick = {
                            selectedRole = null
                            expanded = false
                        }
                    )
                    StaffRole.values().forEach { role ->
                        DropdownMenuItem(
                            text = { Text(role.displayName) },
                            onClick = {
                                selectedRole = role
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // 🔹 Staff List 可滚
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val filteredList = staffList.filter {
                (selectedRole == null || it.role == selectedRole) &&
                        (searchQuery.isBlank() ||
                                it.name.contains(searchQuery, ignoreCase = true) ||
                                it.role.displayName.contains(searchQuery, ignoreCase = true))
            }

            items(filteredList) { staff ->
                StaffItem(
                    staff = staff,
                    onStatusChange = { newStatus ->
                        staffList = staffList.toMutableList().also {
                            val index = it.indexOf(staff)
                            it[index] = it[index].copy(status = newStatus)
                        }
                    },
                    onTimeChange = { newTime ->
                        staffList = staffList.toMutableList().also {
                            val index = it.indexOf(staff)
                            it[index] = it[index].copy(jobTime = newTime)
                        }
                    }
                )
            }
        }
    }
}

// -------------------- Staff Item --------------------
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

@Preview(showBackground = true)
@Composable
fun PreviewStaffSchedule() {
    StaffScheduleScreen()
}