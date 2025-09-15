package com.example.rasago.theme.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rasago.data.entity.StaffEntity
import com.example.rasago.theme.navigation.StaffBottomNavigationBar
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
    val staffRole = uiState.staff?.role ?: ""
    val currentTime = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())}

    LaunchedEffect(staff) {
        viewModel.setStaff(staff)
    }

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
                            "Staff Menu",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 10.dp),
                            color = Color.Black
                        )

                        Text(
                            text = "Name : ${uiState.staff?.name ?: "Staff"}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )

                        Text(
                            text = "Role: ${uiState.staff?.role?.replaceFirstChar { it.titlecase() } ?: "N/A"}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Status: ",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(2.dp))

                            TextButton(
                                onClick = { viewModel.updateStatus("Working") },
                                shape = RoundedCornerShape(60.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = if (uiState.status.equals("Working", true)) Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                                    contentColor = if (uiState.status.equals("Working", true)) Color.White else Color.Black
                                ),
                                modifier = Modifier
                                    .height(35.dp)
                                    .padding(vertical = 2.dp, horizontal = 10.dp)
                                    .width(90.dp)
                            ) {
                                Text("Working", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            TextButton(
                                onClick = { viewModel.updateStatus("On Break") },
                                shape = RoundedCornerShape(60.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = if (uiState.status.equals("On Break", true)) Color(0xFFf8be0b) else Color(0xFFE0E0E0),
                                    contentColor = if (uiState.status.equals("On Break", true)) Color.White else Color.Black
                                ),
                                modifier = Modifier
                                    .height(35.dp)
                                    .padding(vertical = 2.dp, horizontal = 8.dp)
                                    .width(90.dp)
                            ) {
                                Text("On Break", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Text(
                            text = "Current Time: $currentTime",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }
                },
                modifier = Modifier.height(230.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFeffae9),
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            StaffBottomNavigationBar(
                selectedNavItem = "Main", // This screen is the "Profile" screen for staff
                onNavItemSelect = { selectedItem ->
                    when (selectedItem) {
                        "Main" -> {}
                        "Cart" -> onNavigateToOrders() // Mapped Cart to Orders for staff
                        "Profile" -> onNavigateToProfile()
                    }
                }
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

            Button(
                onClick = {},
                modifier = Modifier
                    .height(60.dp)
                    .width(250.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Menu", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.padding(15.dp))

            if (RoleDetector.hasManagementPermission(staffRole)) {
                Button(
                    onClick = onNavigateToStaffSchedule,
                    modifier = Modifier
                        .width(250.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Staff Schedule", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.padding(15.dp))
            }

            if (RoleDetector.canHandleOrders(staffRole)) {
                Button(
                    onClick = onNavigateToOrders,
                    modifier = Modifier
                        .width(250.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Orders Management", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
