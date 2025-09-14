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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rasago.theme.navigation.AppTopBar

data class Order(val orderNumber: String, val time: String, val type: String)

@Composable
fun OrderItem(order: Order) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Order Number: ${order.orderNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Time: ${order.time}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /*TODO: Handle view receipt*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (order.type) {
                        "Dine In" -> Color.Green
                        "Take Away" -> Color.Yellow
                        else -> Color.Gray
                    }
                )
            ) {
                Text(text = order.type, color = Color.Black)
            }
            TextButton(onClick = { /*TODO: Handle view receipt*/ }) {
                Text("View receipt")
            }
        }
    }
}

@Composable
fun OrderList(orders: List<Order>) {
    LazyColumn {
        items(orders) { order ->
            OrderItem(order = order)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            AppTopBar(title = "Order History", onBackClick = onBackClick)
        }
    ) { innerPadding ->
        val orders = listOf(
            Order("#T01", "9:03 AM", "Dine In"),
            Order("#T02", "9:15 AM", "Take Away"),
            Order("#T03", "9:20 AM", "Dine In"),
            Order("#T04", "9:30 AM", "Take Away")
        )
        Column(modifier = Modifier.padding(innerPadding)) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Search by Order ID") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            OrderList(orders = orders)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOrderHistoryScreen() {
    MaterialTheme {
        OrderHistoryScreen(onBackClick = {})
    }
}
