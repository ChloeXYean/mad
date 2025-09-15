package com.example.rasago.theme.order

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rasago.R
import com.example.rasago.data.model.CartItem
import com.example.rasago.order.OrderViewModel
import com.example.rasago.ui.theme.GreenTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryScreen(
    orderViewModel: OrderViewModel,
    onNavigateToPayment: () -> Unit,
    onNavigateBack: () -> Unit,
    onAddItemClick: () -> Unit,
    onEditItem: (CartItem, Int) -> Unit
) {
    val orderState by orderViewModel.uiState.collectAsState()
    val cartItems = orderState.orderItems

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Order Summary", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 120.dp), // 留出底部空间
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    YourOrderCard(cartItems = cartItems)
                }

                item {
                    OrderTypeSelectionCard(
                        selectedType = orderState.orderType,
                        onTypeSelected = { orderViewModel.setOrderType(it) }
                    )
                }

                item {
                    PaymentDetailsCard(
                        selectedPaymentMethod = orderState.paymentMethod,
                        onPaymentMethodSelect = { orderViewModel.setPaymentMethod(it) }
                    )
                }
            }

            // Bottom Floating Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                TotalFooterCard(
                    total = orderState.total,
                    onPlaceOrderClick = onNavigateToPayment
                )
            }
        }
    }
}

@Composable
fun YourOrderCard(cartItems: List<CartItem>) {
    val cardColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Your Order",
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            cartItems.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.menuItem.name,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "RM ${String.format("%.2f", item.menuItem.price)}",
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun OrderTypeSelectionCard(selectedType: String, onTypeSelected: (String) -> Unit) {
    val dineIn = stringResource(R.string.dine_in)
    val takeAway = stringResource(R.string.take_away)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(text = "Order Type", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OrderTypeButton(
                text = dineIn,
                iconRes = if (selectedType == "Dine-In") R.drawable.dine_in_white else R.drawable.dine_in_black,
                isSelected = selectedType == "Dine-In",
                onClick = { onTypeSelected("Dine-In") }
            )
            Spacer(modifier = Modifier.width(16.dp))
            OrderTypeButton(
                text = takeAway,
                iconRes = if (selectedType == "Take-away") R.drawable.take_away_white else R.drawable.take_away_black,
                isSelected = selectedType == "Take-away",
                onClick = { onTypeSelected("Take-away") }
            )
        }
    }
}

@Composable
fun OrderTypeButton(text: String, iconRes: Int, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = if (isSelected) GreenTheme else MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                modifier = Modifier.size(24.dp),
                tint = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun PaymentDetailsCard(selectedPaymentMethod: String, onPaymentMethodSelect: (Int) -> Unit) {
    val selectedIndex = when (selectedPaymentMethod) {
        "QR Scan" -> 0
        "Cash" -> 1
        "Card" -> 2
        else -> 0
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()

            .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(8.dp))

            .padding(16.dp)
    ) {
        Text(text = "Payment Details", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        RadioButtonRow(
            text = "QR Scan",
            icon = Icons.Default.QrCodeScanner,
            selected = selectedIndex == 0,
            onClick = { onPaymentMethodSelect(0) }
        )
        RadioButtonRow(
            text = "Cash",
            icon = Icons.Default.Money,
            selected = selectedIndex == 1,
            onClick = { onPaymentMethodSelect(1) }
        )
        RadioButtonRow(
            text = "Card",
            icon = Icons.Default.CreditCard,
            selected = selectedIndex == 2,
            onClick = { onPaymentMethodSelect(2) }
        )
    }
}

@Composable
fun RadioButtonRow(
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
                    .background(MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(20.dp))
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(text = text, style = MaterialTheme.typography.bodyLarge)
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Total:", fontWeight = FontWeight.Bold)
                Text(text = "RM ${String.format("%.2f", total)}", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onPlaceOrderClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Place Order")
            }
        }
    }
}