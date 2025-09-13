package com.example.rasago

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 员工数据类
data class Staff(
    val name: String,
    val role: String,
    val jobTime: String,
    val lastLogin: String,
    val status: StaffStatus
)

// 员工状态枚举
enum class StaffStatus {
    WORKING, ON_BREAK
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffProfileScreen(
    staff: Staff,
    onMenuClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var staffStatus by remember { mutableStateOf(staff.status) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            "Staff Profile",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 10.dp),
                            color = Color.Black
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Name : ${staff.name}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                            Text(
                                text = "Job Time: ${staff.jobTime}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(end = 20.dp),
                                color = Color.Black
                            )
                        }

                        Text(
                            text = "Role: ${staff.role}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Status: ",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(2.dp))

                            TextButton(
                                onClick = { staffStatus = StaffStatus.WORKING },
                                shape = RoundedCornerShape(60.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = if (staffStatus == StaffStatus.WORKING)
                                        Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                                    contentColor = if (staffStatus == StaffStatus.WORKING)
                                        Color.White else Color.Black
                                ),
                                modifier = Modifier
                                    .height(35.dp)
                                    .padding(vertical = 2.dp)
                                    .padding(horizontal = 10.dp)
                                    .width(90.dp)
                            ) {
                                Text(
                                    "Working",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }

                            TextButton(
                                onClick = { staffStatus = StaffStatus.ON_BREAK },
                                shape = RoundedCornerShape(60.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = if (staffStatus == StaffStatus.ON_BREAK)
                                        Color(0xFFf8be0b) else Color(0xFFE0E0E0),
                                    contentColor = if (staffStatus == StaffStatus.ON_BREAK)
                                        Color.White else Color.Black
                                ),
                                modifier = Modifier
                                    .height(35.dp)
                                    .padding(vertical = 2.dp)
                                    .padding(horizontal = 8.dp)
                                    .width(90.dp)
                            ) {
                                Text(
                                    "On Break",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }

                        Text(
                            text = "Last Login: ${staff.lastLogin}",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }
                },
                modifier = Modifier.height(220.dp),
                colors = TopAppBarColors(
                    containerColor = Color(0xFFeffae9),
                    scrolledContainerColor = Color(0xFF4CAF50),
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            StaffBottomNavigationBar(
                selectedItem = "Staff",
                onMenuClick = onMenuClick,
                onLogoutClick = onLogoutClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 中间按钮保持不变
            Button(
                onClick = { /* 功能暂不实现 */ },
                modifier = Modifier
                    .height(60.dp)
                    .width(250.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Orders Management", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.padding(15.dp))

            Button(
                onClick = { /* 功能暂不实现 */ },
                modifier = Modifier
                    .width(250.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Orders History", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.padding(15.dp))

            Button(
                onClick = { /* 功能暂不实现 */ },
                modifier = Modifier
                    .width(250.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Staff Schedule", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// 员工底部导航栏（保持不变）
@Composable
fun StaffBottomNavigationBar(
    selectedItem: String,
    onMenuClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, "Menu") },
            label = { Text("Menu") },
            selected = selectedItem == "Menu",
            onClick = onMenuClick
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, "Staff") },
            label = { Text("Staff") },
            selected = selectedItem == "Staff",
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Logout, "Log Out") },
            label = { Text("Log Out") },
            selected = selectedItem == "Log Out",
            onClick = onLogoutClick
        )
    }
}

@Preview
@Composable
fun StaffProfilePreview() {
//    AssignmentTestTheme {
//        StaffProfileScreen(
//            staff = Staff("Ali", "Cashier", "8am - 8pm", "Sept 13, 2025", StaffStatus.WORKING),
//            onMenuClick = {},
//            onLogoutClick = {}
//        )
//    }
}