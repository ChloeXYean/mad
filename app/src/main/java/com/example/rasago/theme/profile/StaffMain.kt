package com.example.rasago.theme.staff

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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffMainScreen(
    staff: StaffEntity?,
    onNavigateToProfile: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToStaffSchedule: () -> Unit,
    onNavigateToMenuManagement: () -> Unit,
    onLogout: () -> Unit,
    viewModel: StaffMainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
                            color = Color.Black
                        )
                        staff?.let {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Name : ${it.name}", fontSize = 18.sp)
                                Text("Job Time: ${SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(it.jobTime))}", fontSize = 14.sp, modifier = Modifier.padding(end = 20.dp))
                            }
                            Text("Role: ${it.role.replaceFirstChar { char -> char.titlecase(Locale.getDefault()) }}", fontSize = 18.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Status: ", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(2.dp))
                                StatusButton(
                                    text = "Working",
                                    isSelected = uiState.status.equals("active", true) || uiState.status.equals("Working", true),
                                    onClick = { viewModel.updateStatus("Working") },
                                    activeColor = Color(0xFF4CAF50)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                StatusButton(
                                    text = "On Break",
                                    isSelected = uiState.status == "On Break",
                                    onClick = { viewModel.updateStatus("On Break") },
                                    activeColor = Color(0xFFf8be0b)
                                )
                            }
                            Text("Last Login: ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())}", fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                },
                modifier = Modifier.height(220.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFeffae9)
                )
            )
        },
        bottomBar = {
            StaffBottomNavigationBar(
                onProfileClick = onNavigateToProfile,
                onLogoutClick = onLogout
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
            ActionButton(text = "Staff Profile", onClick = onNavigateToProfile)
            Spacer(modifier = Modifier.padding(15.dp))
            ActionButton(text = "Orders History", onClick = onNavigateToOrders)
            Spacer(modifier = Modifier.padding(15.dp))
            ActionButton(text = "Staff Schedule", onClick = onNavigateToStaffSchedule)
            Spacer(modifier = Modifier.padding(15.dp))
            ActionButton(text = "Orders Management", onClick = onNavigateToMenuManagement)
        }
    }
}

@Composable
private fun StatusButton(text: String, isSelected: Boolean, onClick: () -> Unit, activeColor: Color) {
    TextButton(
        onClick = onClick,
        shape = RoundedCornerShape(60.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = if (isSelected) activeColor else Color(0xFFE0E0E0),
            contentColor = if (isSelected) Color.White else Color.Black
        ),
        modifier = Modifier
            .height(35.dp)
            .padding(vertical = 2.dp)
    ) {
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(60.dp)
            .width(250.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text, fontSize = 18.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun StaffBottomNavigationBar(onProfileClick: () -> Unit, onLogoutClick: () -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = { /* Already on the main screen */ },
            icon = { Icon(Icons.Default.Home, "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onProfileClick,
            icon = { Icon(Icons.Default.Person, "Profile") },
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

