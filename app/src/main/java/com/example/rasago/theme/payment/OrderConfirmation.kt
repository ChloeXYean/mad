package com.example.rasago.theme.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rasago.data.model.CartItem
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OrderConfirmationScreen(
    cartItems: List<CartItem> = emptyList(),
    paymentMethod: String = "QR Scan",
    onContinueClick: () -> Unit = {},
    onChangePaymentClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Calculate totals based on the cart items passed in.
    val subtotal = cartItems.sumOf { it.calculateTotalPrice().toDouble() }
    val serviceCharge = subtotal * 0.1
    val sst = subtotal * 0.06
    val totalPayment = subtotal + serviceCharge + sst
    val orderNo = "T${System.currentTimeMillis() % 10000}"
    val orderTime = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())

    // State for managing the visibility of the success dialog.
    var showPaymentSuccess by remember { mutableStateOf(false) }

    Surface {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)) // Light grey background for the whole screen.
        ) {
            // Top title area.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Order Confirmation",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Main details card.
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Order number and time.
                    Text("Order No : $orderNo", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Text("Order Time : $orderTime", style = MaterialTheme.typography.bodyLarge)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // List of items in the order.
                    Text("Order :", style = MaterialTheme.typography.titleMedium)
                    // Use a LazyColumn for potentially long lists of items and add-ons
                    LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                        items(cartItems) { cartItem ->
                            val itemName = cartItem.menuItem.name
                            OrderItemRow(
                                name = "$itemName x${cartItem.quantity}",
                                price = "RM ${String.format("%.2f", cartItem.calculateTotalPrice())}"
                            )
                            // Display selected add-ons for each item.
                            if (cartItem.selectedAddOns.any { it.quantity > 0 }) {
                                Column(modifier = Modifier.padding(start = 16.dp)) {
                                    cartItem.selectedAddOns.filter { it.quantity > 0 }.forEach { addOn ->
                                        Text(
                                            text = "+ ${addOn.name} x${addOn.quantity}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Price summary section.
                    Text("Price Summary:", style = MaterialTheme.typography.titleMedium)
                    TotalRow(name = "Subtotal :", price = "RM ${String.format("%.2f", subtotal)}")
                    TotalRow(name = "Service Charge (10%) :", price = "RM ${String.format("%.2f", serviceCharge)}")
                    TotalRow(name = "SST (6%) :", price = "RM ${String.format("%.2f", sst)}")
                    Spacer(modifier = Modifier.height(12.dp))
                    TotalRow(
                        name = "Total Payment :",
                        price = "RM ${String.format("%.2f", totalPayment)}",
                        fontWeight = FontWeight.Bold
                    )
                    Divider(modifier = Modifier.padding(vertical = 16.dp))

                    // Payment method display and change button.
                    Text("Payment Method : $paymentMethod", style = MaterialTheme.typography.bodyLarge)
                    Button(
                        onClick = onChangePaymentClick,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(text = "Change payment method")
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f)) // Pushes the action buttons to the bottom.

            // Bottom action buttons area with shadow.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { showPaymentSuccess = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Proceed", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onCancelClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Cancel", fontSize = 16.sp)
                    }
                }
            }
        }
    }

    // Dialog to show after payment is successful.
    if (showPaymentSuccess) {
        AlertDialog(
            onDismissRequest = { showPaymentSuccess = false },
            title = { Text("Payment Successful!", fontWeight = FontWeight.Bold) },
            text = { Text("Your order has been placed successfully!") },
            confirmButton = {
                Button(
                    onClick = {
                        showPaymentSuccess = false
                        onContinueClick() // This triggers the save order and navigation logic.
                    }
                ) {
                    Text("Continue")
                }
            }
        )
    }
}

@Composable
fun OrderItemRow(name: String, price: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Text(price, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
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
        Text(name, style = MaterialTheme.typography.bodyLarge, fontWeight = fontWeight)
        Text(price, style = MaterialTheme.typography.bodyLarge, fontWeight = fontWeight)
    }
}

