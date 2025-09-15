package com.example.rasago.theme.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rasago.theme.navigation.AppTopBar

// Enum to represent staff status, directly included for UI purposes.
enum class StaffStatus {
    WORKING, ON_BREAK
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffProfileScreen(
    onBackClick: () -> Unit,
    onManageMenuClicked: () -> Unit,
    onLogout: () -> Unit,
    // Placeholder data that would be provided by a ViewModel
    staffName: String = "Ali",
    staffRole: String = "Cashier",
    staffJobTime: String = "9am - 5pm",
    staffLastLogin: String = "September 15, 2025"
) {
    var staffStatus by remember { mutableStateOf(StaffStatus.WORKING) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Staff Profile",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header Card with Staff Details
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFFBE9))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Name: $staffName", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        Text("Job Time: $staffJobTime", fontSize = 14.sp)
                    }
                    Text("Role: $staffRole", fontSize = 18.sp)

                    // Status Buttons
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Status: ", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { staffStatus = StaffStatus.WORKING },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (staffStatus == StaffStatus.WORKING) Color(0xFF4CAF50) else Color.LightGray,
                                contentColor = if (staffStatus == StaffStatus.WORKING) Color.White else Color.Black
                            )
                        ) {
                            Text("Working", fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { staffStatus = StaffStatus.ON_BREAK },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (staffStatus == StaffStatus.ON_BREAK) Color(0xFFF8BE0B) else Color.LightGray,
                                contentColor = if (staffStatus == StaffStatus.ON_BREAK) Color.White else Color.Black
                            )
                        ) {
                            Text("On Break", fontSize = 12.sp)
                        }
                    }
                    Text("Last Login: $staffLastLogin", fontSize = 14.sp, color = Color.Gray)
                }
            }

            // Main Action Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onManageMenuClicked,
                    modifier = Modifier
                        .height(60.dp)
                        .width(250.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Manage Menu", fontSize = 18.sp)
                }

                Button(
                    onClick = { /* TODO: Navigate to Order History for Staff */ },
                    modifier = Modifier
                        .width(250.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Orders History", fontSize = 18.sp)
                }

                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .width(250.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Log Out", fontSize = 18.sp)
                }
            }
        }
    }
}
