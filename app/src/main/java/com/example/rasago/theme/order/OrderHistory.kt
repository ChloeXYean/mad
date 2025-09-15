package com.example.rasago.theme.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rasago.data.model.Order
import com.example.rasago.order.OrderHistoryViewModel
import com.example.rasago.theme.navigation.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    historyViewModel: OrderHistoryViewModel = hiltViewModel(),
    onViewOrder: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val orders by historyViewModel.allOrders.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("All Time") }
    var selectedStatus by remember { mutableStateOf("All") }
    var dateExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    val dateOptions = listOf("Today", "This Week", "This Month", "All Time")
    val statusOptions = listOf("All", "Preparing", "Done", "Cancelled")

    val filteredOrders = orders.filter { order ->
        val matchesSearch = searchQuery.isEmpty() || order.orderNo.contains(searchQuery, ignoreCase = true)
        val matchesStatus = selectedStatus == "All" || order.foodStatus.equals(selectedStatus, ignoreCase = true)
        // Date filtering logic would need to be more complex, this is a basic implementation
        val matchesDate = selectedDate == "All Time" // Simplified for now
        matchesSearch && matchesStatus && matchesDate
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Order History",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by Order ID", style = MaterialTheme.typography.bodyMedium) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            // Filter Dropdowns
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date Filter
                ExposedDropdownMenuBox(
                    expanded = dateExpanded,
                    onExpandedChange = { dateExpanded = !it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Date", style = MaterialTheme.typography.bodySmall) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dateExpanded) },
                        modifier = Modifier.menuAnchor(),
                        shape = RoundedCornerShape(15.dp)
                    )
                    ExposedDropdownMenu(expanded = dateExpanded, onDismissRequest = { dateExpanded = false }) {
                        dateOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedDate = option
                                    dateExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                // Status Filter
                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = !it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedStatus,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status", style = MaterialTheme.typography.bodySmall) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                        modifier = Modifier.menuAnchor(),
                        shape = RoundedCornerShape(15.dp)
                    )
                    ExposedDropdownMenu(expanded = statusExpanded, onDismissRequest = { statusExpanded = false }) {
                        statusOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedStatus = option
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Order List or Empty State
            if (filteredOrders.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("No orders yet", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Your order history will appear here",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(filteredOrders) { order ->
                        OrderHistoryItem(
                            order = order,
                            onViewClick = { onViewOrder(order.orderId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderHistoryItem(order: Order, onViewClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = onViewClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)) // Light green background
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Order: #${order.orderNo}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order.orderTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .background(
                        color = when (order.orderType) {
                            "Dine-In" -> Color(0xFF81C784)
                            "Takeaway" -> Color(0xFFFFD54F)
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = order.orderType,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Status: ${order.foodStatus}",
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(
                onClick = onViewClick,
                modifier = Modifier.padding(top = 4.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
            ) {
                Text("View Status", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

