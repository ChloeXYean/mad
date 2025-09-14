package com.example.rasago.theme.order

import OrderViewModel
import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rasago.ui.theme.Baloo2
import com.example.rasago.ui.theme.DarkGreen
import com.example.rasago.ui.theme.LightGreen
import com.example.rasago.ui.theme.GreenTheme
import com.example.rasago.ui.theme.YellowPrepare
import com.example.rasago.ui.theme.GreenDone
import com.example.rasago.ui.theme.RedCancel
import java.util.Locale
import androidx.compose.ui.Alignment
import androidx.compose.runtime.collectAsState
import com.example.rasago.theme.navigation.AppTopBar
import com.example.rasago.theme.profile.UserRole
import com.example.rasago.data.model.OrderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun FoodStatusScreen(
    orderViewModel: OrderViewModel,
    role: UserRole = UserRole.CUSTOMER,
    onBackClick: () -> Unit = {}
) {
    val uiState by orderViewModel.uiState.collectAsState()
    val selectedOrder = uiState.selectedOrder

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Food Status",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        if (selectedOrder != null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // --- FIXED UI PART ---
                OrderTypeSelection(orderType = selectedOrder.type)
                Divider(modifier = Modifier.padding(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LightGreen.copy(alpha = 0.3f)),
                    border = BorderStroke(width = 1.dp, color = DarkGreen)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Order No: ${selectedOrder.no}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Baloo2,
                            color = DarkGreen
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Order Time: ${selectedOrder.time}",
                            fontSize = 16.sp,
                            fontFamily = Baloo2,
                            color = DarkGreen
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Food Status: ",
                    fontFamily = Baloo2,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
                // --- END OF FIXED UI PART ---

                // --- SCROLLABLE LIST PART --- for wait for huxley
//                LazyColumn {
//                    items(selectedOrder.items) { orderItem ->
//                        FoodItemRow(
//                            item = orderItem,
//                            status = selectedOrder.status,
//                            role = role
//                        )
//                    }
//                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No order details available.")
            }
        }
    }
}

@Composable
fun FoodItemRow(item: OrderItem, status: String, role: UserRole) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(color = Color(0xFFE9E7E7), shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_menu_gallery),
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "${item.quantity}x ${item.name}",
                fontFamily = Baloo2,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
            )
        }

        StatusDropdown(
            status = status,
            enabled = (role == UserRole.MANAGER)
        )
    }
}

@Composable
fun OrderTypeSelection(orderType: String) {
    val dineIn = stringResource(com.example.rasago.R.string.dine_in)
    val takeAway = stringResource(com.example.rasago.R.string.take_away)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .width(84.dp)
                .height(64.dp)
                .padding(end = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Order \nType:",
                fontFamily = Baloo2,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    color = if (orderType == "Dine-In") GreenTheme else Color.White
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val isSelected = orderType == "Dine-In"
                Icon(
                    painter = painterResource(if (isSelected) com.example.rasago.R.drawable.dine_in_white else com.example.rasago.R.drawable.dine_in_black),
                    contentDescription = dineIn,
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) Color.White else Color.Black
                )
                Text(
                    text = dineIn,
                    fontSize = 16.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else Color.Black,
                    fontFamily = Baloo2
                )
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    color = if (orderType == "Takeaway") GreenTheme else Color.White
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val isSelected = orderType == "Takeaway"
                Icon(
                    painter = painterResource(if (isSelected) com.example.rasago.R.drawable.take_away_white else com.example.rasago.R.drawable.take_away_black),
                    contentDescription = takeAway,
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) Color.White else Color.Black
                )
                Text(
                    text = takeAway,
                    fontSize = 16.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else Color.Black,
                    fontFamily = Baloo2
                )
            }
        }
    }
}

@Composable
fun StatusDropdown(status: String, enabled: Boolean) {
    var expanded by remember { mutableStateOf(false) }
    val currentStatus = status.replaceFirstChar { it.titlecase(Locale.getDefault()) }

    val statusColor = when (currentStatus) {
        "Preparing" -> YellowPrepare
        "Done" -> GreenDone
        "Cancelled" -> RedCancel
        else -> Color.LightGray
    }
    val statusImage = when (currentStatus) {
        "Preparing" -> com.example.rasago.R.drawable.food_prepare
        "Done" -> com.example.rasago.R.drawable.food_ready
        "Cancelled" -> com.example.rasago.R.drawable.food_cancel
        else -> R.drawable.ic_menu_help
    }

    Box(
        modifier = Modifier
            .width(120.dp)
            .background(statusColor, shape = RoundedCornerShape(48.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled) { if (enabled) expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = statusImage),
                contentDescription = currentStatus,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = currentStatus,
                fontSize = 16.sp,
                fontFamily = Baloo2,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
        }

        DropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = { if (enabled) expanded = false },

            modifier = Modifier.background(Color.White)
        ) {
            DropdownMenuItem(
                text = { Text("Preparing") },
                onClick = { /* TODO: Update status in ViewModel */ expanded = false }
            )
            DropdownMenuItem(
                text = { Text("Done") },
                onClick = { /* TODO: Update status in ViewModel */ expanded = false }
            )
            DropdownMenuItem(
                text = { Text("Cancelled") },
                onClick = { /* TODO: Update status in ViewModel */ expanded = false }
            )
        }
    }
}

