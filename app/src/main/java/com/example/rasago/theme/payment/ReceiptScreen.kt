package com.example.rasago.theme.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rasago.theme.navigation.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(
    orderNo: String,
    orderTime: String,
    orderType: String,
    orderItems: List<Pair<String, Float>>,
    subtotal: Double,
    serviceCharge: Double,
    tax: Double,
    takeAwayCharge: Double,
    paymentMethod: String,
    onBackClick: () -> Unit,
    onProceedClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Receipt",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OrderInfoSection(orderNo, orderTime, orderType)
                        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                        OrderItemsSection(orderItems)
                        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                        PaymentSummarySection(subtotal, serviceCharge, tax, takeAwayCharge)
                        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                        PaymentMethodSection(paymentMethod)
                    }
                }
            }
            
            item {
                Button(
                    onClick = onProceedClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Continue",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderInfoSection(orderNo: String, orderTime: String, orderType: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Order Information", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF424242))
        InfoRow("Order Number", orderNo)
        InfoRow("Order Time", orderTime)
        InfoRow("Order Type", orderType)
    }
}

@Composable
private fun OrderItemsSection(orderItems: List<Pair<String, Float>>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Order Items", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF424242))
        LazyColumn(
            modifier = Modifier.heightIn(max = 300.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(orderItems) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = item.first,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF424242),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "RM ${String.format("%.2f", item.second)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentSummarySection(subtotal: Double, serviceCharge: Double, tax: Double, takeAwayCharge: Double) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Payment Summary", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF424242))
        InfoRow("Subtotal", "RM ${String.format("%.2f", subtotal)}")
        InfoRow("Service Charge (10%)", "RM ${String.format("%.2f", serviceCharge)}")
        if (takeAwayCharge > 0) {
            InfoRow("Take-away Charge", "RM ${String.format("%.2f", takeAwayCharge)}")
        }
        InfoRow("SST (6%)", "RM ${String.format("%.2f", tax)}")
        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
        val total = subtotal + serviceCharge + tax + takeAwayCharge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Total Payment", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF424242))
            Text("RM ${String.format("%.2f", total)}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
        }
    }
}



@Composable
private fun PaymentMethodSection(paymentMethod: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Payment Method", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF424242))
        InfoRow("Method", paymentMethod)
    }
}

@Composable
private fun RemarksSection(remarks: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "Remarks",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF424242)
        )

        LazyColumn(
            modifier = Modifier.heightIn(max = 100.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(remarks) { remark ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color(0xFF2E7D32), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        remark,
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            fontSize = 14.sp,
            color = Color(0xFF666666)
        )
        Text(
            value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF424242)
        )
    }
}

