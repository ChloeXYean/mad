package com.example.rasago.theme.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rasago.data.model.Order
import com.example.rasago.data.model.OrderItem

@Composable
fun OrderConfirmationScreen(
    order: Order,
    paymentMethod: String, // 添加支付方式参数
    onProceed: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)) // 整体背景为浅灰色
        ) {
            // 标题区域
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

            Spacer(modifier = Modifier.padding(10.dp))

            // 订单详情卡片
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // 订单基本信息
                    Column(
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "Order No : ${order.no}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Order Time : ${order.time}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }

                    // 订单项列表
                    Text(
                        text = "Order :",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    order.orderItems.forEach { item ->
                        OrderItemRow(
                            name = "${item.name} x${item.quantity}",
                            price = "RM ${String.format("%.2f", item.price * item.quantity)}"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // 价格汇总
                    Text(
                        text = "Price Summary:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    val subtotal = order.subtotal
                    TotalRow(name = "Subtotal :", price = "RM ${String.format("%.2f", subtotal)}")
                    TotalRow(name = "Service Charge :", price = "RM 2.00")
                    TotalRow(name = "SST (6%) :", price = "RM ${String.format("%.2f", subtotal * 0.06)}")

                    Spacer(modifier = Modifier.height(12.dp))
                    TotalRow(
                        name = "Total Payment :",
                        price = "RM ${String.format("%.2f", subtotal + 2.00 + subtotal * 0.06)}",
                        fontWeight = FontWeight.Bold
                    )
                    Divider(modifier = Modifier.padding(vertical = 16.dp))

                    // 支付方式
                    Text(
                        text = "Payment Method : $paymentMethod",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Button(
                        onClick = { /* 实现修改支付方式逻辑 */ },
                        modifier = Modifier
                            .width(210.dp)
                            .height(50.dp)
                            .padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5099aa),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Change payment method")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // 将按钮推到底部

            // 操作按钮区域

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(120.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp), // 给按钮区域设置左右内边距，保证按钮与边缘有距离
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onProceed,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(24.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        Text("Proceed", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onCancel,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(24.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        Text("Cancel", fontSize = 16.sp)
                    }
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
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = price,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
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
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = fontWeight
        )
        Text(
            text = price,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = fontWeight
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OrderConfirmationPreview() {
    // 创建示例订单数据
    val orderItems = listOf(
        OrderItem(name = "Nasi Lemak", price = 7.50, quantity = 1),
        OrderItem(name = "Asam Laksa", price = 8.00, quantity = 1)
    )

    val sampleOrder = Order(
        no = "T01",
        type = "Dine-In",
        time = "9:15 AM",
        status = "Preparing",
        orderItems = orderItems
    )

    MaterialTheme {
        OrderConfirmationScreen(
            order = sampleOrder,
            paymentMethod = "Cash", // 传递支付方式
            onProceed = { },
            onCancel = { }
        )
    }
}
