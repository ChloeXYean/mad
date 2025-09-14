package com.example.rasago.theme.order

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rasago.data.model.MenuItem
import com.example.rasago.order.OrderUiState
import java.text.NumberFormat
import java.util.Locale

// --- Style Constants ---
private val backgroundColor = Color(0xFFF0F0F0)
private val cardColor = Color.White

// --- Helper Function ---
private fun formatPriceForSummary(price: Double): String {
    return "RM ${String.format(Locale.US, "%.2f", price)}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryScreen(
    orderUiState: OrderUiState,
    onNextButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    onAddItemClick: () -> Unit,
    onIncreaseItem: (MenuItem) -> Unit,
    onDecreaseItem: (MenuItem) -> Unit,
    onRemoveItem: (MenuItem) -> Unit
) {
    var selectedPaymentMethod by remember { mutableStateOf(0) } // 0: QR, 1: Cash, 2: Card

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Order Summary", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onCancelButtonClicked) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    YourOrderCard(
                        orderItems = orderUiState.orderItems,
                        subtotal = orderUiState.subtotal,
                        tax = orderUiState.tax,
                        onAddItemClick = onAddItemClick,
                        onIncrease = onIncreaseItem,
                        onDecrease = onDecreaseItem,
                        onRemove = onRemoveItem
                    )
                }
                item {
                    PaymentDetailsCard(
                        selectedPaymentMethod = selectedPaymentMethod,
                        onPaymentMethodSelect = { selectedPaymentMethod = it }
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                TotalFooterCard(
                    total = orderUiState.total,
                    onPlaceOrderClick = onNextButtonClicked
                )
            }
        }
    }
}

@Composable
private fun YourOrderCard(
    orderItems: List<MenuItem>,
    subtotal: Double,
    tax: Double,
    onAddItemClick: () -> Unit,
    onIncrease: (MenuItem) -> Unit,
    onDecrease: (MenuItem) -> Unit,
    onRemove: (MenuItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(cardColor, shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Your Order", style = MaterialTheme.typography.titleMedium)
            Button(
                onClick = onAddItemClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF236cb2),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .height(30.dp)
                    .width(105.dp)
            ) {
                Text(
                    text = "Add Items",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Normal
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        orderItems.forEach { item ->
            SummaryItemRow(
                item = item,
                onIncrease = { onIncrease(item) },
                onDecrease = { onDecrease(item) },
                onRemove = { onRemove(item) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Subtotal:")
            Text(text = formatPriceForSummary(subtotal))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "SST (6%):")
            Text(text = formatPriceForSummary(tax))
        }
    }
}

@Composable
private fun SummaryItemRow(
    item: MenuItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = item.name, modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onDecrease, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease quantity")
            }
            Text(text = item.quantity.toString())
            IconButton(onClick = onIncrease, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Add, contentDescription = "Increase quantity")
            }
            IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Delete, contentDescription = "Remove item", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}


@Composable
private fun PaymentDetailsCard(
    selectedPaymentMethod: Int,
    onPaymentMethodSelect: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(cardColor, shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(text = "Payment Details", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        RadioButtonRow(
            text = "QR Scan",
            icon = Icons.Default.QrCodeScanner,
            selected = selectedPaymentMethod == 0,
            onClick = { onPaymentMethodSelect(0) }
        )
        RadioButtonRow(
            text = "Cash",
            icon = Icons.Default.Money,
            selected = selectedPaymentMethod == 1,
            onClick = { onPaymentMethodSelect(1) }
        )
        RadioButtonRow(
            text = "Card",
            icon = Icons.Default.CreditCard,
            selected = selectedPaymentMethod == 2,
            onClick = { onPaymentMethodSelect(2) }
        )
    }
}

@Composable
private fun RadioButtonRow(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Color.LightGray, shape = RoundedCornerShape(20.dp)
                    )
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        RadioButton(
            selected = selected,
            onClick = null,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun TotalFooterCard(total: Double, onPlaceOrderClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Total", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = formatPriceForSummary(total),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onPlaceOrderClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Place Order")
            }
        }
    }
}