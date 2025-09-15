package com.example.rasago.theme.order

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rasago.data.model.OrderItem
import com.example.rasago.order.OrderHistoryViewModel
import com.example.rasago.theme.navigation.AppTopBar
import com.example.rasago.theme.utils.RoleDetector
import com.example.rasago.ui.theme.Baloo2
import com.example.rasago.ui.theme.DarkGreen
import com.example.rasago.ui.theme.GreenDone
import com.example.rasago.ui.theme.GreenTheme
import com.example.rasago.ui.theme.LightGreen
import com.example.rasago.ui.theme.RedCancel
import com.example.rasago.ui.theme.YellowPrepare
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodStatusScreen(
    historyViewModel: OrderHistoryViewModel,
    role: String, // Role is now a String to be used with RoleDetector
    onBackClick: () -> Unit = {}
) {
    val orderDetails by historyViewModel.selectedOrderDetails.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Order #${orderDetails?.order?.orderNo ?: "Status"}",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        if (orderDetails == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val order = orderDetails!!.order
            val orderItems = orderDetails!!.items
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                OrderTypeSelection(orderType = order.orderType)
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
                            text = "Order No: ${order.orderNo}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Baloo2,
                            color = DarkGreen
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Order Time: ${order.orderTime}",
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
                if (orderItems.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No items in this order",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Baloo2,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn {
                        items(orderItems) { orderItem ->
                            FoodItemRow(
                                item = orderItem,
                                role = role
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodItemRow(item: OrderItem, role: String) {
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
                AsyncImage(
                    model = item.photo,
                    contentDescription = "Food Item",
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
            status = item.status,
            enabled = RoleDetector.canHandleKitchen(role) // Using RoleDetector for permissions
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
    val currentStatus = status.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

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

