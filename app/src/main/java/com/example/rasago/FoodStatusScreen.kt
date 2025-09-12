package com.example.rasago

import android.content.Context
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rasago.ui.theme.Baloo2
import com.example.rasago.ui.theme.DarkGreen
import com.example.rasago.ui.theme.LightGreen
import com.example.rasago.ui.theme.GreenTheme
import com.example.rasago.ui.theme.YellowPrepare
import com.example.rasago.ui.theme.GreenDone
import com.example.rasago.ui.theme.RedCancel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import android.app.DatePickerDialog
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import com.example.rasago.data.model.Order
import com.example.rasago.ui.theme.GreyGreen
import com.example.rasago.ui.theme.order.OrderViewModel
import java.util.Calendar
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState


data class FoodOrder(
    val id: Int,
    val imageRes: Int,
    val name: String,
    val status: String
)
val foodOrders = listOf(
    FoodOrder(1, android.R.drawable.ic_menu_gallery, "Spaghetti", "Preparing"),
    FoodOrder(2, android.R.drawable.ic_menu_camera, "Pizza", "Ready"),
    FoodOrder(3, android.R.drawable.ic_menu_send, "Burger", "Cancelled")
)
//TODO: Customer
//TODO: Staff
@Composable //TODO: Soon change to pass parameter, dont fix
fun FoodStatusScreen(role: UserRole = UserRole.CUSTOMER, onBackClick: () -> Unit = {}) {
    val orderNo = "ORD12345"
    val orderTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()) // e.g., "11:47 PM" on 09/08/2025

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Food Status",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            OrderTypeSelection()
            Divider(modifier = Modifier.padding(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LightGreen.copy(alpha = 0.3f)),
                border = BorderStroke(width = 1.dp, color = DarkGreen)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Order No: $orderNo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Baloo2,
                        color = DarkGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Order Time: $orderTime",
                        fontSize = 16.sp,
                        fontFamily = Baloo2,
                        color = DarkGreen
                    )
                }

            }
            Text(
                text = "Food Status: ",
                fontFamily = Baloo2,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(foodOrders) { order ->
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
                                    painter = painterResource(id = order.imageRes),
                                    contentDescription = order.name,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = order.name,
                                fontFamily = Baloo2,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                            )
                        }

                        StatusDropdown(
                            status = order.status,
                            enabled = (role == UserRole.STAFF)
                        )
                    }
                }
            }
        }
    }
}
//Staff
@Composable
fun OrderTypeSelection() {
    val dineIn = stringResource(R.string.dine_in)
    val takeAway = stringResource(R.string.take_away)
    var orderType by remember { mutableStateOf(dineIn) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val dineInColor by animateColorAsState(
            targetValue = if (orderType == dineIn) GreenTheme else Color.White
        )
        val takeAwayColor by animateColorAsState(
            targetValue = if (orderType == takeAway) GreenTheme else Color.White
        )
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
                    color =
                        if (orderType == stringResource(R.string.dine_in)) GreenTheme
                        else Color.White
                )
                .clickable { orderType = dineIn },
            contentAlignment = Alignment.Center

        ) {
            Column {
                //TODO: Fix the prb of when selected the image is black not white (ICON)
                if (orderType == dineIn) {
                    Icon(
                        painter = painterResource(R.drawable.dine_in_white),
                        contentDescription = stringResource(R.string.dine_in),
                        modifier = Modifier.size(24.dp).align(Alignment.CenterHorizontally)
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.dine_in_black),
                        contentDescription = stringResource(R.string.dine_in),
                        modifier = Modifier.size(24.dp).align(Alignment.CenterHorizontally)

                    )
                }
                Text(
                    text = dineIn,
                    fontSize = 16.sp,
                    fontWeight =
                        if (orderType == dineIn) FontWeight.Bold else FontWeight.Normal,
                    color =
                        if (orderType == dineIn) Color.White else Color.Black,
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
                    color =
                        if (orderType == stringResource(R.string.take_away)) GreenTheme
                        else Color.White
                )
                .clickable { orderType = takeAway },
            contentAlignment = Alignment.Center
        ) {
            Column {
                if (orderType == takeAway) {
                    Icon(
                        painter = painterResource(R.drawable.take_away_white),
                        contentDescription = stringResource(R.string.take_away),
                        modifier = Modifier.size(24.dp).align(Alignment.CenterHorizontally)
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.take_away_black),
                        contentDescription = stringResource(R.string.take_away),
                        modifier = Modifier.size(24.dp).align(Alignment.CenterHorizontally)
                    )
                }

                Text(
                    text = takeAway,
                    fontSize = 16.sp,
                    fontWeight =
                        if (orderType == stringResource(R.string.take_away)) FontWeight.Bold else FontWeight.Normal,
                    color =
                        if (orderType == stringResource(R.string.take_away)) Color.White else Color.Black,
                    fontFamily = Baloo2
                )
            }
        }
    }
}

