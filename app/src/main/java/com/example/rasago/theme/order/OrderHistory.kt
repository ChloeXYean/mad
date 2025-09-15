package com.example.rasago.theme.order

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rasago.data.model.Order
import com.example.rasago.order.OrderHistoryViewModel
import com.example.rasago.theme.navigation.AppTopBar
import java.time.LocalDate

private val backgroundColor = Color(0xFFF0F0F0)
private val cardColor = Color.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    historyViewModel: OrderHistoryViewModel = hiltViewModel(),
    onViewOrder: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val orders by historyViewModel.allOrders.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("All") }
    var selectedStatus by remember { mutableStateOf("All") }
    var dateExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    val dateOptions = listOf("All", "Today", "This Week", "This Month")
    val statusOptions = listOf("All", "Preparing", "Done", "Cancelled")

    val filteredOrders = orders.filter { order ->
        val matchesSearch = searchQuery.isEmpty() || order.orderNo.contains(searchQuery, ignoreCase = true)
        val matchesStatus = selectedStatus == "All" || order.foodStatus.equals(selectedStatus, ignoreCase = true)
        
        // Date filtering logic
        val matchesDate = when (selectedDate) {
            "All" -> true
            "Today" -> {
                val today = java.time.LocalDate.now()
                val orderDate = try {
                    java.time.LocalDate.parse(order.orderTime.substring(0, 10))
                } catch (e: Exception) {
                    null
                }
                orderDate == today
            }
            "This Week" -> {
                val now = java.time.LocalDate.now()
                val weekStart = now.minusDays(now.dayOfWeek.value.toLong() - 1)
                val orderDate = try {
                    java.time.LocalDate.parse(order.orderTime.substring(0, 10))
                } catch (e: Exception) {
                    null
                }
                orderDate?.isAfter(weekStart.minusDays(1)) == true && orderDate?.isBefore(now.plusDays(1)) == true
            }
            "This Month" -> {
                val now = java.time.LocalDate.now()
                val monthStart = now.withDayOfMonth(1)
                val orderDate = try {
                    java.time.LocalDate.parse(order.orderTime.substring(0, 10))
                } catch (e: Exception) {
                    null
                }
                orderDate?.isAfter(monthStart.minusDays(1)) == true && orderDate?.isBefore(now.plusDays(1)) == true
            }
            else -> true
        }
        
        matchesSearch && matchesStatus && matchesDate
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Order History", 
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
            // Search and Filter Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search by Order ID") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF236cb2),
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color(0xFF236cb2),
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    // Filter Dropdowns
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Date Filter
                        ExposedDropdownMenuBox(
                            expanded = dateExpanded,
                            onExpandedChange = { 
                                dateExpanded = !it
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedDate,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Date") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Date"
                                    )
                                },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dateExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .clickable { dateExpanded = !dateExpanded },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF236cb2),
                                    unfocusedBorderColor = Color.Gray,
                                    focusedLabelColor = Color(0xFF236cb2),
                                    unfocusedLabelColor = Color.Gray
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = dateExpanded, 
                                onDismissRequest = { dateExpanded = false }
                            ) {
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
                        Spacer(modifier = Modifier.width(12.dp))
                        // Status Filter
                        ExposedDropdownMenuBox(
                            expanded = statusExpanded,
                            onExpandedChange = { 
                                statusExpanded = !it
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedStatus,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Status") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = "Status"
                                    )
                                },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .clickable { statusExpanded = !statusExpanded },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF236cb2),
                                    unfocusedBorderColor = Color.Gray,
                                    focusedLabelColor = Color(0xFF236cb2),
                                    unfocusedLabelColor = Color.Gray
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = statusExpanded, 
                                onDismissRequest = { statusExpanded = false }
                            ) {
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
                }
            }

            // Order List or Empty State
            if (filteredOrders.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = "No Orders",
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No orders yet", 
                            fontSize = 20.sp, 
                            fontWeight = FontWeight.Bold, 
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Your order history will appear here",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
            .fillMaxWidth()
            .clickable(onClick = onViewClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order #${order.orderNo}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order.orderTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Order Type and Status Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Order Type Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            color = when (order.orderType) {
                                "Dine-In" -> Color(0xFF4CAF50)
                                "Takeaway" -> Color(0xFFFF9800)
                                else -> Color.Gray
                            }
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (order.orderType == "Dine-In") Icons.Default.Restaurant else Icons.Default.ShoppingBag,
                            contentDescription = order.orderType,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Text(
                            text = order.orderType,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                
                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            color = when (order.foodStatus.lowercase()) {
                                "preparing" -> Color(0xFFFF9800)
                                "done" -> Color(0xFF4CAF50)
                                "cancelled" -> Color(0xFFF44336)
                                else -> Color.Gray
                            }
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = when (order.foodStatus.lowercase()) {
                                "preparing" -> Icons.Default.AccessTime
                                "done" -> Icons.Default.CheckCircle
                                "cancelled" -> Icons.Default.Cancel
                                else -> Icons.Default.Info
                            },
                            contentDescription = order.foodStatus,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Text(
                            text = order.foodStatus,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action Button
            Button(
                onClick = onViewClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF236cb2),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "View Details",
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "View Order Details",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

