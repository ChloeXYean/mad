
package com.example.rasago

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val backgroundColor = Color(0xFFF0F0F0) // 浅灰色背景
val cardColor = Color.White // 白色卡片背景

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryScreen(
    onBackClick: () -> Unit,
    onAddItemClick: () -> Unit,
    onQuantityChange: (itemIndex: Int, change: Int) -> Unit,
    selectedPaymentMethod: Int,
    onPaymentMethodSelect: (Int) -> Unit,
    onPlaceOrderClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Order Summary", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                modifier = Modifier.padding(15.dp)
            )
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
                .fillMaxSize()
        ) {
            // 上半部分：Your Order + Payment Details
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Your Order 区域：独立卡片
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
                            modifier = Modifier.height(30.dp).width(105.dp)
                        ) {
                            Text(
                                text = "Add Items",
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Normal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val items = listOf(
                        Item("Nasi Lemak", 7.50, 1),
                        Item("Asam Laksa", 8.00, 1)
                    )
                    items.forEachIndexed { index, item ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                // 这里可添加图片，示例用占位
                                Text(text = item.name)
                                Text(
                                    text = "Edit",
                                    fontSize = 12.sp,
                                    color = Color(0xFF236cb2)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(5.dp),
                                    verticalArrangement = Arrangement.SpaceEvenly,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "${String.format("%.2f", item.price)}")
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .border(1.dp, Color(0xFF4CAF50), shape = CircleShape)
                                            .clickable { /* 数量增减逻辑可后续补充 */ },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = item.quantity.toString())
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

// Subtotal 行
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Subtotal:")
                        Text(text = "RM 15.50")
                    }

// SST (6%) 行
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "SST (6%):")
                        Text(text = "RM ${String.format("%.2f", (15.50 * 0.06))}")
                    }
                }

                // Payment Details 区域：独立卡片
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cardColor, shape = RoundedCornerShape(8.dp))
                        .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp)
                        .fillMaxHeight()
                ) {
                    Text(text = "Payment Details", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    // 带图标的单选按钮
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

            // 底部：Total + 下单按钮（贴底，带阴影）
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 500.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Total", style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = "RM ${15.50 + 15.50 * 0.06}",
                                style = MaterialTheme.typography.titleMedium
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
        }
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
                modifier = Modifier.background(
                    Color.LightGray, shape = RoundedCornerShape(20.dp)
                ).padding(4.dp)


            ){
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

data class Item(val name: String, val price: Double, val quantity: Int)

@Preview(showBackground = true, name = "Order Summary Preview")
@Composable
fun OrderSummaryPreview() {
    MaterialTheme {
        OrderSummaryScreen(
            onBackClick = { },
            onAddItemClick = { },
            onQuantityChange = { _, _ -> },
            selectedPaymentMethod = 0,
            onPaymentMethodSelect = { },
            onPlaceOrderClick = { }
        )
    }
}