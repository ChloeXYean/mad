package com.example.rasago.theme.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.rotate
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

    val dateOptions = listOf("Today", "This Week", "This Month", "Last 3 Months", "All Time")
    val statusOptions = listOf("All", "Pending", "Preparing", "Ready", "Done", "Cancelled")

    val filteredOrders = orders.filter { order ->
        val matchesSearch = searchQuery.isEmpty() || order.orderNo.contains(searchQuery, ignoreCase = true)
        val matchesStatus = selectedStatus == "All" || order.foodStatus.equals(selectedStatus, ignoreCase = true)
        
        // Date filtering logic
        val matchesDate = when (selectedDate) {
            "Today" -> {
                val today = java.time.LocalDate.now()
                val orderDate = try {
                    java.time.LocalDate.parse(order.orderTime.substringBefore(" "))
                } catch (e: Exception) {
                    java.time.LocalDate.now()
                }
                orderDate.isEqual(today)
            }
            "This Week" -> {
                val now = java.time.LocalDate.now()
                val weekStart = now.minusDays(now.dayOfWeek.value.toLong() - 1)
                val orderDate = try {
                    java.time.LocalDate.parse(order.orderTime.substringBefore(" "))
                } catch (e: Exception) {
                    java.time.LocalDate.now()
                }
                orderDate.isAfter(weekStart.minusDays(1)) && orderDate.isBefore(now.plusDays(1))
            }
            "This Month" -> {
                val now = java.time.LocalDate.now()
                val orderDate = try {
                    java.time.LocalDate.parse(order.orderTime.substringBefore(" "))
                } catch (e: Exception) {
                    java.time.LocalDate.now()
                }
                orderDate.year == now.year && orderDate.month == now.month
            }
            "Last 3 Months" -> {
                val now = java.time.LocalDate.now()
                val threeMonthsAgo = now.minusMonths(3)
                val orderDate = try {
                    java.time.LocalDate.parse(order.orderTime.substringBefore(" "))
                } catch (e: Exception) {
                    java.time.LocalDate.now()
                }
                orderDate.isAfter(threeMonthsAgo.minusDays(1)) && orderDate.isBefore(now.plusDays(1))
            }
            "All Time" -> true
            else -> true
        }
        
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
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date Filter
                ModernDropdownMenu(
                    selectedValue = selectedDate,
                    options = dateOptions,
                    expanded = dateExpanded,
                    onExpandedChange = { dateExpanded = it },
                    onOptionSelected = { selectedDate = it; dateExpanded = false },
                    icon = Icons.Default.CalendarToday,
                    label = "Time Duration",
                    modifier = Modifier.weight(1f)
                )

                // Status Filter
                ModernDropdownMenu(
                    selectedValue = selectedStatus,
                    options = statusOptions,
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = it },
                    onOptionSelected = { selectedStatus = it; statusExpanded = false },
                    icon = Icons.Default.FilterList,
                    label = "Orders Status",
                    modifier = Modifier.weight(1f)
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernDropdownMenu(
    selectedValue: String,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onOptionSelected: (String) -> Unit,
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF8F9FA)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (expanded) 8.dp else 2.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFF6C757D),
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF6C757D),
                            fontSize = 12.sp
                        )
                        Text(
                            text = selectedValue,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color(0xFF6C757D),
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(if (expanded) 180f else 0f)
                )
            }
        }
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { 
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (option == selectedValue) Color(0xFF1976D2) else Color.Black,
                            fontWeight = if (option == selectedValue) FontWeight.SemiBold else FontWeight.Normal
                        )
                    },
                    onClick = { onOptionSelected(option) },
                    colors = MenuDefaults.itemColors(
                        textColor = if (option == selectedValue) Color(0xFF1976D2) else Color.Black
                    ),
                    modifier = Modifier.background(
                        color = if (option == selectedValue) Color(0xFFE3F2FD) else Color.Transparent
                    )
                )
            }
        }
    }
}

