package com.example.rasago

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rasago.data.model.Order
import com.example.rasago.ui.theme.Baloo2
import com.example.rasago.ui.theme.DarkGreen
import com.example.rasago.ui.theme.GreenDone
import com.example.rasago.ui.theme.GreenTheme
import com.example.rasago.ui.theme.GreyGreen
import com.example.rasago.ui.theme.LightGreen
import com.example.rasago.ui.theme.MediumPurple
import com.example.rasago.ui.theme.RedCancel
import com.example.rasago.ui.theme.YellowPrepare
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun OrderManagement(
    role: UserRole = UserRole.CASHIER,
    context: Context = LocalContext.current,
    onViewStatusClick: (Order) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val orderNo = "T01"
    val orderTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedStatus by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var showStatusDropdown by remember { mutableStateOf(false) }

    // Filtered orders
//    val filteredOrders = DummyData.orders.filter { order ->
//        val orderDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//            .parse(order.time.split(" ")[0])
//        val selectedDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//            .format(selectedDate.time)
//        val selectedDateParsed = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//            .parse(selectedDateStr)
//
//
//        (orderDate?.equals(selectedDateParsed) == true) &&
//                (selectedStatus == "All" || order.status == selectedStatus) &&
//                (searchQuery.isEmpty() || order.no.contains(searchQuery, ignoreCase = true)
//                        || order.type.contains(searchQuery, ignoreCase = true))
//    }

    //Use for test
    val filteredOrders = DummyData.orders.filter { order ->
        val matchesDate = run {
            val orderDateStr = order.time.split(" ")[0] // e.g. "2025-09-09"
            val selectedDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(selectedDate.time)
            orderDateStr == selectedDateStr
        }

        (matchesDate || selectedStatus == "All") &&
                (selectedStatus == "All" || order.status == selectedStatus) &&
                (searchQuery.isEmpty() || order.no.contains(searchQuery, ignoreCase = true)
                        || order.type.contains(searchQuery, ignoreCase = true))
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Order Management",
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

            }

            Spacer(modifier = Modifier.height(12.dp))


            LazyColumn {
                items(filteredOrders.size) { index ->
                    val order = filteredOrders[index]
                    OrderCard(
                        order = order,
                        isKitchenView = (role == UserRole.KITCHEN),
                        isManagerOrCashier = (role == UserRole.MANAGER || role == UserRole.CASHIER)
                    )
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: Order,
    isKitchenView: Boolean = false, // true if in kitchen
    isManagerOrCashier: Boolean = false // true if manager/cashier
) {
    var currentStatus by remember { mutableStateOf(order.status) }
    var isDoneGreen by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Text(
                            text ="Order No: ",
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = Baloo2,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = order.no,
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = Baloo2,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                if (order.type == "Dine-In") GreenTheme else MediumPurple,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = order.type,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Row {
                    Text(
                        text = "Order Time: ",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = Baloo2,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = order.time,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = Baloo2,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column {
                    Text(
                        text = "Status: ",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = Baloo2,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                when (currentStatus) {
                                    "Done" -> GreenDone // Green
                                    "Preparing" -> YellowPrepare
                                    "Cancelled" -> RedCancel
                                    else -> Color.Gray
                                },
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = currentStatus,
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = Baloo2,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

            }
            Spacer (modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFDFF6E1),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Subtotal: RM ${String.format("%.2f", order.subtotal)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = Baloo2,
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold
                    )
                }


                Spacer(modifier = Modifier.weight(1f))

                if (isManagerOrCashier) {
                    Button(
                        onClick = { /* Open receipt */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenTheme,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            "View Receipt",
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = Baloo2
                        )
                    }
                }

                if (isKitchenView) {
                    Button(
                        onClick = {
                            isDoneGreen = !isDoneGreen
                            currentStatus = if (isDoneGreen) "Done" else "Preparing"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDoneGreen) GreenDone else RedCancel
                        ),
                        modifier = Modifier
                            .height(36.dp)
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            "Done",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

    }
    Spacer (modifier = Modifier.height(8.dp))

}

@Preview(showBackground = true)
@Composable
fun PreviewOrderManagement(){
    OrderManagement()
}