@Composable
fun StatusDropdown(status: String, enabled: Boolean) {
    var expanded by remember { mutableStateOf(false) }
    val statusText = when (status) {
        "Preparing" -> R.string.preparing
        "Ready" -> R.string.ready
        "Cancelled" -> R.string.cancel
        else -> R.string.defaultStatus
    }
    val statusColor = when (status) {
        "Preparing" -> YellowPrepare
        "Ready" -> GreenDone
        "Cancelled" -> RedCancel
        else -> Color.LightGray
    }
    val statusImage = when (status) {
        "Preparing" -> R.drawable.food_prepare
        "Ready" -> R.drawable.food_ready
        "Cancelled" -> R.drawable.food_cancel
        else -> R.drawable.background_image
    }


    Box(
        modifier = Modifier
            .width(120.dp)
            .background(statusColor, shape = RoundedCornerShape(48.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (status == "Ready"){
                Icon(
                    painter = painterResource(id = statusImage),
                    contentDescription = stringResource(id = statusText),
                    modifier = Modifier
                        .size(20.dp).offset(x = (-8).dp)
                )
            }
            else {
                Icon(
                    painter = painterResource(id = statusImage),
                    contentDescription = stringResource(id = statusText),
                    modifier = Modifier
                        .size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = status,
                fontSize = 16.sp,
                fontFamily = Baloo2,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.clickable(enabled = enabled) {
                    if (enabled) expanded = !expanded
                }
            )
        }

        DropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = { if (enabled) expanded = false },
            modifier = Modifier.background(statusColor)
        ) {
            DropdownMenuItem(
                text = { Text(text = "Placeholder", color = Color.Black) },
                onClick = {},
                // TODO: Update status in your state (ViewModel or remember list)
                enabled = false
            )
        }
    }
}




// ----------------- STAFF ONLY -----------------


val dummyOrders = listOf(
    Order("001", "2025-09-09 10:00", "Complete", "Dine-In"),
    Order("002", "2025-09-09 11:00", "Pending", "Takeaway"),
    Order("003", "2025-09-09 12:00", "Complete", "Delivery"),
)

@Composable
fun StaffPage(
    onViewStatusClick: (Order) -> Unit = {}, // Callback for navigation to another page
    context: Context = LocalContext.current
) {
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedStatus by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var showStatusDropdown by remember { mutableStateOf(false) }

    // Filtered orders based on date, status, and search
    val filteredOrders = dummyOrders.filter { order ->
        val orderDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(order.time.split(" ")[0])
        val selectedDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
        val selectedDateParsed = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selectedDateStr)

        (orderDate?.equals(selectedDateParsed) == true) &&
                (selectedStatus == "All" || order.status == selectedStatus) &&
                (searchQuery.isEmpty() || order.no.contains(searchQuery, ignoreCase = true) || order.type.contains(searchQuery, ignoreCase = true))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        selectedDate.set(year, month, day)
                    },
                    selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH),
                    selectedDate.get(Calendar.DAY_OF_MONTH)
                ).show()
            }) {
                Text("Pick Date")
            }

            Box {
                Button(onClick = { showStatusDropdown = true }) {
                    Text("Status: $selectedStatus")
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = showStatusDropdown,
                    onDismissRequest = { showStatusDropdown = false }
                ) {
                    DropdownMenuItem(text = { Text("All") }, onClick = { selectedStatus = "All"; showStatusDropdown = false })
                    DropdownMenuItem(text = { Text("Complete") }, onClick = { selectedStatus = "Complete"; showStatusDropdown = false })
                    DropdownMenuItem(text = { Text("Pending") }, onClick = { selectedStatus = "Pending"; showStatusDropdown = false })
                    // Add more statuses as needed
                }
            }

            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.width(150.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(filteredOrders) { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Order No: ${order.no}", style = MaterialTheme.typography.bodyLarge)
                            Text("Order Time: ${order.time}", style = MaterialTheme.typography.bodyMedium)
                        }
                        Text("Status: ${order.status}", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Type: ${order.type}", style = MaterialTheme.typography.bodyLarge)
                            // Button to change order type/status (example: toggle between types)
                            var currentType by remember { mutableStateOf(order.type) }
                            IconButton(onClick = {
                                // Example logic: toggle between Dine-In and Takeaway
                                currentType = if (currentType == "Dine-In") "Takeaway" else "Dine-In"
                            }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Change Type") // Can replace with a like/thumbs-up icon if intended
                            }
                        }
                        Text(
                            "View Status",
                            modifier = Modifier.clickable { onViewStatusClick(order) },
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun FoodOrderStatusList(
    orderViewModel: OrderViewModel,
    role: UserRole = UserRole.CUSTOMER,
    context: Context = LocalContext.current,
    onViewStatusClick: (Order) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val orders by orderViewModel.orders.observeAsState(emptyList())
    val context = LocalContext.current

    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedStatus by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var showStatusDropdown by remember { mutableStateOf(false) }

    // Filtered orders
    val filteredOrders = dummyOrders.filter { order ->
        val orderDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .parse(order.time.split(" ")[0])
        val selectedDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(selectedDate.time)
        val selectedDateParsed = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .parse(selectedDateStr)

        (orderDate?.equals(selectedDateParsed) == true) &&
                (selectedStatus == "All" || order.status == selectedStatus) &&
                (searchQuery.isEmpty() || order.no.contains(searchQuery, ignoreCase = true)
                        || order.type.contains(searchQuery, ignoreCase = true))
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Food Status",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Orders") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LightGreen,
                    unfocusedContainerColor = LightGreen,
                    focusedIndicatorColor = DarkGreen,
                    unfocusedIndicatorColor = GreyGreen,
                    focusedLabelColor = DarkGreen,
                    unfocusedLabelColor = DarkGreen
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                selectedDate.set(year, month, day)
                            },
                            selectedDate.get(Calendar.YEAR),
                            selectedDate.get(Calendar.MONTH),
                            selectedDate.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = LightGreen,
                        contentColor = DarkGreen
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, GreyGreen.copy(alpha = 0.6f))
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Pick Date")
                }

                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { showStatusDropdown = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = LightGreen,
                            contentColor = DarkGreen
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, GreyGreen.copy(alpha = 0.6f))
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = selectedStatus,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.offset(-4.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                    }

                    DropdownMenu(
                        expanded = showStatusDropdown,
                        onDismissRequest = { showStatusDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "All",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            onClick = { selectedStatus = "All"; showStatusDropdown = false })
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Complete",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            onClick = { selectedStatus = "Complete"; showStatusDropdown = false })
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Pending",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            onClick = { selectedStatus = "Pending"; showStatusDropdown = false })
                    }
                }



                Spacer(modifier = Modifier.height(16.dp))
                //Use Lazy Column 
                // TODO: Wait for Order History Code (Hux)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
//    FoodOrderStatusList()
}