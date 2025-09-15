package com.example.rasago.theme.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rasago.ui.theme.RasagoApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptPage(
    navController: NavController,
    orderNo: String = "12345",
    orderTime: String = "2025-09-09 14:30",
    orderItems: List<Pair<String, Float>> = listOf("Nasi Lemak" to 10f, "Teh Tarik" to 3.5f),
    subtotal: Float = 13.5f,
    serviceCharge: Float = 1.35f,
    sst: Float = 0.81f,
    paymentMethod: String = "Cash",
    remarks: List<String> = listOf("No sugar", "Extra spicy"),
    onBackClick: () -> Unit = {},
    onProceedClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Receipt",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Receipt Card
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
                        // Order Info Section
                        OrderInfoSection(orderNo, orderTime)

                        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

                        // Order Items Section
                        OrderItemsSection(orderItems)

                        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

                        // Payment Summary Section
                        PaymentSummarySection(subtotal, serviceCharge, sst)

                        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

                        // Payment Method Section
                        PaymentMethodSection(paymentMethod)

                        // Remarks Section
                        if (remarks.isNotEmpty()) {
                            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                            RemarksSection(remarks)
                        }
                    }
                }

                // Proceed Button
                Button(
                    onClick = onProceedClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7D32),
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Continue Shopping",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderInfoSection(orderNo: String, orderTime: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "Order Information",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF424242)
        )

        InfoRow("Order Number", orderNo)
        InfoRow("Order Time", orderTime)
    }
}

@Composable
private fun OrderItemsSection(orderItems: List<Pair<String, Float>>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Order Items",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF424242)
        )

        LazyColumn(
            modifier = Modifier.heightIn(max = 200.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(orderItems) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            item.first,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF424242)
                        )
                        Text(
                            "RM ${String.format("%.2f", item.second)}",
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

@Composable
private fun PaymentSummarySection(subtotal: Float, serviceCharge: Float, sst: Float) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "Payment Summary",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF424242)
        )

        InfoRow("Subtotal", "RM ${String.format("%.2f", subtotal)}")
        InfoRow("Service Charge (10%)", "RM ${String.format("%.2f", serviceCharge)}")
        InfoRow("SST (6%)", "RM ${String.format("%.2f", sst)}")

        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

        val total = subtotal + serviceCharge + sst
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Total Payment",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF424242)
            )
            Text(
                "RM ${String.format("%.2f", total)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
        }
    }
}

@Composable
private fun PaymentMethodSection(paymentMethod: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "Payment Method",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF424242)
        )
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

@Preview(showBackground = true)
@Composable
fun ReceiptPreview() {
    MaterialTheme {
        ReceiptPage(
            navController = rememberNavController(),
            orderNo = "T12345",
            orderTime = "2025-01-15 14:30",
            orderItems = listOf(
                "Nasi Lemak" to 10.0f,
                "Teh Tarik" to 3.5f,
                "Roti Canai" to 4.0f
            ),
            subtotal = 17.5f,
            serviceCharge = 1.75f,
            sst = 1.05f,
            paymentMethod = "QR Scan",
            remarks = listOf("No sugar", "Extra spicy", "Thank you for your order!"),
            onBackClick = {},
            onProceedClick = {}
        )
    }
}
