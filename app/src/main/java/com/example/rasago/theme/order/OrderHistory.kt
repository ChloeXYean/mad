//package com.example.rasago.theme.order
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.example.rasago.theme.navigation.AppTopBar
//
//data class Order(val orderNumber: String, val time: String, val type: String)
//
//@Composable
//fun OrderItem(order: Order) {
//    Card(
//        modifier = Modifier
//            .padding(8.dp)
//            .fillMaxWidth(),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        shape = MaterialTheme.shapes.medium
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Row(
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    text = "Order Number: ${order.orderNumber}",
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.Bold
//                )
//                Text(
//                    text = "Time: ${order.time}",
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//            Button(
//                onClick = { /*TODO: Handle view receipt*/ },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = when (order.type) {
//                        "Dine In" -> Color.Green
//                        "Take Away" -> Color.Yellow
//                        else -> Color.Gray
//                    }
//                )
//            ) {
//                Text(text = order.type, color = Color.Black)
//            }
//            TextButton(onClick = { /*TODO: Handle view receipt*/ }) {
//                Text("View receipt")
//            }
//        }
//    }
//}
//
//@Composable
//fun OrderList(orders: List<Order>) {
//    LazyColumn {
//        items(orders) { order ->
//            OrderItem(order = order)
//        }
//    }
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun OrderHistoryScreen() {
//    Scaffold(
//        topBar = {
//            AppTopBar(title = "Order History", onBackClick = { /*TODO: Handle back click*/ })
//        }
//    ) { innerPadding ->
//        val orders = listOf(
//            Order("#T01", "9:03 AM", "Dine In"),
//            Order("#T02", "9:15 AM", "Take Away"),
//            Order("#T03", "9:20 AM", "Dine In"),
//            Order("#T04", "9:30 AM", "Take Away")
//        )
//        Column(modifier = Modifier.padding(innerPadding)) {
//            // Search Bar (Placeholder)
//            OutlinedTextField(
//                value = "",
//                onValueChange = {},
//                label = { Text("Search by Order ID") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//            )
//
//            OrderList(orders = orders)
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewOrderHistoryScreen() {
//    MaterialTheme {
//        OrderHistoryScreen()
//    }
//}
//
package com.example.rasago.theme.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

    Scaffold(
        topBar = {
            AppTopBar(title = "Order History", onBackClick = onBackClick)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(orders) { order ->
                OrderHistoryItem(
                    order = order,
                    onViewClick = { onViewOrder(order.id) }
                )
            }
        }
    }
}

@Composable
fun OrderHistoryItem(order: Order, onViewClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Order: #${order.orderNo}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order.orderTime,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Status: ${order.status}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onViewClick, modifier = Modifier.fillMaxWidth()) {
                Text("View Food Status")
            }
        }
    }
}


