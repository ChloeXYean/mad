package com.example.rasago.theme.order

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OrderConfirmationScreen() {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Order Confirmation",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Order Details
            Text(text = "Order No : T01", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Order Time : 9:15 AM", style = MaterialTheme.typography.bodyLarge)
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Order Items
            Text(text = "Order :", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start))
            OrderItemRow(name = "Nasi Lemak x1", price = "RM 7.50")
            OrderItemRow(name = "Asam Laksa x1", price = "RM 8.00")
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Totals
            TotalRow(name = "Subtotal :", price = "RM 15.50")
            TotalRow(name = "Service Change :", price = "RM 2.00")
            TotalRow(name = "SST (6%) :", price = "RM 3.45")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            TotalRow(name = "Total Payment :", price = "RM 20.95", fontWeight = FontWeight.Bold)

            // Payment Method
            Text(text = "Payment Method : Cash", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 16.dp))
            Button(onClick = { /*TODO: Implement change payment method*/ }) {
                Text(text = "Change payment method")
            }

            Spacer(modifier = Modifier.weight(1f)) // Push buttons to the bottom

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { /*TODO: Implement proceed action*/ }, colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.Green)) {
                    Text(text = "Proceed", color = androidx.compose.ui.graphics.Color.White)
                }
                Button(onClick = { /*TODO: Implement cancel action*/ }, colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.Red)) {
                    Text(text = "Cancel", color = androidx.compose.ui.graphics.Color.White)
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

@Preview(showBackground = true)
@Composable
fun OrderConfirmationPreview() {
    MaterialTheme {
        OrderConfirmationScreen()
    }
}
