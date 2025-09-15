package com.example.rasago.theme.payment.cashier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CashPaymentScreen() {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable { /* TODO: Implement back navigation */ }
                )
                Text(
                    text = "Cash Payment",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Order Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Order No :", style = MaterialTheme.typography.bodyLarge)
                Text(text = "T01", style = MaterialTheme.typography.bodyLarge)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Total Payment :", style = MaterialTheme.typography.bodyLarge)
                Text(text = formatCurrency(20.95), style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Customer Paid Input
            var customerPaid by remember { mutableStateOf("") }
            OutlinedTextField(
                value = customerPaid,
                onValueChange = { customerPaid = it },
                label = { Text("Customer Paid :") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontSize = 18.sp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Calculate Change
            val totalPayment = 20.95 // Hardcoded total payment
            val change: Double = try {
                val paidAmount = customerPaid.toDouble()
                paidAmount - totalPayment
            } catch (e: NumberFormatException) {
                0.0 // Handle invalid input
            }

            // Display Change
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Change :", style = MaterialTheme.typography.bodyLarge)
                Text(text = formatCurrency(change), style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { /* TODO: Implement confirm payment */ }, colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.Green)) {
                    Text(text = "Confirm Payment", color = Color.Green)
                }
                Button(onClick = { /* TODO: Implement cancel action */ }, colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.Red)) {
                    Text(text = "Cancel", color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "MY")) // Malaysian Ringgit
    return format.format(amount)
}

@Preview(showBackground = true)
@Composable
fun CashPaymentPreview() {
    MaterialTheme {
        CashPaymentScreen()
    }
}
