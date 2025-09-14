//package com.example.rasago.theme.order
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//@Composable
//fun OrderConfirmationScreen() {
//    Surface {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Order Confirmation",
//                style = MaterialTheme.typography.headlineSmall,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//
//            // Order Details
//            Text(text = "Order No : T01", style = MaterialTheme.typography.bodyLarge)
//            Text(text = "Order Time : 9:15 AM", style = MaterialTheme.typography.bodyLarge)
//            Divider(modifier = Modifier.padding(vertical = 8.dp))
//
//            // Order Items
//            Text(text = "Order :", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start))
//            OrderItemRow(name = "Nasi Lemak x1", price = "RM 7.50")
//            OrderItemRow(name = "Asam Laksa x1", price = "RM 8.00")
//            Divider(modifier = Modifier.padding(vertical = 8.dp))
//
//            // Totals
//            TotalRow(name = "Subtotal :", price = "RM 15.50")
//            TotalRow(name = "Service Change :", price = "RM 2.00")
//            TotalRow(name = "SST (6%) :", price = "RM 3.45")
//            Divider(modifier = Modifier.padding(vertical = 8.dp))
//            TotalRow(name = "Total Payment :", price = "RM 20.95", fontWeight = FontWeight.Bold)
//
//            // Payment Method
//            Text(text = "Payment Method : Cash", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 16.dp))
//            Button(onClick = { /*TODO: Implement change payment method*/ }) {
//                Text(text = "Change payment method")
//            }
//
//            Spacer(modifier = Modifier.weight(1f)) // Push buttons to the bottom
//
//            // Buttons
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceAround
//            ) {
//                Button(onClick = { /*TODO: Implement proceed action*/ }, colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.Green)) {
//                    Text(text = "Proceed", color = androidx.compose.ui.graphics.Color.White)
//                }
//                Button(onClick = { /*TODO: Implement cancel action*/ }, colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.Red)) {
//                    Text(text = "Cancel", color = androidx.compose.ui.graphics.Color.White)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun OrderItemRow(name: String, price: String) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(text = name, style = MaterialTheme.typography.bodyMedium)
//        Text(text = price, style = MaterialTheme.typography.bodyMedium)
//    }
//}
//
//@Composable
//fun TotalRow(name: String, price: String, fontWeight: FontWeight = FontWeight.Normal) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(text = name, style = MaterialTheme.typography.bodyLarge, fontWeight = fontWeight)
//        Text(text = price, style = MaterialTheme.typography.bodyLarge, fontWeight = fontWeight)
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun OrderConfirmationPreview() {
//    MaterialTheme {
//        OrderConfirmationScreen()
//    }
//}

package com.example.rasago.theme.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rasago.order.OrderUiState
import com.example.rasago.theme.navigation.AppTopBar
import java.text.NumberFormat
import java.util.Locale

// Helper to format currency
fun formatPrice(price: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("ms", "MY")).format(price)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderConfirmationScreen(
    orderState: OrderUiState,
    onBackButtonClicked: () -> Unit,
    onConfirmButtonClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Order Confirmation",
                onBackClick = onBackButtonClicked
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Use LazyColumn for the main content to ensure it's scrollable if needed
            LazyColumn(modifier = Modifier.weight(1f)) {
                // Order Details Section
                item {
                    Text(
                        text = "Order No: #${orderState.order?.orderNo ?: "N/A"}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Order Time: ${orderState.order?.orderTime ?: "N/A"}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                }

                // Order Items Header
                item {
                    Text(
                        text = "Your Order:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }

                // Dynamically list order items
                items(orderState.orderItems) { item ->
                    OrderItemRow(
                        name = "${item.name} x${item.quantity}",
                        price = formatPrice(item.price * item.quantity)
                    )
                }

                // Totals Section
                item {
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    TotalRow(name = "Subtotal:", price = formatPrice(orderState.subtotal))
                    TotalRow(name = "Service Charge:", price = formatPrice(orderState.serviceCharge))
                    TotalRow(name = "SST (6%):", price = formatPrice(orderState.tax))
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    TotalRow(
                        name = "Total Payment:",
                        price = formatPrice(orderState.total),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Payment Method Section
                item {
                    Text(
                        text = "Payment Method: Cash", // Placeholder
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { /* TODO: Implement change payment method */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Change Payment Method")
                    }
                }
            }

            // Buttons at the bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = onBackButtonClicked, // Use the passed-in function
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Cancel", color = Color.White)
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Button(
                    onClick = onConfirmButtonClicked, // Use the passed-in function
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.weight(1f)

                ) {
                    Text(text = "Proceed", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun OrderItemRow(name: String, price: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = name, style = MaterialTheme.typography.bodyMedium)
        Text(text = price, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun TotalRow(name: String, price: String, fontWeight: FontWeight = FontWeight.Normal) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = name, style = MaterialTheme.typography.bodyLarge, fontWeight = fontWeight)
        Text(text = price, style = MaterialTheme.typography.bodyLarge, fontWeight = fontWeight)
    }
}