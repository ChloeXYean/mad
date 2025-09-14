//package com.example.rasago.theme.order
//
//import android.annotation.SuppressLint
//import android.app.DatePickerDialog
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material.icons.filled.DateRange
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.example.rasago.theme.navigation.AppTopBar
//import com.example.rasago.DummyData
//import com.example.rasago.theme.profile.UserRole
//import com.example.rasago.data.model.Order
//import com.example.rasago.order.OrderViewModel
//import com.example.rasago.ui.theme.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//
//@Composable
////if (role == STAFF || role == MANAGER || role == CASHIER || role == KITCHEN)
//fun OrderManagementScreen(
//    orderViewModel: OrderViewModel,
//    role: UserRole = UserRole.CASHIER, //TODO: Need to check if cashier/staff cuz multiple staff = staff
//    onViewStatusClick: (Order) -> Unit = {},
//    onBackClick: () -> Unit = {}
//) {
//    val uiState by orderViewModel.uiState.collectAsState()
//    OrderManagementContent(
//        orders = uiState.orders,
//        role = role,
//        onViewStatusClick = { order ->
//            orderViewModel.selectOrder(order)
//            onViewStatusClick(order)
//        },
//        onBackClick = onBackClick,
//        onStatusChange = { orderNo, newStatus ->
//            orderViewModel.updateOrderStatus(orderNo, newStatus)
//        }
//    )
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun OrderManagementContent(
//    orders: List<Order>,
//    role: UserRole,
//    onViewStatusClick: (Order) -> Unit,
//    onBackClick: () -> Unit,
//    onStatusChange: (orderNo: String, newStatus: String) -> Unit
//) {
//    val context = LocalContext.current
//    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
//    var selectedStatus by remember { mutableStateOf("All") }
//    var searchQuery by remember { mutableStateOf("") }
//    var showStatusDropdown by remember { mutableStateOf(false) }
//
//    val filteredOrders = orders.filter { order ->
//        val matchesDate by lazy {
//            val orderDateStr = order.time.split(" ")[0]
//            val selectedDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
//            orderDateStr == selectedDateStr
//        }
//        val matchesStatus = selectedStatus == "All" || order.status.equals(selectedStatus, ignoreCase = true)
//        val matchesSearch = searchQuery.isEmpty() ||
//                order.no.contains(searchQuery, ignoreCase = true) ||
//                order.type.contains(searchQuery, ignoreCase = true)
//        matchesDate && matchesStatus && matchesSearch
//    }
//
//    Scaffold(
//        topBar = {
//            AppTopBar(
//                title = "Order Management",
//                onBackClick = onBackClick
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .padding(innerPadding)
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            TextField(
//                value = searchQuery,
//                onValueChange = { searchQuery = it },
//                label = { Text("Search Orders") },
//                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
//                colors = TextFieldDefaults.colors(
//                    focusedContainerColor = LightGreen,
//                    unfocusedContainerColor = LightGreen,
//                    focusedIndicatorColor = DarkGreen,
//                    unfocusedIndicatorColor = GreyGreen,
//                    focusedLabelColor = DarkGreen,
//                    unfocusedLabelColor = DarkGreen
//                ),
//                shape = RoundedCornerShape(12.dp),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                OutlinedButton(
//                    onClick = {
//                        DatePickerDialog(
//                            context,
//                            { _, year, month, day -> selectedDate.set(year, month, day) },
//                            selectedDate.get(Calendar.YEAR),
//                            selectedDate.get(Calendar.MONTH),
//                            selectedDate.get(Calendar.DAY_OF_MONTH)
//                        ).show()
//                    },
//                    modifier = Modifier.weight(1f),
//                    colors = ButtonDefaults.outlinedButtonColors(containerColor = LightGreen, contentColor = DarkGreen),
//                    shape = RoundedCornerShape(12.dp),
//                    border = BorderStroke(1.dp, GreyGreen.copy(alpha = 0.6f))
//                ) {
//                    Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
//                    Spacer(modifier = Modifier.width(6.dp))
//                    Text("Pick Date")
//                }
//
//                Box(modifier = Modifier.weight(1f)) {
//                    OutlinedButton(
//                        onClick = { showStatusDropdown = true },
//                        modifier = Modifier.fillMaxWidth(),
//                        colors = ButtonDefaults.outlinedButtonColors(containerColor = LightGreen, contentColor = DarkGreen),
//                        shape = RoundedCornerShape(12.dp),
//                        border = BorderStroke(1.dp, GreyGreen.copy(alpha = 0.6f))
//                    ) {
//                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//                            Text(text = selectedStatus, textAlign = TextAlign.Center, modifier = Modifier.offset((-4).dp))
//                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.align(Alignment.CenterEnd))
//                        }
//                    }
//
//                    DropdownMenu(expanded = showStatusDropdown, onDismissRequest = { showStatusDropdown = false }) {
//                        DropdownMenuItem(
//                            text = { Text("All", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
//                            onClick = { selectedStatus = "All"; showStatusDropdown = false })
//                        DropdownMenuItem(
//                            text = { Text("Preparing", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
//                            onClick = { selectedStatus = "Preparing"; showStatusDropdown = false })
//                        DropdownMenuItem(
//                            text = { Text("Done", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
//                            onClick = { selectedStatus = "Done"; showStatusDropdown = false })
//                        DropdownMenuItem(
//                            text = { Text("Cancelled", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
//                            onClick = { selectedStatus = "Cancelled"; showStatusDropdown = false })
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//            LazyColumn {
//                items(filteredOrders) { order ->
//                    OrderCard(
//                        order = order,
//                        isKitchenView = (role == UserRole.KITCHEN),
//                        isManagerOrCashier = (role == UserRole.MANAGER || role == UserRole.CASHIER),
//                        onStatusChange = { newStatus -> onStatusChange(order.no, newStatus) },
//                        onCardClick = { onViewStatusClick(order) }
//                    )
//                }
//            }
//        }
//    }
//}
//
//@SuppressLint("DefaultLocale")
//@Composable
//fun OrderCard(
//    order: Order,
//    isKitchenView: Boolean = false,
//    isManagerOrCashier: Boolean = false,
//    onStatusChange: (String) -> Unit,
//    onCardClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp)
//            .clickable { onCardClick() },
//        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Row {
//                        Text(text ="Order No: ", style = MaterialTheme.typography.bodyLarge, fontFamily = Baloo2, fontWeight = FontWeight.SemiBold)
//                        Text(text = order.no, style = MaterialTheme.typography.bodyLarge, fontFamily = Baloo2, fontWeight = FontWeight.Medium)
//                    }
//                    Box(
//                        modifier = Modifier
//                            .background(if (order.type == "Dine-In") GreenTheme else MediumPurple, shape = RoundedCornerShape(8.dp))
//                            .padding(horizontal = 8.dp, vertical = 4.dp)
//                    ) {
//                        Text(text = order.type, color = Color.White, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
//                    }
//                }
//                Row {
//                    Text(text = "Order Time: ", style = MaterialTheme.typography.bodyMedium, fontFamily = Baloo2, fontWeight = FontWeight.SemiBold)
//                    Text(text = order.time, style = MaterialTheme.typography.bodyMedium, fontFamily = Baloo2, fontWeight = FontWeight.Medium)
//                }
//                Column {
//                    Text(text = "Status: ", style = MaterialTheme.typography.bodyMedium, fontFamily = Baloo2, fontWeight = FontWeight.Bold)
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Box(
//                        modifier = Modifier
//                            .background(
//                                when (order.status) {
//                                    "Done" -> GreenDone
//                                    "Preparing" -> YellowPrepare
//                                    "Cancelled" -> RedCancel
//                                    else -> Color.Gray
//                                },
//                                shape = RoundedCornerShape(8.dp)
//                            )
//                            .padding(horizontal = 8.dp, vertical = 4.dp)
//                    ) {
//                        Text(text = order.status, style = MaterialTheme.typography.bodyMedium, fontFamily = Baloo2, fontWeight = FontWeight.SemiBold)
//                    }
//                }
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Box(
//                    modifier = Modifier
//                        .background(color = Color(0xFFDFF6E1), shape = RoundedCornerShape(8.dp))
//                        .padding(horizontal = 12.dp, vertical = 6.dp)
//                ) {
//                    Text(text = "Subtotal: RM ${String.format("%.2f", order.subtotal)}", style = MaterialTheme.typography.bodyLarge, fontFamily = Baloo2, color = DarkGreen, fontWeight = FontWeight.Bold)
//                }
//                Spacer(modifier = Modifier.weight(1f))
//
//                if (isManagerOrCashier) {
//                    Button(
//                        onClick = { onCardClick() },
//                        colors = ButtonDefaults.buttonColors(containerColor = GreenTheme, contentColor = Color.White),
//                        modifier = Modifier.height(36.dp)
//                    ) {
//                        Text("View Details", style = MaterialTheme.typography.bodyMedium, fontFamily = Baloo2)
//                    }
//                }
//
//                if (isKitchenView) {
//                    Button(
//                        onClick = {
//                            val newStatus = if (order.status == "Done") "Preparing" else "Done"
//                            onStatusChange(newStatus)
//                        },
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = if (order.status == "Done") RedCancel else GreenDone
//                        ),
//                        modifier = Modifier.height(36.dp)
//                    ) {
//                        Text(if (order.status == "Done") "Undo" else "Done", color = Color.White, style = MaterialTheme.typography.bodyMedium)
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewOrderManagement() {
//    OrderManagementContent(
//        orders = DummyData.orders,
//        role = UserRole.CASHIER,
//        onViewStatusClick = {},
//        onBackClick = {},
//        onStatusChange = { _, _ -> },
//    )
//}
